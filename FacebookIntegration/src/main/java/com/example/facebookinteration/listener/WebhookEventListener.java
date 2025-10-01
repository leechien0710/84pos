package com.example.facebookinteration.listener;

import com.example.facebookinteration.convert.FacebookCommentDtoToEntityConverter;
import com.example.facebookinteration.entity.CommentEntity;
import com.example.facebookinteration.entity.FbPostEntity;
import com.example.facebookinteration.entity.PageEntity;
import com.example.facebookinteration.event.NewCommentEvent;
import com.example.facebookinteration.repository.FbCommentRepo;
import com.example.facebookinteration.repository.FbPostRepo;
import com.example.facebookinteration.repository.PageRepository;
import com.example.facebookinteration.service.impl.WebSocketHandlerService;
import com.restfb.DefaultFacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Event Listener xử lý bất đồng bộ các sự kiện webhook từ Facebook
 * Chạy trên thread riêng biệt để không block webhook response
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookEventListener {

    private final FbCommentRepo fbCommentRepo;
    private final FbPostRepo fbPostRepo;
    private final PageRepository pageRepository;
    private final FacebookCommentDtoToEntityConverter facebookCommentDtoToEntityConverter;
    private final WebSocketHandlerService webSocketHandlerService;

    /**
     * Xử lý bất đồng bộ sự kiện comment mới từ webhook
     * Chạy trên thread riêng để không block webhook response
     */
    @EventListener
    @Async("webhookTaskExecutor")
    public void handleNewCommentEvent(NewCommentEvent event) {
        try {
            log.info("Processing NewCommentEvent asynchronously for comment: {}", 
                    event.getCommentValue().getCommentId());
            
            // Chuyển đổi DTO sang Entity
            CommentEntity commentEntity = facebookCommentDtoToEntityConverter.toEntity(event.getCommentValue());
            
            // Check nếu post đã ẩn tất cả comment thì ẩn comment mới này luôn
            String postId = commentEntity.getPostId();
            if (postId != null) {
                FbPostEntity post = fbPostRepo.findById(postId).orElse(null);
                if (post != null && Boolean.TRUE.equals(post.getAllCommentsHidden())) {
                    log.info("Post {} has all comments hidden, hiding new comment {}", 
                            postId, commentEntity.getId());
                    commentEntity.setIsHidden(true);
                    
                    // Gọi Facebook API để ẩn comment thật sự
                    hideCommentOnFacebook(commentEntity, event.getPageId());
                }
            }
            
            // Lưu comment vào database
            fbCommentRepo.save(commentEntity);
            
            // Gửi WebSocket message SAU KHI đã xử lý xong để đảm bảo trạng thái cuối cùng
            webSocketHandlerService.sendMessageToPageSubscribers(event.getPageId(), event.getCommentValue(), "Comment");
            
            log.info("Successfully processed NewCommentEvent for comment: {} and sent WebSocket notification", 
                    commentEntity.getId());
            
        } catch (Exception e) {
            log.error("Error processing NewCommentEvent: {}", e.getMessage(), e);
        }
    }

    /**
     * Gọi Facebook API để ẩn comment
     */
    private void hideCommentOnFacebook(CommentEntity commentEntity, String pageId) {
        try {
            PageEntity pageEntity = pageRepository.findByPageId(pageId).orElse(null);
            if (pageEntity != null && pageEntity.getPageAccessToken() != null) {
                com.restfb.FacebookClient fbClient = new DefaultFacebookClient(
                        pageEntity.getPageAccessToken(), Version.LATEST);
                fbClient.publish(commentEntity.getId(), Boolean.class, 
                        Parameter.with("is_hidden", true));
                log.info("Facebook API: Comment {} hidden successfully via webhook", 
                        commentEntity.getId());
            } else {
                log.warn("Page access token not found for pageId: {}", pageId);
            }
        } catch (Exception e) {
            log.error("Facebook API failed to hide comment {} via webhook: {}", 
                    commentEntity.getId(), e.getMessage(), e);
            // Vẫn tiếp tục lưu vào database với trạng thái ẩn
        }
    }
}
