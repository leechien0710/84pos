package com.example.facebookinteration.service.business;

import com.example.facebookinteration.dto.response.GetMessageResponseDTO;
import com.example.facebookinteration.entity.Message;
import com.example.facebookinteration.entity.Sender;
import com.example.facebookinteration.repository.MessageRepository;
import com.example.facebookinteration.repository.SenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("businessMessageService")
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SenderRepository senderRepository;

    // Chỉnh sửa method để nhận tham số là String conversationId, int page, int size
    public Page<GetMessageResponseDTO> getMessages(String conversationId, int page, int size) {
        // Sắp xếp theo thời gian và phân trang
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdDate"))); // Sắp xếp giảm dần theo createdDate

        // Lấy danh sách tin nhắn theo conversationId và phân trang tự động
        Page<Message> messagesPage = messageRepository.findByConversationIdOrderByCreatedTimeDesc(
                conversationId, pageable);

        // Chuyển các Message thành GetMessageResponseDTO
        List<GetMessageResponseDTO> responseList = messagesPage.getContent().stream()
                .map(message -> {
                    // Lấy thông tin Sender từ senderId
                    Sender sender = senderRepository.findBySenderId(message.getSenderId());

                    GetMessageResponseDTO responseDTO = new GetMessageResponseDTO();
                    responseDTO.setSenderId(message.getSenderId());
                    responseDTO.setSenderName(sender != null ? sender.getName() : "Unknown");
                    responseDTO.setSenderAvatar(sender != null ? sender.getAvatar() : "default-avatar.png");
                    responseDTO.setContent(message.getContent());
                    responseDTO.setCreatedDate(message.getCreatedTime());
                    return responseDTO;
                })
                .collect(Collectors.toList());

        // Trả về Page với các responseDTO
        return new PageImpl<>(responseList, pageable, messagesPage.getTotalElements());
    }
}
