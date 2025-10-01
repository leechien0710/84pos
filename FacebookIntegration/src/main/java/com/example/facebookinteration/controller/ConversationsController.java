package com.example.facebookinteration.controller;

import com.example.facebookinteration.service.impl.ConversationsService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@Slf4j
public class ConversationsController {
    @Autowired
    private ConversationsService conversationsService;

    @GetMapping("page/{id}/conversations")
    public ResponseEntity<Object> getConversations(@PathVariable("id") String pageId,
                                                    @RequestParam(value = "limit", defaultValue = "20") int limit,
                                                    @RequestParam(value = "offset", defaultValue = "0") int offset) {

        return ResponseEntity.ok(conversationsService.getFacebookConversations(pageId, limit, offset));
    }

    @GetMapping("page/{pageId}/conversation/{conversationId}/messages")
    public ResponseEntity<Object> getFacebookMessages(@PathVariable("pageId") String pageId,
                                                      @PathVariable("conversationId") String conversationId,
                                                      @RequestParam(value = "limit", defaultValue = "20") int limit,
                                                      @RequestParam(value = "offset", defaultValue = "0") int offset) {
        Object messages = conversationsService.getFacebookMessages(conversationId, pageId, limit, offset);
        return ResponseEntity.ok(messages);
    }
}
