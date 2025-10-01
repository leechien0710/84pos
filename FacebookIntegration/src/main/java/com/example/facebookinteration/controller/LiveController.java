package com.example.facebookinteration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.facebookinteration.dto.res.LiveVideoRes;
import com.example.facebookinteration.service.impl.LiveServiceImpl;

@RestController
@RequestMapping()
public class LiveController {
    @Autowired
    private LiveServiceImpl liveServiceImpl;
 
    @GetMapping("page/{pageId}/live/{liveId}")
    public ResponseEntity<LiveVideoRes> getLiveDetails(@PathVariable String pageId, @PathVariable String liveId) {
        return ResponseEntity.ok(liveServiceImpl.getFacebookLiveVideo(liveId, pageId));
    }
}