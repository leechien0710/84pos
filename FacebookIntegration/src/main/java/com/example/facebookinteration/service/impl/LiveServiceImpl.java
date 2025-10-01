package com.example.facebookinteration.service.impl;

import com.example.facebookinteration.constant.Constant;
import com.example.facebookinteration.dto.res.LiveVideoRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class LiveServiceImpl {
    @Autowired
    private RestTemplate restTemplate;

    public LiveVideoRes getFacebookLiveVideo(String liveVideoId, String token) {
        String url = UriComponentsBuilder.fromHttpUrl(Constant.FACE_BASE_URL + liveVideoId)
                .queryParam("fields", "status,embed_html,video")
                .queryParam("access_token", token)
                .toUriString();

        ResponseEntity<LiveVideoRes> response = restTemplate.getForEntity(url, LiveVideoRes.class);
        return response.getBody();
    }

}
