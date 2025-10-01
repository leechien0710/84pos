package com.example.facebookinteration.service;

import com.restfb.types.webhook.ChangeValue;

/**
 * Interface chung cho tất cả các bộ xử lý sự kiện từ Facebook Feed.
 */
public interface FeedEventHandler {

    /**
     * Xử lý logic cho sự kiện.
     * @param changeValue Đối tượng chứa dữ liệu của sự kiện (ví dụ: FeedCommentValue).
     * @param pageId ID của trang đã phát sinh sự kiện.
     */
    void handle(ChangeValue changeValue, String pageId);

    /**
     * Trả về lớp Class của sự kiện mà handler này hỗ trợ.
     * Ví dụ: FeedCommentValue.class
     * @return Class của ChangeValue được hỗ trợ.
     */
    Class<? extends ChangeValue> getSupportedType();
}
