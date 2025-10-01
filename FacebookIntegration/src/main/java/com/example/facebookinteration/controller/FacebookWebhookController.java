package com.example.facebookinteration.controller;

import com.example.facebookinteration.constant.FunctionUtil;
import com.example.facebookinteration.dto.facewebhook.FacebookWebhookPayload;
import com.example.facebookinteration.service.impl.WebhookService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/webhook")
@Slf4j
public class FacebookWebhookController {
    @Autowired
    private WebhookService webhookService;

    @GetMapping
    public ResponseEntity<String> verifyWebhook(@RequestParam("hub.mode") String mode,
            @RequestParam("hub.challenge") String challenge,
            @RequestParam("hub.verify_token") String verifyToken) {
        String expectedToken = "B19dccn099@"; // token bạn đã đăng ký trong Facebook
        log.info("Received webhook verification request: mode={}, challenge={}, verify_token={}", mode, challenge,
                verifyToken);
        if (mode.equals("subscribe") && verifyToken.equals(expectedToken)) {
            log.info("Webhook verification succeeded. Returning challenge: {}", challenge); // Log thành công
            return ResponseEntity.ok(challenge); // Xác nhận webhook
        } else {
            log.error("Webhook verification failed. Mode or token mismatch."); // Log khi không xác thực được
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Verification failed");
        }
    }

    // Xử lý dữ liệu webhook từ Facebook
    @PostMapping
    public ResponseEntity<Void> handleWebhook(@RequestBody Map<String, Object> payload) {
        log.info("payload webhook: {}", payload);
        try {
            FacebookWebhookPayload facebookWebhookPayload = FunctionUtil.mapToObject(payload,
                    FacebookWebhookPayload.class);
            webhookService.processPayload(facebookWebhookPayload);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Webhook processing failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Map<String, Object> getUserInfo(String senderId, String pageAccessToken) {
        String url = "https://graph.facebook.com/" + senderId + "?fields=name,picture&access_token=" + pageAccessToken;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, Map.class);
    }

    public void sendMessage(String recipientId, String messageText, String pageAccessToken) {
        RestTemplate restTemplate = new RestTemplate();
        // Tạo body cho request
        Map<String, Object> message = new HashMap<>();
        Map<String, String> recipient = new HashMap<>();
        recipient.put("id", recipientId);
        message.put("recipient", recipient);

        Map<String, String> messageContent = new HashMap<>();
        messageContent.put("text", messageText);
        message.put("message", messageContent);

        // Tạo headers cho request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tạo HTTP request
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(message, headers);

        // Gửi POST request tới Facebook Send API
        ResponseEntity<String> response = restTemplate.exchange(
                "https://graph.facebook.com/me/messages?access_token=" + pageAccessToken,
                HttpMethod.POST,
                request,
                String.class);

        // Kiểm tra phản hồi
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Message sent successfully");
        } else {
            System.out.println("Failed to send message: " + response.getBody());
        }
    }
}
