package com.example.businessservice.repository;

import com.example.businessservice.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    // Các phương thức tùy chỉnh có thể được thêm ở đây nếu cần
    Message findFirstByConversationIdOrderByCreatedTimeDesc(String conversationId);
    Page<Message> findByConversationIdOrderByCreatedTimeDesc (String conversationId , Pageable pageable);
}
