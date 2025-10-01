package com.example.facebookinteration.controller;

import com.example.facebookinteration.entity.FbPostEntity;
import com.example.facebookinteration.repository.FbPostRepo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping()
public class PostController {
    @Autowired
    private FbPostRepo fbPostRepo;

    @GetMapping("/page/{pageId}/posts")
    public ResponseEntity<Page<FbPostEntity>> getPosts(
            @PathVariable String pageId,
                    @RequestParam(required = false) String type,

            @PageableDefault(page = 0, size = 20) Pageable pageable) {

        Page<FbPostEntity>  posts;
        if (type == null) {
            posts = fbPostRepo.findAllByPageIdAndIsPublishedTrue(pageId, pageable);
        } 
        else if ("added_livetream".equals(type)) {
            posts = fbPostRepo.findPostsWithVideosByLiveStatusAndPageId("VOD", pageId, pageable);
        } 
        else {
            posts = fbPostRepo.findAllByPageIdAndStatusTypeAndIsPublishedTrue(pageId, type, pageable);
        }
        return ResponseEntity.ok(posts);
    }

}
