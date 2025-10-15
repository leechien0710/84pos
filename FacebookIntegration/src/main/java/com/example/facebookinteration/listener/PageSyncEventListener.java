package com.example.facebookinteration.listener;

import com.example.facebookinteration.constant.enums.PageStatus;
import com.example.facebookinteration.dto.notification.SyncNotificationDto;
import com.example.facebookinteration.entity.PageEntity;
import com.example.facebookinteration.event.PageSyncEvent;
import com.example.facebookinteration.repository.PageRepository;
import com.example.facebookinteration.service.impl.PageService;
import com.example.facebookinteration.service.PostService;
import com.example.facebookinteration.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class PageSyncEventListener {

    @Autowired
    private PageRepository pageRepository;


    @Autowired
    private PageService pageService;

    @Autowired
    private PostService postService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @Async("syncTaskExecutor")
    @EventListener
    @Retryable(
        value = { Exception.class }, // Thử lại với mọi Exception
        maxAttempts = 3, // Thử lại tối đa 3 lần
        backoff = @Backoff(delay = 2000) // Chờ 2 giây giữa các lần thử
    )
    public void handlePageSyncEvent(PageSyncEvent event) {
        List<String> pageIds = event.getPageIds();
        String userId = event.getUserId();
        Long userAppId = event.getUserAppId();
        
        log.info("SYNC_START: {} pages for userAppId: {}", pageIds.size(), userAppId);
        
        // Lấy tất cả page entities
        List<PageEntity> pageEntities = pageRepository.findAllById(pageIds);
        if (pageEntities.size() != pageIds.size()) {
            throw new RuntimeException("Some PageEntities not found. Expected: " + pageIds.size() + ", Found: " + pageEntities.size());
        }

        // Tạo CompletableFuture cho mỗi page
        List<CompletableFuture<Void>> pageSyncFutures = new ArrayList<>();
        
        for (PageEntity pageEntity : pageEntities) {
            String pageId = pageEntity.getPageId();
            String pageAccessToken = pageEntity.getPageAccessToken();
            
            CompletableFuture<Void> pageSyncFuture = CompletableFuture.runAsync(() -> {
                try {
                    // Đồng bộ post và video cho page này
                    CompletableFuture<Void> postSyncFuture = CompletableFuture.runAsync(() -> {
                        postService.syncPost(pageId, pageAccessToken);
                    });
                    
                    CompletableFuture<Void> videoSyncFuture = CompletableFuture.runAsync(() -> {
                        videoService.syncVideo(pageId, pageAccessToken);
                    });
                    
                    // Đợi cả 2 task hoàn tất cho page này
                    CompletableFuture.allOf(postSyncFuture, videoSyncFuture).join();
                    
                } catch (Exception e) {
                    log.error("SYNC_ERROR: pageId={}, error={}", pageId, e.getMessage(), e);
                    throw new RuntimeException("Failed to sync pageId: " + pageId, e);
                }
            });
            
            pageSyncFutures.add(pageSyncFuture);
        }
        
        // Đợi tất cả pages hoàn tất
        CompletableFuture.allOf(pageSyncFutures.toArray(new CompletableFuture[0])).join();

        // Cập nhật trạng thái thành công cho tất cả pages
        pageService.updatePageStatus(pageIds, PageStatus.ACTIVE.getValue());
        
        log.info("SYNC_SUCCESS: {} pages for userAppId: {}", pageIds.size(), userAppId);
        
        // Gửi 1 thông báo cho tất cả pages
        sendBulkSyncStatusNotification(pageIds, PageStatus.ACTIVE.getValue(), "Đồng bộ thành công!", userAppId);
    }

    @Recover
    public void recover(Exception e, PageSyncEvent event) {
        List<String> pageIds = event.getPageIds();
        Long userAppId = event.getUserAppId();
        log.error("SYNC_RETRY_FAILED: {} pages, userAppId={}, error={} (after 3 retries)", 
                pageIds.size(), userAppId, e.getMessage());
        
        // Cập nhật trạng thái thất bại cho tất cả pages
        pageService.updatePageStatus(pageIds, PageStatus.FAILED.getValue());
        sendBulkSyncStatusNotification(pageIds, PageStatus.FAILED.getValue(), "Đồng bộ thất bại: " + e.getMessage(), userAppId);
    }


    private void sendBulkSyncStatusNotification(List<String> pageIds, Integer status, String message, Long userAppId) {
        if (pageIds.isEmpty()) {
            log.warn("Cannot send bulk notification because pageIds list is empty.");
            return;
        }

        if (userAppId == null) {
            log.warn("Cannot send bulk notification because userAppId is null.");
            return;
        }

        // Gửi thông báo DTO object
        String destination = "/topic/sync-status/" + userAppId;
        SyncNotificationDto notification = SyncNotificationDto.builder()
                .status(status) // 1=ACTIVE, 2=INACTIVE, 3=SYNCING, 4=FAILED
                .message(message)
                .build();
        
        messagingTemplate.convertAndSend(destination, notification);
        log.info("Sent sync notification to {}: {} pages, status={}, message={}", destination, pageIds.size(), notification.getStatus(), notification.getMessage());
    }
}
