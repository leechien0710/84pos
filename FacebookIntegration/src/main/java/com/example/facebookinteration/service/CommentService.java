package com.example.facebookinteration.service;

public interface CommentService {
    void syncComment(String postId, String accessToken);
    
    /**
     * Ẩn comment theo commentId
     * @param commentId ID của comment cần ẩn
     * @param accessToken Access token của page
     * @return true nếu thành công, false nếu không tìm thấy comment
     */
    boolean hideComment(String commentId, String accessToken);
    
    /**
     * Hiện comment theo commentId
     * @param commentId ID của comment cần hiện
     * @param accessToken Access token của page
     * @return true nếu thành công, false nếu không tìm thấy comment
     */
    boolean showComment(String commentId, String accessToken);
    
    /**
     * Ẩn tất cả comment của một bài post
     * @param postId ID của bài post
     * @param accessToken Access token của page
     * @return số lượng comment đã được ẩn
     */
    int hideAllCommentsByPostId(String postId, String accessToken);
    
    /**
     * Hiện tất cả comment của một bài post
     * @param postId ID của bài post
     * @param accessToken Access token của page
     * @return số lượng comment đã được hiện
     */
    int showAllCommentsByPostId(String postId, String accessToken);

}