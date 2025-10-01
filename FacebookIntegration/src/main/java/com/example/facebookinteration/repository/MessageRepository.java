package com.example.facebookinteration.repository;

import com.example.facebookinteration.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, String> {
    Page<Message> findByConversationIdOrderByCreatedTimeDesc(String conversationId, Pageable pageable);
    Message findFirstByConversationIdOrderByCreatedTimeDesc(String conversationId);
}
