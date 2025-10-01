package com.example.facebookinteration.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.restfb.types.webhook.FeedCommentValue;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import com.restfb.json.JsonObject;
import com.example.facebookinteration.dto.SendTextMessageRequest;
import com.example.facebookinteration.dto.response.GetMessageResponseDTO;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping()
public class MessageController {
    private final com.example.facebookinteration.service.MessageService messageService;
    @Qualifier("businessMessageService")
    private final com.example.facebookinteration.service.business.MessageService businessMessageService;
    
    public MessageController(com.example.facebookinteration.service.MessageService messageService, 
                           @Qualifier("businessMessageService") com.example.facebookinteration.service.business.MessageService businessMessageService) {
        this.messageService = messageService;
        this.businessMessageService = businessMessageService;
    }
    @PostMapping("/pages/{pageId}/send-message")
    public ResponseEntity<String> sendMessage(
            @PathVariable String pageId,
            @RequestBody FeedCommentValue feedCommentValue
    ) {
        JsonObject fbResponse = messageService.sendMessageByTemplate(pageId, feedCommentValue);
        return ResponseEntity.ok(fbResponse.toString());
    }

    @PostMapping("/pages/{pageId}/send-text-message")
    public ResponseEntity<String> sendTextMessage(
            @PathVariable String pageId,
            @RequestBody SendTextMessageRequest request
    ) {
        // Gọi service gửi tin nhắn
        JsonObject fbResponse = messageService.sendTextMessage(pageId, request.getUserId(), request.getMessage());
        return ResponseEntity.ok(fbResponse.toString());
    }

    // Business endpoints - Lấy danh sách tin nhắn của một cuộc trò chuyện
    @GetMapping("/message/{conversationId}")
    public ResponseEntity<Page<GetMessageResponseDTO>> getMessages(
            @PathVariable String conversationId, // Lấy conversationId từ PathVariable
            @RequestParam(defaultValue = "0") int page , @RequestParam(defaultValue = "20") int size
            ) {

        // Gọi service để lấy danh sách tin nhắn với conversationId, page và size
        Page<GetMessageResponseDTO> messages = businessMessageService.getMessages(conversationId, page,size);

        // Trả về kết quả với HTTP status 200 OK
        return ResponseEntity.ok(messages);
    }
} 