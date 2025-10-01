package com.example.facebookinteration.service.impl;

import com.example.facebookinteration.service.VariableResolver;
import com.restfb.types.webhook.FeedCommentValue;
import com.example.facebookinteration.repository.SenderRepository;
import com.example.facebookinteration.entity.Sender;
import com.example.facebookinteration.constant.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("ten_shop")
public class TenShopResolver implements VariableResolver {
    private final SenderRepository senderRepository;
    @Autowired
    public TenShopResolver(SenderRepository senderRepository) {
        this.senderRepository = senderRepository;
    }
    @Override
    public String resolve(FeedCommentValue comment, String pageId) {
        return senderRepository.findById(pageId)
            .map(Sender::getName)
            .orElseThrow(() -> new CustomException(404, "Sender (page) not found"));
    }
} 