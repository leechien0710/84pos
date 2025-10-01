package com.example.facebookinteration.service.business;

import com.example.facebookinteration.dto.response.GetConversationResponseDTO;
import com.example.facebookinteration.entity.Conversation;
import com.example.facebookinteration.entity.Message;
import com.example.facebookinteration.entity.Sender;
import com.example.facebookinteration.repository.ConversationRepository;
import com.example.facebookinteration.repository.MessageRepository;
import com.example.facebookinteration.repository.SenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private SenderRepository senderRepository;

    @Autowired
    private MessageRepository messageRepository;

    public Page<GetConversationResponseDTO> getConversations(String pageId, int page, int size) {
        // Tạo Pageable với size cố định
        Pageable pageable = PageRequest.of(page, size);

        // Lấy danh sách các cuộc trò chuyện từ repository theo pageId
        Page<Conversation> conversationsPage = conversationRepository.findByPageId(pageId, pageable);

        List<GetConversationResponseDTO> responseList = new ArrayList<>();
        for (Conversation conversation : conversationsPage.getContent()) {
            // Lấy thông tin Sender
            Sender sender = senderRepository.findBySenderId(conversation.getCustomerId());

            // Lấy tin nhắn mới nhất từ Message repository
            Message lastMessage = messageRepository.findFirstByConversationIdOrderByCreatedTimeDesc(conversation.getConversationId());

            // Tạo và thiết lập DTO
            GetConversationResponseDTO responseDTO = new GetConversationResponseDTO();
            responseDTO.setConversationId(conversation.getConversationId());

            // Kiểm tra sender có tồn tại không
            if (sender != null) {
                responseDTO.setSenderName(sender.getName());
                responseDTO.setSenderAvatar(sender.getAvatar());
            }

            // Kiểm tra tin nhắn có tồn tại không
            if (lastMessage != null) {
                responseDTO.setFinallyMessage(lastMessage.getContent());
                responseDTO.setMessageCreatedDate(lastMessage.getCreatedTime());
            }

            responseList.add(responseDTO);
        }

        // Trả về một trang chứa các DTO đã được tạo
        return new PageImpl<>(responseList, pageable, conversationsPage.getTotalElements());
    }
}
