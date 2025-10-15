package com.example.facebookinteration.service.impl;

import com.example.facebookinteration.convert.FacebookPostDtoToEntityConverter;
import com.example.facebookinteration.dto.facebookdto.PostSync;
import com.example.facebookinteration.entity.FbPostEntity;
import com.example.facebookinteration.repository.FbPostRepo;
import com.example.facebookinteration.service.CommentService;
import com.example.facebookinteration.service.FacebookClient;
import com.example.facebookinteration.service.PostService;
import com.example.facebookinteration.utils.DataUtils;
import com.restfb.DefaultFacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.Post;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class PostServiceSyncImpl implements PostService {

    @Autowired
    private FacebookPostDtoToEntityConverter facebookPostDtoToEntityConverter;

    @Autowired
    private FbPostRepo fbPostRepo;

    @Autowired
    private CommentService commentService;

    private final FacebookClient facebookClient;

    public PostServiceSyncImpl(FacebookClient facebookClient) {
        this.facebookClient = facebookClient;
    }

    @Override
    public void syncPost(String pageId, String accessToken) {
        try {
            PostSync postSync = getPostsSync(pageId, accessToken);
            handleSyncPost(postSync.getPosts(), pageId, accessToken);
        } catch (Exception e) {
            log.error("syncPost (sync) has error occurred: {}", e.getMessage(), e);
        }
    }

    private PostSync getPostsSync(String pageId, String accessToken) {
        Map<String, String> params = Map.of(
                "fields",
                "posts{message,id,full_picture,created_time,updated_time,attachments{title},status_type,likes.summary(true).limit(0),comments.summary(true).limit(0),is_published}");
        return facebookClient.callApiSync(pageId, params, accessToken, PostSync.class);
    }

    public void handleSyncPost(PostSync.Posts firstPagePosts, String pageId, String accessToken) {
        log.info("POST_START: pageId={}", pageId);
        
        PostSync.Posts currentPosts = firstPagePosts;
        List<CompletableFuture<Void>> commentFutures = new ArrayList<>();
        int totalPosts = 0;

        while (currentPosts != null) {
            List<FbPostEntity> currentBatch = new ArrayList<>();

            for (PostSync.Post post : currentPosts.getData()) {
                FbPostEntity fbPostEntity = facebookPostDtoToEntityConverter.convert(post);
                fbPostEntity.setPageId(pageId);
                currentBatch.add(fbPostEntity);
            }

            fbPostRepo.saveAll(currentBatch);
            totalPosts += currentBatch.size();
            log.info("POST_SAVED: pageId={}, batch={}, total={}", pageId, currentBatch.size(), totalPosts);

            // Sync comment cho từng post và gom các task vào danh sách
            log.info("COMMENT_QUEUE: pageId={}, queuing {} posts for comment sync", pageId, currentBatch.size());
            for (int i = 0; i < currentBatch.size(); i++) {
                FbPostEntity post = currentBatch.get(i);
                
                // Thêm delay giữa các request để tránh rate limiting
                if (i > 0) {
                    try {
                        Thread.sleep(1000); // 1 giây delay giữa các request
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        log.info("COMMENT_START: pageId={}, postId={}", pageId, post.getId());
                        commentService.syncComment(post.getId(), accessToken);
                        log.info("COMMENT_DONE: pageId={}, postId={}", pageId, post.getId());
                    } catch (Exception e) {
                        log.error("COMMENT_ERROR: pageId={}, postId={}, error={}", 
                                pageId, post.getId(), e.getMessage());
                    }
                });
                commentFutures.add(future);
            }

            // Phân trang tiếp
            String nextPageUrl = currentPosts.getPaging().getNext();
            if (nextPageUrl == null || nextPageUrl.isEmpty()) {
                break;
            }

            try {
                Object rawResponse = facebookClient.callApiByUrlSync(nextPageUrl);
                currentPosts = DataUtils.convertToTargetType(rawResponse, PostSync.Posts.class);
            } catch (Exception e) {
                log.error("POST_API_ERROR: pageId={}, error={}", pageId, e.getMessage());
                break;
            }
        }

        // Đợi tất cả comment sync xong
        CompletableFuture.allOf(commentFutures.toArray(new CompletableFuture[0])).join();
        
        log.info("POST_COMPLETE: pageId={}, totalPosts={}", pageId, totalPosts);
    }

    PostSync.Post getPost(String postId, String accessToken) {
        com.restfb.FacebookClient facebookClient = new DefaultFacebookClient(accessToken, Version.VERSION_19_0);

		return facebookClient.fetchObject(
				postId,
				PostSync.Post.class,
				Parameter.with("fields", "message,id,full_picture,created_time,updated_time," +
						"attachments{title},status_type,likes.summary(true).limit(0)," +
						"comments.summary(true).limit(0),is_published")
		);
    }

    Post getPostRest(String postId, String accessToken) {
        com.restfb.FacebookClient facebookClient = new DefaultFacebookClient(accessToken, Version.VERSION_19_0);

		return facebookClient.fetchObject(
				postId,
				Post.class,
				Parameter.with("fields", "message,id,full_picture,created_time,updated_time," +
						"attachments{title},status_type,likes.summary(true).limit(0)," +
						"comments.summary(true).limit(0),is_published")
		);
    }

}
