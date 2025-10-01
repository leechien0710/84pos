package com.example.facebookinteration.controller;

import com.example.facebookinteration.constant.enums.PageStatus;
import com.example.facebookinteration.dto.res.PageRes;
import com.example.facebookinteration.service.impl.PageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping()
public class PageController {
    @Autowired
    private PageService pageService;

    @GetMapping("/user/{userId}/pages")
    public ResponseEntity<List<PageRes>> getPage(@PathVariable String userId ) {
        return ResponseEntity.ok(pageService.getByUser(userId));
    }

    @GetMapping("/user/{userId}/pages/active")
    public ResponseEntity<List<PageRes>> getPageActive(@PathVariable String userId ) {
        return ResponseEntity.ok(pageService.getByUserAndStatus(userId));
    } 

    @PostMapping("/pages/active")
    public ResponseEntity<String> activePage(@RequestBody List<String> pageIds){
        pageService.updatePageStatus(pageIds, PageStatus.ACTIVE.getValue());
        return ResponseEntity.ok("success");
    }

    @PostMapping("/pages/inactive")
    public ResponseEntity<String> inActivePage(@RequestBody List<String> pageIds){
        pageService.updatePageStatus(pageIds, PageStatus.INACTIVE.getValue());
        return ResponseEntity.ok("success");
    }
}
