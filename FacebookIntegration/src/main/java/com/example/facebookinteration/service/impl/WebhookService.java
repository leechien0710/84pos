package com.example.facebookinteration.service.impl;

import com.example.facebookinteration.constant.Constant;
import com.example.facebookinteration.constant.FunctionUtil;
import com.example.facebookinteration.convert.FacebookPostDtoToEntityConverter;
import com.example.facebookinteration.dto.facewebhook.Change;
import com.example.facebookinteration.dto.facewebhook.Entry;
import com.example.facebookinteration.dto.facewebhook.FacebookWebhookPayload;
import com.example.facebookinteration.dto.facewebhook.LiveVideosValue;
import com.example.facebookinteration.dto.res.LiveVideoRes;
import com.example.facebookinteration.entity.FacebookVideoEntity;
import com.example.facebookinteration.entity.FbPostEntity;
import com.example.facebookinteration.entity.PageEntity;
import com.example.facebookinteration.repository.FbPostRepo;
import com.example.facebookinteration.repository.FbVideoRepo;
import com.example.facebookinteration.repository.PageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfb.types.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebhookService {
    private final WebSocketHandlerService webSocketHandlerService;
    private final LiveServiceImpl liveServiceImpl;
    private final PostServiceSyncImpl postServiceSyncImpl;
    private final PageRepository pageRepository;
    private final FbPostRepo fbPostRepo;
    private final FacebookPostDtoToEntityConverter converter;
    private final FbVideoRepo fbVideoRepo;
    private final ObjectMapper objectMapper;
    private final FeedEventHandlerFactory handlerFactory;

    public void processPayload(FacebookWebhookPayload payload) {
        // <<< Tạo một ID định danh duy nhất cho mỗi lần webhook được gọi
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put("requestId", requestId);

        try {
            if (payload == null || payload.getEntry() == null) {
                log.warn("Payload or entry is null, skipping processing");
                return;
            }

            for (Entry entry : payload.getEntry()) {
                // <<< Thêm ID của page vào context để biết log này thuộc về page nào
                MDC.put("pageId", entry.getId());

                try {
                    if (entry.getChanges() == null) {
                        continue;
                    }

                    for (Change change : entry.getChanges()) {
                        if (change == null || change.getField() == null) {
                            continue;
                        }

                        // <<< (Tùy chọn) Thêm loại sự kiện đang xử lý
                        MDC.put("fieldType", change.getField());

                        switch (change.getField()) {
                            case Constant.Field.LIVE_VIDEOS:
                                log.info("Processing live_videos change");
                                processLiveVideos(change.getValue(), entry.getId());
                                break;
                            case Constant.Field.FEED:
                                log.info("Processing feed change");
                                processFeed(change.getValue(), entry.getId());
                                break;
                            default:
                                log.info("Unhandled field type: {}", change.getField());
                        }

                        MDC.remove("fieldType"); // <<< Dọn dẹp fieldType
                    }
                } finally {
                    // <<< LUÔN dọn dẹp pageId sau khi xử lý xong một entry
                    MDC.remove("pageId");
                }
            }
        } catch (Exception e) {
            // Log lỗi này giờ đây sẽ tự động chứa requestId và pageId (nếu có)
            log.error("Exception occurred while processing webhook payload: {}", payload, e);
        } finally {
            // <<< Chỉ dọn dẹp các key do service tự thêm, tránh xóa trace/span của OTel
            MDC.remove("requestId");
        }
    }

    public void processLiveVideos(Map<String, Object> value, String pageId) {
        LiveVideosValue liveVideosValue = FunctionUtil.mapToObject(value, LiveVideosValue.class);
        if ("live".equals(liveVideosValue.getStatus()) || "vod".equals(liveVideosValue.getStatus())) {
            PageEntity pageEntity = pageRepository.findByPageId(pageId).orElseThrow(
                    () -> new IllegalArgumentException(
                            "getFacebookLiveVideo Page access token not found for pageId: " + pageId));
            String liveId = liveVideosValue.getId();
            LiveVideoRes liveVideoRes = liveServiceImpl.getFacebookLiveVideo(liveId, pageEntity.getPageAccessToken());
            FacebookVideoEntity entity = FacebookVideoEntity.fromLiveVideoRes(liveVideoRes, pageId);
            webSocketHandlerService.sendMessageToPageSubscribers(pageId, entity, "live_videos");
            Post post = postServiceSyncImpl.getPostRest(entity.getPostId(), pageEntity.getPageAccessToken());
            FbPostEntity fbPostEntity = converter.convertFromRestFbPost(post);
            // Kiểm tra sự tồn tại của video trước khi lưu hoặc cập nhật
            Optional<FacebookVideoEntity> existingVideo = fbVideoRepo.findById(entity.getId());
            if (existingVideo.isPresent()) {
                // Nếu đã tồn tại thì cập nhật
                FacebookVideoEntity videoToUpdate = existingVideo.get();
                videoToUpdate.setLiveStatus(entity.getLiveStatus());
                // Cập nhật thêm các trường khác nếu cần
                fbVideoRepo.save(videoToUpdate); // Cập nhật video
            } else {
                // Nếu không tồn tại thì lưu mới
                fbVideoRepo.save(entity);
            }

            Optional<FbPostEntity> existingPost = fbPostRepo.findById(fbPostEntity.getId());
            if (existingPost.isPresent()) {
                FbPostEntity postToUpdate = existingPost.get();
                postToUpdate.setUpdatedTime(fbPostEntity.getUpdatedTime());
                fbPostRepo.save(postToUpdate);
            } else {
                fbPostRepo.save(fbPostEntity);
            }
        }
    }

    public void processFeed(Map<String, Object> value, String pageId) {
        String item = (String) value.get("item");
        if (item == null || item.isBlank()) {
            log.warn("Feed 'value' does not contain an 'item' field.");
            return;
        }

        try {
            // Chuyển Map từ webhook thành chuỗi JSON
            String jsonValue = objectMapper.writeValueAsString(value);

            // Ủy quyền hoàn toàn cho nhà máy để xử lý
            handlerFactory.process(item, jsonValue, pageId);

        } catch (JsonProcessingException e) {
            log.error("Failed to convert Map to JSON string. Item: '{}'", item, e);
        }
    }
}
