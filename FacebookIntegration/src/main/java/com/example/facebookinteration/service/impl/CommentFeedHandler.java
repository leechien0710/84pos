package com.example.facebookinteration.service.impl;

import com.example.facebookinteration.event.NewCommentEvent;
import com.example.facebookinteration.service.FeedEventHandler;
import com.restfb.types.webhook.ChangeValue;
import com.restfb.types.webhook.FeedCommentValue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentFeedHandler implements FeedEventHandler {
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void handle(ChangeValue changeValue, String pageId) {
        FeedCommentValue commentValue = (FeedCommentValue) changeValue;
        String verb = commentValue.getVerbAsString();
        
        log.info("Facebook Comment: Received webhook event from '{}': {} (verb: {})",
                commentValue.getFrom().getName(), commentValue.getMessage(), verb);
        
        // Chỉ xử lý comment mới (add)
        if ("add".equalsIgnoreCase(verb)) {
            log.info("Publishing NewCommentEvent for async processing...");
            
            // Publish event để xử lý bất đồng bộ
            NewCommentEvent event = new NewCommentEvent(commentValue, pageId, verb);
            eventPublisher.publishEvent(event);
            
            log.info("NewCommentEvent published successfully. Webhook response sent to Facebook.");
        } else {
            log.debug("Ignoring comment event with verb: {}", verb);
        }
    }

    @Override
    public Class<? extends ChangeValue> getSupportedType() {
        return FeedCommentValue.class;
    }
}
