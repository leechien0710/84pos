package com.example.businessservice.controller.Message;

import com.example.businessservice.dto.response.GetMessageResponseDTO;
import com.example.businessservice.service.MessageService;
import com.example.businessservice.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;

@RestController
@RequestMapping(Constant.BASE_URL)
public class MessageController {

    @Autowired
    private MessageService messageService;

    // Lấy danh sách tin nhắn của một cuộc trò chuyện
    @GetMapping("/message/{conversationId}")
    public ResponseEntity<Page<GetMessageResponseDTO>> getMessages(
            @PathVariable String conversationId, // Lấy conversationId từ PathVariable
            @RequestParam(defaultValue = "0") int page , @RequestParam(defaultValue = "20") int size
            ) {

        // Gọi service để lấy danh sách tin nhắn với conversationId, page và size
        Page<GetMessageResponseDTO> messages = messageService.getMessages(conversationId, page,size);

        // Trả về kết quả với HTTP status 200 OK
        return ResponseEntity.ok(messages);
    }
}
