package com.example.facebookinteration.service;

import com.restfb.json.JsonObject;
import com.restfb.types.webhook.FeedCommentValue;

public interface MessageService {
    /**
     * Gửi tin nhắn Facebook động theo template cho một comment.
     * @param pageId ID của page
     * @param feedCommentValue Thông tin comment
     * @return true nếu gửi thành công, false nếu thất bại
     */
    JsonObject sendMessageByTemplate(String pageId, FeedCommentValue feedCommentValue);
    com.restfb.json.JsonObject sendTextMessage(String pageId, String userId, String message);
} 