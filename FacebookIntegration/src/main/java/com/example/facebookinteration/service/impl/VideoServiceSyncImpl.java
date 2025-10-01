package com.example.facebookinteration.service.impl;

import com.example.facebookinteration.convert.FacebookVideoConvert;
import com.example.facebookinteration.dto.facebookdto.FacebookVideoResponse;
import com.example.facebookinteration.entity.FacebookVideoEntity;
import com.example.facebookinteration.repository.FbVideoRepo;
import com.example.facebookinteration.service.FacebookClient;
import com.example.facebookinteration.service.VideoService;
import com.example.facebookinteration.utils.DataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoServiceSyncImpl implements VideoService {

    private final FacebookVideoConvert facebookVideoConvert;
    private final FbVideoRepo fbVideoRepo;
    private final FacebookClient facebookClient;

    @Override
    public void syncVideo(String pageId, String accessToken) {
        try {
            FacebookVideoResponse videoResponse = getVideosSync(pageId, accessToken);
            handleSyncVideo(videoResponse.getVideos(), pageId);
        } catch (Exception e) {
            log.error("syncVideo (sync) error: {}", e.getMessage(), e);
        }
    }

    private FacebookVideoResponse getVideosSync(String pageId, String accessToken) {
        Map<String, String> params = Map.of(
                "fields", "videos{live_status,post_id,comments{comment_count},embed_html}");
        return facebookClient.callApiSync(pageId, params, accessToken, FacebookVideoResponse.class);
    }

    public void handleSyncVideo(FacebookVideoResponse.Videos videos, String pageId) {
        if (videos == null || videos.getData() == null) {
            log.info("No videos to sync for pageId={}", pageId);
            return;
        }

        FacebookVideoResponse.Videos currentVideos = videos;

        while (currentVideos != null && currentVideos.getData() != null && !currentVideos.getData().isEmpty()) {
            List<FacebookVideoEntity> entities = new ArrayList<>();
            currentVideos.getData().forEach(video -> {
                FacebookVideoEntity entity = facebookVideoConvert.convert(video, pageId);
                entities.add(entity);
            });

            fbVideoRepo.saveAll(entities);

            String nextPageUrl = currentVideos.getPaging() != null ? currentVideos.getPaging().getNext() : null;
            if (nextPageUrl == null || nextPageUrl.isEmpty()) {
                break;
            }

            try {
                Object raw = facebookClient.callApiByUrlSync(nextPageUrl);
                currentVideos = DataUtils.convertToTargetType(raw, FacebookVideoResponse.Videos.class);
            } catch (Exception e) {
                log.error("Error while fetching next video page (sync): {}", e.getMessage(), e);
                break;
            }
        }
    }

}
