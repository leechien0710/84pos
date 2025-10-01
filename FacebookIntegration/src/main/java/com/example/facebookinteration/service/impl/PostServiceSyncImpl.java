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
        long totalStart = System.currentTimeMillis();

        PostSync.Posts currentPosts = firstPagePosts;
        List<CompletableFuture<Void>> commentFutures = new ArrayList<>();

        while (currentPosts != null) {
            long saveStart = System.currentTimeMillis();
            List<FbPostEntity> currentBatch = new ArrayList<>();

            for (PostSync.Post post : currentPosts.getData()) {
                FbPostEntity fbPostEntity = facebookPostDtoToEntityConverter.convert(post);
                fbPostEntity.setPageId(pageId);
                currentBatch.add(fbPostEntity);
            }

            fbPostRepo.saveAll(currentBatch);
            log.info("[{}] Lưu {} post vào DB mất {}ms", pageId, currentBatch.size(),
                    System.currentTimeMillis() - saveStart);

            // Sync comment cho từng post và gom các task vào danh sách
            for (FbPostEntity post : currentBatch) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    long commentStart = System.currentTimeMillis();
                    try {
                        commentService.syncComment(post.getId(), accessToken);
                        log.info("[{}] ✅ Sync xong comment cho postId={} trong {}ms",
                                pageId, post.getId(), System.currentTimeMillis() - commentStart);
                    } catch (Exception e) {
                        log.error("❌ Lỗi sync comment cho postId={} - {}", post.getId(), e.getMessage(), e);
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
                long apiStart = System.currentTimeMillis();
                Object rawResponse = facebookClient.callApiByUrlSync(nextPageUrl);
                currentPosts = DataUtils.convertToTargetType(rawResponse, PostSync.Posts.class);
                log.info("[{}] Gọi API lấy trang kế tiếp mất {}ms", pageId, System.currentTimeMillis() - apiStart);
            } catch (Exception e) {
                log.error("❌ Lỗi gọi API trang kế tiếp cho pageId={} - {}", pageId, e.getMessage(), e);
                break;
            }
        }

        // 🔚 Đợi tất cả comment sync xong rồi mới log tổng thời gian
        CompletableFuture.allOf(commentFutures.toArray(new CompletableFuture[0])).join();

        log.info("[{}] ✅ Hoàn tất đồng bộ tất cả bài viết và comment, tổng thời gian: {}ms",
                pageId, System.currentTimeMillis() - totalStart);
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
