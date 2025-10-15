package com.example.facebookinteration.event;

import org.springframework.context.ApplicationEvent;
import java.util.List;

public class PageSyncEvent extends ApplicationEvent {
    private final List<String> pageIds;
    private final String userId;
    private final Long userAppId;

    public PageSyncEvent(Object source, List<String> pageIds, String userId, Long userAppId) {
        super(source);
        this.pageIds = pageIds;
        this.userId = userId;
        this.userAppId = userAppId;
    }

    public List<String> getPageIds() {
        return pageIds;
    }

    public String getUserId() {
        return userId;
    }

    public Long getUserAppId() {
        return userAppId;
    }
}
