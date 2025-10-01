package com.example.businessservice.controller.Conversation;

import com.example.businessservice.dto.response.GetConversationResponseDTO;
import com.example.businessservice.service.ConversationService;
import com.example.businessservice.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;

@RestController
@RequestMapping(Constant.BASE_URL)
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    // Lấy danh sách cuộc trò chuyện
    @GetMapping("/conversation/{pageId}")
    public ResponseEntity<Page<GetConversationResponseDTO>> getConversations(
            @PathVariable String pageId, // Lấy pageId từ PathVariable
            @RequestParam(defaultValue = "0") int page , @RequestParam(defaultValue = "20") int size// Tham số page mặc định là 0 nếu không truyền vào
           ) {

        // Truyền vào service với pageId, page và size (size mặc định là 20)
        Page<GetConversationResponseDTO> conversations = conversationService.getConversations(pageId, page, size);

        // Trả về kết quả với HTTP status 200 OK
        return ResponseEntity.ok(conversations);
    }
}
