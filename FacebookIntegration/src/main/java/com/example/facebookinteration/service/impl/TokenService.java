package com.example.facebookinteration.service.impl;

import com.example.facebookinteration.constant.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class TokenService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String DEBUG_TOKEN_URL = "https://graph.facebook.com/debug_token";

    public Long debugAccessToken(String accessToken) {
        try {
            // Tạo URL để gọi API debug_token
            String debugTokenUrl = UriComponentsBuilder.fromHttpUrl(DEBUG_TOKEN_URL)
                    .queryParam("input_token", accessToken)
                    .queryParam("access_token", accessToken) // App token hoặc user token có quyền để debug
                    .toUriString();

            // Gửi yêu cầu GET đến debug_token endpoint
            ResponseEntity<Map> debugResponse = restTemplate.getForEntity(debugTokenUrl, Map.class);

            // Kiểm tra phản hồi và xử lý thông tin
            Map<String, Object> debugResponseBody = debugResponse.getBody();
            if (debugResponseBody != null) {
                Map<String, Object> data = (Map<String, Object>) debugResponseBody.get("data");
                if (data != null) {
                    Long expiresAt = (Long) data.get("expires_at");
//                    Long dataAccessExpiresAt = (Long) data.get("data_access_expires_at");
                    if (expiresAt == null) {
                        throw new CustomException(403, "Token expiration time not found");
                    }
                    return expiresAt;
//                    if (dataAccessExpiresAt != null) {
//                        throw new CustomException(403, "Data access expiration time not found");
//                    }
                } else {
                    throw new CustomException(403, "No 'data' found in the debug_token response");
                }
            } else {
                throw new CustomException(403, "No response from debug_token API");
            }
        } catch (Exception e) {
            throw new CustomException(500, "Error occurred while calling debug_token API: " + e.getMessage());
        }
    }

    public String getLongLivedToken(String appId, String appSecret, String shortLivedToken) {
        String graphApiVersion = ""; // Đặt version của Graph API
        String baseUrl = "https://graph.facebook.com/" + graphApiVersion + "/oauth/access_token";

        // Tạo URL với các tham số truy vấn
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("grant_type", "fb_exchange_token")
                .queryParam("client_id", appId)
                .queryParam("client_secret", appSecret)
                .queryParam("fb_exchange_token", shortLivedToken)
                .toUriString();

        try {
            // Gửi yêu cầu GET và ánh xạ trực tiếp vào Map
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (response.getBody() != null && response.getBody().containsKey("access_token")) {
                return (String) response.getBody().get("access_token");
            } else {
                throw new CustomException(500, "Failed to retrieve long-lived token.");
            }
        } catch (Exception e) {
            throw new CustomException(500, "Error occurred while calling debug_token API: " + e.getMessage());
        }
    }
}

