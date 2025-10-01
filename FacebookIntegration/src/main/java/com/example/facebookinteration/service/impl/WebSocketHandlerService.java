package com.example.facebookinteration.service.impl;

import com.example.facebookinteration.constant.FunctionUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Service được cải tiến để quản lý các kênh WebSocket dựa trên pageId.
 * Hỗ trợ gửi tin nhắn đến các client đã đăng ký theo dõi một page cụ thể.
 */
@Service
public class WebSocketHandlerService {
    private static final Logger log = LogManager.getLogger(WebSocketHandlerService.class);

    // Bỏ static, mỗi instance của service sẽ quản lý state riêng.
    // Key là pageId, Value là danh sách các session đang theo dõi.
    private final Map<String, List<WebSocketSession>> pageSubscribers = new ConcurrentHashMap<>();

    /**
     * Thêm một session vào kênh theo dõi của một pageId.
     * @param pageId ID của page mà client muốn theo dõi.
     * @param session Session của client.
     */
    public void addSession(String pageId, WebSocketSession session) {
        pageSubscribers.computeIfAbsent(pageId, id -> new CopyOnWriteArrayList<>()).add(session);
        log.info("Session {} added to subscription for Page ID: {}", session.getId(), pageId);
    }

    /**
     * Xóa một session khỏi kênh theo dõi của một pageId.
     * @param pageId ID của page mà client đã theo dõi.
     * @param session Session của client.
     */
    public void removeSession(String pageId, WebSocketSession session) {
        List<WebSocketSession> subscribers = pageSubscribers.get(pageId);
        if (subscribers != null) {
            subscribers.remove(session);
            log.info("Session {} removed from subscription for Page ID: {}", session.getId(), pageId);
            if (subscribers.isEmpty()) {
                pageSubscribers.remove(pageId);
                log.info("No subscribers left for Page ID: {}. Channel removed.", pageId);
            }
        }
    }

    /**
     * Gửi tin nhắn đến tất cả client đang theo dõi một pageId cụ thể.
     * @param pageId ID của page đích.
     * @param topic Chủ đề của tin nhắn (ví dụ: "NEW_COMMENT", "NEW_REACTION").
     * @param data Dữ liệu cần gửi.
     */
    public <T> void sendMessageToPageSubscribers(String pageId, T data, String topic) {
        List<WebSocketSession> subscribers = pageSubscribers.get(pageId);
        if (subscribers == null || subscribers.isEmpty()) {
            log.warn("No subscribers for Page ID: '{}'. Message not sent.", pageId);
            return;
        }

        MessageWrapper<T> messageWrapper = new MessageWrapper<>(topic, data);
        String messagePayload;
        try {
            messagePayload = FunctionUtil.toJson(messageWrapper);
        } catch (Exception e) {
            log.error("Failed to serialize message data for page {}", pageId, e);
            return;
        }

        TextMessage textMessage = new TextMessage(messagePayload);
        log.info("Sending message to {} subscribers of Page ID: {}", subscribers.size(), pageId);

        for (WebSocketSession session : subscribers) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(textMessage);
                }
            } catch (IOException e) {
                log.error("Error sending message to session {}: {}", session.getId(), e.getMessage());
                // Nếu gửi lỗi, có thể xóa session này đi
                subscribers.remove(session);
            }
        }
    }

    /**
     * Lớp nội bộ để bao bọc tin nhắn, giúp frontend dễ dàng xử lý.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class MessageWrapper<T> {
        private String topic;
        private T data;
    }
}