package com.example.facebookinteration.service.impl;

import com.example.facebookinteration.service.MessageService;
import com.example.facebookinteration.service.VariableResolver;
import com.restfb.types.webhook.FeedCommentValue;
import org.springframework.stereotype.Service;
import com.example.facebookinteration.constant.exception.CustomException;
import com.example.facebookinteration.repository.PageRepository;
import com.example.facebookinteration.repository.PageTemplateRepository;
import com.example.facebookinteration.repository.SenderRepository;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import com.restfb.Parameter;
import com.restfb.FacebookClient;
import java.util.Collections;
import com.restfb.json.JsonObject;
import com.example.facebookinteration.service.FacebookClientFactory;

@Service
public class MessageServiceImpl implements MessageService {
    private final PageRepository pageRepository;
    private final PageTemplateRepository pageTemplateRepository;
    private final Map<String, VariableResolver> variableResolvers;
    private final FacebookClientFactory facebookClientFactory;

    public MessageServiceImpl(PageRepository pageRepository, PageTemplateRepository pageTemplateRepository, SenderRepository senderRepository, FacebookClientFactory facebookClientFactory, Map<String, VariableResolver> variableResolvers) {
        this.pageRepository = pageRepository;
        this.pageTemplateRepository = pageTemplateRepository;
        this.facebookClientFactory = facebookClientFactory;
        this.variableResolvers = variableResolvers;
    }

    @Override
    public JsonObject sendMessageByTemplate(String pageId, FeedCommentValue feedCommentValue) {
        // 1. Kiểm tra pageId hợp lệ
        var pageOpt = pageRepository.findByPageId(pageId);
        if (pageOpt.isEmpty()) {
            throw new CustomException(404, "Page not found");
        }
        var page = pageOpt.get();

        // 2. Lấy template_content từ DB
        var templateOpt = pageTemplateRepository.findByPageId(pageId);
        if (templateOpt.isEmpty()) {
            throw new CustomException(404, "Template not found");
        }
        String templateContent = templateOpt.get().getTemplateContent();

        // 3. Trích xuất các biến trong template
        Set<String> variableKeys = new HashSet<>();
        Pattern pattern = Pattern.compile("\\{\\{(.+?)\\}\\}");
        Matcher matcher = pattern.matcher(templateContent);
        while (matcher.find()) {
            variableKeys.add(matcher.group(1));
        }

        // 4. Mapping biến động bằng strategy
        Map<String, String> data = new HashMap<>();
        for (String key : variableKeys) {
            VariableResolver resolver = variableResolvers.get(key);
            if (resolver != null) {
                data.put(key, resolver.resolve(feedCommentValue, pageId));
            } else {
                throw new CustomException(400, "Biến '{{" + key + "}}' không được hệ thống hỗ trợ.");
            }
        }

        // 5. Fill dữ liệu vào template_content
        String messageContent = fillTemplate(templateContent, data);

        // 6. Lấy page access token từ DB
        String pageAccessToken = page.getPageAccessToken();
        if (pageAccessToken == null || pageAccessToken.isEmpty()) {
            throw new CustomException(404, "Page access token not found");
        }

        // 7. Gửi tin nhắn đến Facebook Messenger API
        JsonObject fbResponse = sendFacebookMessage(pageAccessToken, feedCommentValue.getFrom().getId(), messageContent);
        return fbResponse;
    }

    @Override
    public JsonObject sendTextMessage(String pageId, String userId, String message) {
        // 1. Kiểm tra pageId hợp lệ
        var pageOpt = pageRepository.findByPageId(pageId);
        if (pageOpt.isEmpty()) {
            throw new CustomException(404, "Page not found");
        }
        var page = pageOpt.get();
        String pageAccessToken = page.getPageAccessToken();
        if (pageAccessToken == null || pageAccessToken.isEmpty()) {
            throw new CustomException(404, "Page access token not found");
        }
        // 2. Gửi tin nhắn text
        return sendFacebookMessage(pageAccessToken, userId, message);
    }

    /**
     * Hàm fill dữ liệu vào template
     */
    private String fillTemplate(String template, Map<String, String> data) {
        Pattern pattern = Pattern.compile("\\{\\{(.+?)\\}\\}");
        Matcher matcher = pattern.matcher(template);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = data.getOrDefault(key, "");
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * Hàm gửi tin nhắn đến Facebook Messenger API
     */
    private JsonObject sendFacebookMessage(String pageAccessToken, String recipientId, String messageContent) {
        try {
            FacebookClient facebookClient = facebookClientFactory.create(pageAccessToken);
            return facebookClient.publish("me/messages", JsonObject.class, Collections.emptyList(),
                Parameter.with("recipient", Map.of("id", recipientId)),
                Parameter.with("message", Map.of("text", messageContent))
            );
        } catch (Exception e) {
            String msg = e.getMessage();
            // Bắt lỗi gửi ngoài 24h (code 10, subcode 2018278)
            if (msg != null && msg.contains("(#10)") && msg.contains("code 10") && msg.contains("subcode 2018278")) {
                throw new CustomException(502, "Facebook API error: Tin nhắn này được gửi ngoài khoảng thời gian cho phép 24h. Xem chính sách: https://developers.facebook.com/docs/messenger-platform/policy-overview");
            }
            throw new CustomException(502, "Facebook API error: " + msg);
        }
    }
} 