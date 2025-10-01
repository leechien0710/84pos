package com.example.facebookinteration.service.impl;

import com.example.facebookinteration.service.FeedEventHandler;
import com.restfb.JsonMapper;
import com.restfb.types.webhook.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Nhà máy chịu trách nhiệm quản lý và điều phối các bộ xử lý sự kiện (FeedEventHandler).
 * Lớp này đóng gói logic lựa chọn handler phù hợp cho một loại sự kiện cụ thể.
 */
@Component
@Slf4j
public class FeedEventHandlerFactory {

    /**
     * Map để chuyển đổi từ tên sự kiện (String) sang Class của đối tượng dữ liệu.
     * Thay thế cho khối switch-case, giúp dễ dàng mở rộng.
     */
    private static final Map<String, Class<? extends ChangeValue>> ITEM_TO_CLASS_MAP = Map.of(
            "comment", FeedCommentValue.class
            // "status", FeedStatusValue.class,
            // "photo", FeedPhotoAddValue.class,
            // "video", FeedVideoValue.class,
            // "reaction", FeedReactionValue.class
    );

    /**
     * Registry chứa các instance của handler.
     * Key: Class của sự kiện (ví dụ: FeedCommentValue.class).
     * Value: Bean của handler tương ứng (ví dụ: một instance của CommentFeedHandler).
     */
    private final Map<Class<? extends ChangeValue>, FeedEventHandler> handlerRegistry;

    /**
     * Đối tượng mapper của RestFB để chuyển đổi chuỗi JSON thành Java object.
     */
    private final JsonMapper restfbJsonMapper;

    /**
     * Constructor của nhà máy.
     * Spring sẽ tự động inject một List chứa tất cả các bean có implement FeedEventHandler
     * và inject bean JsonMapper (nếu bạn đã định nghĩa nó ở một file @Configuration).
     *
     * @param handlers Danh sách tất cả các FeedEventHandler beans được Spring tìm thấy.
     * @param restfbJsonMapper Bean JsonMapper để parse dữ liệu.
     */
    public FeedEventHandlerFactory(List<FeedEventHandler> handlers, JsonMapper restfbJsonMapper) {
        this.restfbJsonMapper = restfbJsonMapper;

        // Xây dựng registry từ list các handler
        this.handlerRegistry = handlers.stream()
                .collect(Collectors.toUnmodifiableMap(FeedEventHandler::getSupportedType, Function.identity()));

        log.info("Initialized FeedEventHandlerFactory with {} handlers.", this.handlerRegistry.size());
        this.handlerRegistry.keySet().forEach(key -> log.info("Registered handler for: {}", key.getSimpleName()));
    }

    /**
     * Phương thức xử lý trọn gói một sự kiện từ webhook.
     * Đây là phương thức chính mà các service khác (như WebhookService) sẽ gọi.
     *
     * @param item     Loại sự kiện dưới dạng String (ví dụ: "comment").
     * @param jsonValue Dữ liệu của sự kiện dưới dạng chuỗi JSON.
     * @param pageId   ID của trang đã phát sinh sự kiện.
     */
    public void process(String item, String jsonValue, String pageId) {
        // Bước 1: Tìm Class tương ứng với tên sự kiện `item`.
        Class<? extends ChangeValue> targetClass = ITEM_TO_CLASS_MAP.get(item);
        if (targetClass == null) {
            log.warn("Unsupported feed item type: '{}'. No ChangeValue class mapped.", item);
            return;
        }

        // Bước 2: Tìm handler đã được đăng ký cho Class đó.
        FeedEventHandler handler = handlerRegistry.get(targetClass);
        if (handler == null) {
            log.warn("No handler bean found for feed type: {}. ", targetClass.getSimpleName());
            return;
        }

        try {
            // Bước 3: Parse chuỗi JSON thành đối tượng Java cụ thể.
            ChangeValue specificValue = restfbJsonMapper.toJavaObject(jsonValue, targetClass);

            // Bước 4: Thực thi logic của handler.
            log.debug("Executing handler for {}", targetClass.getSimpleName());
            handler.handle(specificValue, pageId);

        } catch (Exception e) {
            log.error("Error while processing feed item '{}' with handler {}", item, handler.getClass().getSimpleName(), e);
        }
    }
}
