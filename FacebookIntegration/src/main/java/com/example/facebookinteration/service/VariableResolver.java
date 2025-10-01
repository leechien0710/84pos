package com.example.facebookinteration.service;

import com.restfb.types.webhook.FeedCommentValue;

public interface VariableResolver {
    String resolve(FeedCommentValue comment, String pageId);
} 