package com.example.facebookinteration.service.impl;

import com.example.facebookinteration.service.VariableResolver;
import com.restfb.types.webhook.FeedCommentValue;
import org.springframework.stereotype.Component;

@Component("ten_khach")
public class TenKhachResolver implements VariableResolver {
    @Override
    public String resolve(FeedCommentValue comment, String pageId) {
        return (comment.getFrom() != null && comment.getFrom().getName() != null)
            ? comment.getFrom().getName() : "";
    }
} 