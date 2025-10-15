package com.example.facebookinteration.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.example.facebookinteration.constant.Constant;
import com.example.facebookinteration.constant.exception.CustomException;
import com.example.facebookinteration.convert.FacebookCommentDtoToEntityConverter;
import com.example.facebookinteration.dto.facebookdto.FacebookCommentResponse;
import com.example.facebookinteration.entity.CommentEntity;
import com.example.facebookinteration.entity.FbPostEntity;
import com.example.facebookinteration.entity.Phone;
import com.example.facebookinteration.repository.FbCommentRepo;
import com.example.facebookinteration.repository.FbPostRepo;
import com.example.facebookinteration.repository.PhoneRepository;
import com.example.facebookinteration.service.CommentService;
import com.example.facebookinteration.service.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.batch.BatchRequest;
import com.restfb.batch.BatchResponse;
import com.restfb.exception.FacebookException;
import com.example.facebookinteration.utils.DataUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommentServiceSyncImpl implements CommentService {

    @Autowired
    private FacebookCommentDtoToEntityConverter commentDtoToEntityConverter;

    @Autowired
    private FbCommentRepo commentRepo;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private FbPostRepo postRepo;

    private final FacebookClient facebookClient;

    public CommentServiceSyncImpl(FacebookClient facebookClient) {
        this.facebookClient = facebookClient;
    }

    @Override
    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    public void syncComment(String postId, String accessToken) {
        try {
            log.info("COMMENT_API_START: postId={}", postId);
            FacebookCommentResponse commentResponse = getCommentsSync(postId, accessToken);
            log.info("COMMENT_API_DONE: postId={}", postId);
            
            log.info("COMMENT_HANDLE_START: postId={}", postId);
            handleSyncComments(commentResponse.getComments(), postId);
            log.info("COMMENT_HANDLE_DONE: postId={}", postId);
        } catch (Exception e) {
            log.error("COMMENT_SYNC_ERROR: postId={}, error={}", postId, e.getMessage());
            throw e;
        }
    }

    @Recover
    public void recover(Exception e, String postId, String accessToken) {
        log.error("COMMENT_RETRY_FAILED: postId={}, error={} (after 3 retries)", postId, e.getMessage());
        // In a real-world scenario, you might want to mark the post as failed to sync comments.
        // For now, we just log the final failure.
    }

    public FacebookCommentResponse getCommentsSync(String postId, String accessToken) {
        log.info("COMMENT_API_CALL: postId={}, token={}..., calling Facebook API with RestFB", postId, 
                accessToken.substring(0, Math.min(10, accessToken.length())));
        long startTime = System.currentTimeMillis();
        
        try {
            com.restfb.FacebookClient fbClient = new com.restfb.DefaultFacebookClient(accessToken, com.restfb.Version.LATEST);
            
            com.restfb.types.Comment comment = fbClient.fetchObject(postId, com.restfb.types.Comment.class, 
                    com.restfb.Parameter.with("fields", "comments{attachment,from,created_time,message,comments{attachment,from,message}}"));
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("COMMENT_API_RESPONSE: postId={}, duration={}ms, using RestFB", postId, duration);
            
            // Convert RestFB Comment to FacebookCommentResponse
            return convertRestFBToResponse(comment);
            
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("COMMENT_API_FAILED: postId={}, duration={}ms, error={}", postId, duration, e.getMessage());
            throw e;
        }
    }
    
    private FacebookCommentResponse convertRestFBToResponse(com.restfb.types.Comment restFBComment) {
        FacebookCommentResponse response = new FacebookCommentResponse();
        
        if (restFBComment != null && restFBComment.getComments() != null) {
            FacebookCommentResponse.Comments comments = new FacebookCommentResponse.Comments();
            java.util.List<FacebookCommentResponse.CommentData> commentDataList = new java.util.ArrayList<>();
            
            for (com.restfb.types.Comment childComment : restFBComment.getComments().getData()) {
                FacebookCommentResponse.CommentData commentData = new FacebookCommentResponse.CommentData();
                commentData.setId(childComment.getId());
                commentData.setMessage(childComment.getMessage());
                // Convert Date to ISO string format
                if (childComment.getCreatedTime() != null) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                    commentData.setCreated_time(sdf.format(childComment.getCreatedTime()));
                }
                
                if (childComment.getFrom() != null) {
                    FacebookCommentResponse.CommentData.From from = new FacebookCommentResponse.CommentData.From();
                    from.setId(childComment.getFrom().getId());
                    from.setName(childComment.getFrom().getName());
                    commentData.setFrom(from);
                }
                
                commentDataList.add(commentData);
            }
            
            comments.setData(commentDataList);
            response.setComments(comments);
        }
        
        return response;
    }
    
    private String extractAccessTokenFromUrl(String url) {
        try {
            java.net.URI uri = java.net.URI.create(url);
            String query = uri.getQuery();
            if (query != null) {
                String[] params = query.split("&");
                for (String param : params) {
                    if (param.startsWith("access_token=")) {
                        return param.substring("access_token=".length());
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Failed to extract access token from URL: {}", url);
        }
        return null;
    }

    @Override
    public boolean hideComment(String commentId, String accessToken) {
        try {
            CommentEntity comment = commentRepo.findById(commentId).orElse(null);
            if (comment == null) {
                log.warn("Comment not found with id: {}", commentId);
                throw new CustomException(404, "Comment đã chọn không còn tồn tại");
            }
            
            // Gửi yêu cầu ẩn comment đến Facebook bằng RestFB
            try {
                com.restfb.FacebookClient fbClient = new com.restfb.DefaultFacebookClient(accessToken, Version.LATEST);
                fbClient.publish(commentId, Boolean.class, Parameter.with("is_hidden", true));
                log.info("Facebook API: Comment {} hidden successfully", commentId);
            } catch (Exception e) {
                log.warn("Facebook API failed to hide comment {}: {}", commentId, e.getMessage());
                // Vẫn tiếp tục ẩn trong database local
            }
            
            // Cập nhật database local
            comment.setIsHidden(true);
            commentRepo.save(comment);
            log.info("Comment {} has been hidden locally", commentId);
            return true;
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            log.error("Error hiding comment {}: {}", commentId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean showComment(String commentId, String accessToken) {
        try {
            CommentEntity comment = commentRepo.findById(commentId).orElse(null);
            if (comment == null) {
                log.warn("Comment not found with id: {}", commentId);
                throw new CustomException(404, "Comment đã chọn không còn tồn tại");
            }
            
            // Gửi yêu cầu hiện comment đến Facebook bằng RestFB
            try {
                com.restfb.FacebookClient fbClient = new com.restfb.DefaultFacebookClient(accessToken, Version.LATEST);
                fbClient.publish(commentId, Boolean.class, Parameter.with("is_hidden", false));
                log.info("Facebook API: Comment {} shown successfully", commentId);
            } catch (Exception e) {
                log.warn("Facebook API failed to show comment {}: {}", commentId, e.getMessage());
                // Vẫn tiếp tục hiện trong database local
            }
            
            // Cập nhật database local
            comment.setIsHidden(false);
            commentRepo.save(comment);
            log.info("Comment {} has been shown locally", commentId);
            return true;
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            log.error("Error showing comment {}: {}", commentId, e.getMessage(), e);
            return false;
        }
    }

    public void handleSyncComments(FacebookCommentResponse.Comments comments, String postId) {
        log.info("COMMENT_HANDLE_START: postId={}, comments={}", postId, 
                comments != null && comments.getData() != null ? comments.getData().size() : 0);
        
        if (comments == null || comments.getData() == null) {
            log.info("COMMENT_HANDLE_EMPTY: postId={}", postId);
            return;
        }

        Map<String, String> phoneMap = new HashMap<>();
        List<CommentEntity> allComments = new ArrayList<>();
        String nextPageUrl = comments.getPaging() != null ? comments.getPaging().getNext() : null;
        int pageCount = 0;

        do {
            pageCount++;
            log.info("COMMENT_PAGE_START: postId={}, page={}, comments={}", postId, pageCount, comments.getData().size());
            
            List<CommentEntity> commentEntities = new ArrayList<>();

            for (FacebookCommentResponse.CommentData commentData : comments.getData()) {
                if (commentData == null)
                    continue;

                processComment(commentData, postId, commentEntities, allComments, phoneMap);

                // Sub-comments
                List<FacebookCommentResponse.CommentData> childComments = commentData.getData();
                if (childComments != null) {
                    for (FacebookCommentResponse.CommentData child : childComments) {
                        if (child != null) {
                            processComment(child, postId, commentEntities, allComments, phoneMap);
                        }
                    }
                }
            }

            log.info("COMMENT_SAVE_START: postId={}, page={}, comments={}", postId, pageCount, commentEntities.size());
            commentRepo.saveAll(commentEntities);
            log.info("COMMENT_SAVE_DONE: postId={}, page={}, comments={}", postId, pageCount, commentEntities.size());

            // Fetch next page
            if (nextPageUrl != null && !nextPageUrl.isEmpty()) {
                try {
                    log.info("COMMENT_NEXT_PAGE: postId={}, page={}, url={}", postId, pageCount + 1, nextPageUrl);
                    
                    // Parse access token from URL
                    String accessToken = extractAccessTokenFromUrl(nextPageUrl);
                    com.restfb.FacebookClient fbClient = new com.restfb.DefaultFacebookClient(accessToken, com.restfb.Version.LATEST);
                    
                    // Use RestFB to fetch next page
                    com.restfb.types.Comment nextComment = fbClient.fetchObject(postId, com.restfb.types.Comment.class, 
                            com.restfb.Parameter.with("fields", "comments{attachment,from,created_time,message,comments{attachment,from,message}}"));
                    
                    FacebookCommentResponse nextResponse = convertRestFBToResponse(nextComment);
                    comments = nextResponse.getComments();
                    nextPageUrl = comments.getPaging() != null ? comments.getPaging().getNext() : null;
                } catch (Exception e) {
                    log.error("COMMENT_PAGE_ERROR: postId={}, error={}", postId, e.getMessage());
                    break;
                }
            } else {
                nextPageUrl = null;
            }

        } while (nextPageUrl != null);

        // Xử lý lưu số điện thoại sau khi đã gom hết
        log.info("COMMENT_PHONE_START: postId={}, phones={}", postId, phoneMap.size());
        savePhonesFromMap(phoneMap);
        log.info("COMMENT_PHONE_DONE: postId={}", postId);
        
        // Log tổng kết
        log.info("COMMENT_COMPLETE: postId={}, totalComments={}, pages={}", postId, allComments.size(), pageCount);
    }

    private void processComment(FacebookCommentResponse.CommentData commentData,
            String postId,
            List<CommentEntity> commentEntities,
            List<CommentEntity> allComments,
            Map<String, String> phoneMap) {

        CommentEntity entity = commentDtoToEntityConverter.convert(commentData);
        entity.setPostId(postId);

        String message = entity.getMessage();
        boolean hasPhone = false;

        if (message != null && !message.isEmpty()) {
            List<String> phoneNumbers = Constant.extractPhoneNumbers(message);
            if (!phoneNumbers.isEmpty()) {
                hasPhone = true;

                // Parse JSON string to get fromId directly
                String fromId = null;
                if (entity.getFromUser() != null) {
                    try {
                        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                        com.fasterxml.jackson.databind.JsonNode jsonNode = mapper.readTree(entity.getFromUser());
                        fromId = jsonNode.get("id").asText();
                    } catch (Exception e) {
                        log.warn("Failed to parse fromUser JSON: {}", entity.getFromUser());
                    }
                }

                for (String phoneNumber : phoneNumbers) {
                    phoneMap.put(phoneNumber, fromId);
                }
            }
        }

        entity.setHasPhone(hasPhone);
        commentEntities.add(entity);
        allComments.add(entity);
    }

    private void savePhonesFromMap(Map<String, String> phoneMap) {
        if (phoneMap.isEmpty())
            return;

        long start = System.currentTimeMillis();

        List<String> phoneNumbers = new ArrayList<>(phoneMap.keySet());
        List<Phone> existingPhones = phoneRepository.findByPhoneNumberIn(phoneNumbers);

        Map<String, Phone> existingPhoneMap = existingPhones.stream()
                .collect(Collectors.toMap(Phone::getPhoneNumber, Function.identity()));

        List<Phone> phonesToSave = new ArrayList<>();

        for (Map.Entry<String, String> entry : phoneMap.entrySet()) {
            String phoneNumber = entry.getKey();
            String senderId = entry.getValue();

            Phone phone = existingPhoneMap.get(phoneNumber);
            if (phone != null) {
                phone.setSenderId(senderId); // update
            } else {
                phone = Phone.builder()
                        .phoneNumber(phoneNumber)
                        .senderId(senderId)
                        .build();
            }

            phonesToSave.add(phone);
        }

        phoneRepository.saveAll(phonesToSave);
    }

    @Override
    public int hideAllCommentsByPostId(String postId, String accessToken) {
        try {
            List<CommentEntity> comments = commentRepo.findByPostId(postId);
            if (comments.isEmpty()) {
                log.warn("Không tìm thấy comment nào cho postId: {}", postId);
                return 0;
            }
            
            // Lọc comment cần ẩn
            List<CommentEntity> commentsToHide = comments.stream()
                .filter(comment -> !Boolean.TRUE.equals(comment.getIsHidden()))
                .collect(Collectors.toList());
            
            if (commentsToHide.isEmpty()) {
                log.info("Tất cả comment của postId {} đã được ẩn", postId);
                return 0;
            }
            
            // Gọi Facebook API theo batch để tránh rate limit
            int facebookSuccessCount = callFacebookBatchHideComments(commentsToHide, accessToken);
            
            // Cập nhật database local
            commentsToHide.forEach(comment -> comment.setIsHidden(true));
            commentRepo.saveAll(commentsToHide);
            
            // Cập nhật flag allCommentsHidden cho post
            updatePostAllCommentsHiddenFlag(postId, true);
            
            log.info("Đã ẩn {} comment cho postId: {} (Facebook API success: {})", 
                commentsToHide.size(), postId, facebookSuccessCount);
            
            return commentsToHide.size();
        } catch (Exception e) {
            log.error("Lỗi khi ẩn tất cả comment cho postId {}: {}", postId, e.getMessage(), e);
            throw new CustomException(500, "Lỗi khi ẩn tất cả comment: " + e.getMessage());
        }
    }

    @Override
    public int showAllCommentsByPostId(String postId, String accessToken) {
        try {
            List<CommentEntity> comments = commentRepo.findByPostId(postId);
            if (comments.isEmpty()) {
                log.warn("Không tìm thấy comment nào cho postId: {}", postId);
                return 0;
            }
            
            // Lọc comment cần hiện
            List<CommentEntity> commentsToShow = comments.stream()
                .filter(comment -> Boolean.TRUE.equals(comment.getIsHidden()))
                .collect(Collectors.toList());
            
            if (commentsToShow.isEmpty()) {
                log.info("Tất cả comment của postId {} đã được hiện", postId);
                return 0;
            }
            
            // Gọi Facebook API theo batch để tránh rate limit
            int facebookSuccessCount = callFacebookBatchShowComments(commentsToShow, accessToken);
            
            // Cập nhật database local
            commentsToShow.forEach(comment -> comment.setIsHidden(false));
            commentRepo.saveAll(commentsToShow);
            
            // Cập nhật flag allCommentsHidden cho post
            updatePostAllCommentsHiddenFlag(postId, false);
            
            log.info("Đã hiện {} comment cho postId: {} (Facebook API success: {})", 
                commentsToShow.size(), postId, facebookSuccessCount);
            
            return commentsToShow.size();
        } catch (Exception e) {
            log.error("Lỗi khi hiện tất cả comment cho postId {}: {}", postId, e.getMessage(), e);
            throw new CustomException(500, "Lỗi khi hiện tất cả comment: " + e.getMessage());
        }
    }

    /**
     * Gọi Facebook API ẩn comment theo batch để tránh rate limit
     */
    private int callFacebookBatchHideComments(List<CommentEntity> comments, String accessToken) {
        if (comments.isEmpty()) {
            return 0;
        }
        
        com.restfb.FacebookClient fbClient = new com.restfb.DefaultFacebookClient(accessToken, Version.LATEST);
        int successCount = 0;
        int batchSize = 50; // Xử lý 50 comment mỗi batch
        
        for (int i = 0; i < comments.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, comments.size());
            List<CommentEntity> batch = comments.subList(i, endIndex);
            
            log.info("Xử lý batch ẩn comment {}/{} ({} comment)", 
                (i / batchSize) + 1, (comments.size() + batchSize - 1) / batchSize, batch.size());
            
            try {
                // Tạo danh sách batch requests
                List<BatchRequest> batchRequests = new ArrayList<>();
                
                // Thêm từng comment vào batch request
                for (CommentEntity comment : batch) {
                    batchRequests.add(new BatchRequest.BatchRequestBuilder(comment.getId())
                        .method("POST")
                        .body(Parameter.with("is_hidden", true))
                        .build());
                }
                
                // Thực hiện batch request
                List<BatchResponse> responses = fbClient.executeBatch(batchRequests);
                
                // Xử lý kết quả từng response
                for (int j = 0; j < responses.size(); j++) {
                    BatchResponse response = responses.get(j);
                    CommentEntity comment = batch.get(j);
                    
                    if (response.getCode() == 200) {
                        successCount++;
                        log.debug("Successfully hidden comment: {}", comment.getId());
                    } else {
                        log.warn("Failed to hide comment {}: HTTP {} - {}", 
                            comment.getId(), response.getCode(), response.getBody());
                    }
                }
                
            } catch (FacebookException e) {
                log.error("Facebook batch API failed for hide comments batch: {}", e.getMessage(), e);
                // Tiếp tục xử lý batch tiếp theo
            }
            
            // Delay giữa các batch
            if (endIndex < comments.size()) {
                try {
                    Thread.sleep(1000); // 1000ms delay giữa các batch
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        
        return successCount;
    }

    /**
     * Gọi Facebook API hiện comment theo batch để tránh rate limit
     */
    private int callFacebookBatchShowComments(List<CommentEntity> comments, String accessToken) {
        if (comments.isEmpty()) {
            return 0;
        }
        
        com.restfb.FacebookClient fbClient = new com.restfb.DefaultFacebookClient(accessToken, Version.LATEST);
        int successCount = 0;
        int batchSize = 50; // Xử lý 50 comment mỗi batch
        
        for (int i = 0; i < comments.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, comments.size());
            List<CommentEntity> batch = comments.subList(i, endIndex);
            
            log.info("Xử lý batch hiện comment {}/{} ({} comment)", 
                (i / batchSize) + 1, (comments.size() + batchSize - 1) / batchSize, batch.size());
            
            try {
                // Tạo danh sách batch requests
                List<BatchRequest> batchRequests = new ArrayList<>();
                
                // Thêm từng comment vào batch request
                for (CommentEntity comment : batch) {
                    batchRequests.add(new BatchRequest.BatchRequestBuilder(comment.getId())
                        .method("POST")
                        .body(Parameter.with("is_hidden", false))
                        .build());
                }
                
                // Thực hiện batch request
                List<BatchResponse> responses = fbClient.executeBatch(batchRequests);
                
                // Xử lý kết quả từng response
                for (int j = 0; j < responses.size(); j++) {
                    BatchResponse response = responses.get(j);
                    CommentEntity comment = batch.get(j);
                    
                    if (response.getCode() == 200) {
                        successCount++;
                        log.debug("Successfully shown comment: {}", comment.getId());
                    } else {
                        log.warn("Failed to show comment {}: HTTP {} - {}", 
                            comment.getId(), response.getCode(), response.getBody());
                    }
                }
                
            } catch (FacebookException e) {
                log.error("Facebook batch API failed for show comments batch: {}", e.getMessage(), e);
                // Tiếp tục xử lý batch tiếp theo
            }
            
            // Delay giữa các batch
            if (endIndex < comments.size()) {
                try {
                    Thread.sleep(1000); // 1000ms delay giữa các batch
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        
        return successCount;
    }

    /**
     * Cập nhật flag allCommentsHidden cho post
     */
    private void updatePostAllCommentsHiddenFlag(String postId, boolean allCommentsHidden) {
        try {
            FbPostEntity post = postRepo.findById(postId).orElse(null);
            if (post != null) {
                post.setAllCommentsHidden(allCommentsHidden);
                postRepo.save(post);
                log.info("Updated post {} allCommentsHidden flag to: {}", postId, allCommentsHidden);
            } else {
                log.warn("Post not found with id: {}", postId);
            }
        } catch (Exception e) {
            log.error("Error updating post allCommentsHidden flag for postId {}: {}", postId, e.getMessage(), e);
        }
    }

}
