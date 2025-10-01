package com.example.facebookinteration.convert;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.facebookinteration.dto.facebookdto.PostSync;
import com.example.facebookinteration.entity.FbPostEntity;
import com.example.facebookinteration.utils.DataUtils;
import com.restfb.types.Post;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FacebookPostDtoToEntityConverter implements Converter<PostSync.Post, FbPostEntity> {
    private static final String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";

    @Override
    public FbPostEntity convert(PostSync.Post post) {
        FbPostEntity entity = new FbPostEntity();
        entity.setId(post.getId());
        entity.setMessage(post.getMessage());
        entity.setFullPictureUrl(post.getFull_picture());
        entity.setStatusType(post.getStatus_type());
        entity.setCreatedTime(DataUtils.parseToLocalDateTime(post.getCreated_time(),DATETIME_PATTERN));
        entity.setUpdatedTime(DataUtils.parseToLocalDateTime(post.getUpdated_time(),DATETIME_PATTERN));
        entity.setIsPublished(post.getIs_published());
        // try {
        //     entity.setAttachments(DataUtils.objectToJson(post.getAttachments()));
        // } catch (JsonProcessingException e) {
        //     log.error("Failed to convert attachments to JSON for postId={} in FbPostEntity converter. Attachments: {}",
        //             post.getId(), post.getAttachments(), e);
        //     throw new CustomException(400, "Cannot convert attachments for postId=" + post.getId());
        // }
        if (post.getComments() != null && post.getComments().getSummary() != null) {
            entity.setCommentCount(post.getComments().getSummary().getTotal_count());
        }

        if (post.getLikes() != null && post.getLikes().getSummary() != null) {
            entity.setLikeCount(post.getLikes().getSummary().getTotal_count());
        }

        return entity;
    }

    public FbPostEntity convertFromRestFbPost(Post fbPost) {
        FbPostEntity entity = new FbPostEntity();

        entity.setId(fbPost.getId());
        entity.setMessage(fbPost.getMessage());
        entity.setFullPictureUrl(fbPost.getFullPicture());

        // Chuyển đổi từ Date -> LocalDateTime
        if (fbPost.getCreatedTime() != null) {
            entity.setCreatedTime(LocalDateTime.ofInstant(fbPost.getCreatedTime().toInstant(), ZoneOffset.UTC));
        }

        if (fbPost.getUpdatedTime() != null) {
            entity.setUpdatedTime(LocalDateTime.ofInstant(fbPost.getUpdatedTime().toInstant(), ZoneOffset.UTC));
        }

        // Lấy tổng số like và comment nếu có summary
        Long likeCount = fbPost.getLikes().getTotalCount();

        Long commentCount = fbPost.getComments().getTotalCount();

        entity.setLikeCount(likeCount);
        entity.setCommentCount(commentCount);

        entity.setStatusType(fbPost.getStatusType());
        entity.setIsPublished(fbPost.getIsPublished());

        // Trích xuất pageId từ postId nếu có dạng {pageId}_{postId}
        if (fbPost.getId() != null && fbPost.getId().contains("_")) {
            String pageId = fbPost.getId().split("_")[0];
            entity.setPageId(pageId);
        }

        return entity;
    }
}
