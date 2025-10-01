package com.example.facebookinteration.event;

import com.restfb.types.webhook.FeedCommentValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Event được publish khi có comment mới từ webhook Facebook
 * Chứa đủ dữ liệu cần thiết để xử lý bất đồng bộ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentEvent {
    
    /**
     * Dữ liệu comment từ webhook Facebook
     */
    private FeedCommentValue commentValue;
    
    /**
     * ID của page Facebook
     */
    private String pageId;
    
    /**
     * Verb của sự kiện (add, edit, delete)
     */
    private String verb;
}
