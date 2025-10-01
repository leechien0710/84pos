package com.example.facebookinteration.config;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.facebookinteration.service.impl.WebSocketHandlerService;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final MyWebSocketHandler myWebSocketHandler;

    public WebSocketConfig(MyWebSocketHandler myWebSocketHandler) {
        this.myWebSocketHandler = myWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler, "/live-events/{pageId}")
                .setAllowedOrigins("*");
    }

    // Tạo class WebSocketHandler riêng
    @Component
    @RequiredArgsConstructor
    private static class MyWebSocketHandler extends TextWebSocketHandler {
        private static final Logger log = LogManager.getLogger(MyWebSocketHandler.class);
        private final WebSocketHandlerService webSocketHandlerService;

        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            String pageId = getPageIdFromSession(session);
            if (pageId != null) {
                webSocketHandlerService.addSession(pageId, session);
            } else {
                log.warn("Connection established without Page ID. Session {} will not receive targeted messages.",
                        session.getId());
                session.close(CloseStatus.BAD_DATA);
            }
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            String pageId = getPageIdFromSession(session);
            if (pageId != null) {
                webSocketHandlerService.removeSession(pageId, session);
            }
        }

        private String getPageIdFromSession(WebSocketSession session) {
            if (session.getUri() == null)
                return null;
            String path = session.getUri().getPath();
            if (path == null || path.lastIndexOf('/') == -1)
                return null;
            return path.substring(path.lastIndexOf('/') + 1);
        }
    }
}
