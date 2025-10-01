package com.example.facebookinteration.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.facebookinteration.entity.CommentEntity;


public interface FbCommentRepo extends JpaRepository<CommentEntity, String>{
    Page<CommentEntity> findByPostId(String postId, Pageable pageable);
    
    Page<CommentEntity> findByPostIdAndHasPhone(String postId, Boolean hasPhone, Pageable pageable);
    
    /**
     * Tìm comment theo postId và trạng thái ẩn/hiện
     */
    Page<CommentEntity> findByPostIdAndIsHidden(String postId, Boolean isHidden, Pageable pageable);
    
    /**
     * Tìm comment theo postId, hasPhone và trạng thái ẩn/hiện
     */
    Page<CommentEntity> findByPostIdAndHasPhoneAndIsHidden(String postId, Boolean hasPhone, Boolean isHidden, Pageable pageable);
    
    /**
     * Đếm số comment ẩn theo postId
     */
    long countByPostIdAndIsHidden(String postId, Boolean isHidden);
    
    /**
     * Tìm tất cả comment theo postId (trả về List)
     */
    List<CommentEntity> findByPostId(String postId);

}
