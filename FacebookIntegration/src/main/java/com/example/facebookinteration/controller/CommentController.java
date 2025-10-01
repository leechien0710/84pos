package com.example.facebookinteration.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.facebookinteration.entity.CommentEntity;
import com.example.facebookinteration.entity.PageEntity;
import com.example.facebookinteration.repository.FbCommentRepo;
import com.example.facebookinteration.repository.PageRepository;
import com.example.facebookinteration.service.CommentService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Map;

@RestController
@RequestMapping()
@Slf4j
public class CommentController {
    @Autowired
    private FbCommentRepo fbCommentRepo;
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private PageRepository pageRepository;

    @GetMapping("post/{postId}/comments")
    public ResponseEntity<Page<CommentEntity>> getComment(
            @PathVariable("postId") String postId,
            @PageableDefault(size = 20, page = 0) Pageable pageable,
            @RequestParam(value = "hasPhone", required = false) Boolean hasPhone,
            @RequestParam(value = "includeHidden", defaultValue = "false") Boolean includeHidden) {

        Page<CommentEntity> result;

        if (hasPhone != null) {
            result = fbCommentRepo.findByPostIdAndHasPhone(postId, hasPhone, pageable);
        } else {
            result = fbCommentRepo.findByPostId(postId, pageable);
        }

        return ResponseEntity.ok(result);
    }
    
    /**
     * Ẩn comment theo commentId
     */
    @PutMapping("comment/{commentId}/hide")
    public ResponseEntity<Object> hideComment(
            @PathVariable("commentId") String commentId,
            @RequestParam("pageId") String pageId) {
        
        // Lấy access token của page
        PageEntity page = pageRepository.findByPageId(pageId)
            .orElseThrow(() -> new RuntimeException("Page not found"));
        
        commentService.hideComment(commentId, page.getPageAccessToken());
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Comment đã được ẩn thành công"
        ));
    }
    
    /**
     * Hiện comment theo commentId
     */
    @PutMapping("comment/{commentId}/show")
    public ResponseEntity<Object> showComment(
            @PathVariable("commentId") String commentId,
            @RequestParam("pageId") String pageId) {
        
        // Lấy access token của page
        PageEntity page = pageRepository.findByPageId(pageId)
            .orElseThrow(() -> new RuntimeException("Page not found"));
        
        commentService.showComment(commentId, page.getPageAccessToken());
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Comment đã được hiện thành công"
        ));
    }
    
    /**
     * Ẩn tất cả comment của một bài post
     */
    @PutMapping("post/{postId}/comments/hide-all")
    public ResponseEntity<Object> hideAllComments(
            @PathVariable("postId") String postId,
            @RequestParam("pageId") String pageId) {
        
        // Lấy access token của page
        PageEntity page = pageRepository.findByPageId(pageId)
            .orElseThrow(() -> new RuntimeException("Page not found"));
        
        int hiddenCount = commentService.hideAllCommentsByPostId(postId, page.getPageAccessToken());
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Đã ẩn " + hiddenCount + " comment",
            "hiddenCount", hiddenCount,
            "postId", postId
        ));
    }
    
    /**
     * Hiện tất cả comment của một bài post
     */
    @PutMapping("post/{postId}/comments/show-all")
    public ResponseEntity<Object> showAllComments(
            @PathVariable("postId") String postId,
            @RequestParam("pageId") String pageId) {
        
        // Lấy access token của page
        PageEntity page = pageRepository.findByPageId(pageId)
            .orElseThrow(() -> new RuntimeException("Page not found"));
        
        int shownCount = commentService.showAllCommentsByPostId(postId, page.getPageAccessToken());
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Đã hiện " + shownCount + " comment",
            "shownCount", shownCount,
            "postId", postId
        ));
    }

}
