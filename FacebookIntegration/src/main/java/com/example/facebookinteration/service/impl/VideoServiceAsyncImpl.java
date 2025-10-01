package com.example.facebookinteration.service.impl;

import com.example.facebookinteration.convert.FacebookVideoConvert;
import com.example.facebookinteration.dto.facebookdto.FacebookVideoResponse;
import com.example.facebookinteration.entity.FacebookVideoEntity;
import com.example.facebookinteration.repository.FbVideoRepo;
import com.example.facebookinteration.service.FacebookClient;
import com.example.facebookinteration.utils.DataUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class VideoServiceAsyncImpl {

    private final FacebookVideoConvert facebookVideoConvert;
    private final FbVideoRepo fbVideoRepo;
    private final FacebookClient facebookClient;


    public Mono<Void> syncVideo(String pageId, String accessToken) {
        return getVideos(pageId, accessToken)
                .flatMap(response -> handleSyncVideo(response.getVideos(), pageId)); 
    }

    public Mono<FacebookVideoResponse> getVideos(String pageId, String accessToken) {
        Map<String, String> params = Map.of(
                "fields", "videos{live_status,post_id,comments{comment_count},embed_html}"
        );

        return facebookClient.callApi(pageId, params, accessToken, FacebookVideoResponse.class)
                .doOnError(e -> log.error("API call failed: {}", e.getMessage(), e));
    }

    public Mono<Void> handleSyncVideo(FacebookVideoResponse.Videos videoSync, String pageId) {
        List<FacebookVideoEntity> entities = videoSync.getData().stream()
                .map(video -> facebookVideoConvert.convert(video, pageId))
                .collect(Collectors.toList());

        return Mono.fromRunnable(() -> fbVideoRepo.saveAll(entities))
                .doOnError(e -> log.error("Failed to save videos to DB: {}", e.getMessage(), e))
                .then(Mono.defer(() -> {
                    String nextPageUrl = videoSync.getPaging().getNext();
                    if (nextPageUrl != null && !nextPageUrl.isEmpty()) {
                        return facebookClient.callApiByUrl(nextPageUrl)
                                .map(response -> DataUtils.convertToTargetType(response, FacebookVideoResponse.Videos.class))
                                .flatMap(nextVideos -> handleSyncVideo(nextVideos, pageId));
                    }
                    return Mono.empty();
                }));
    }
}
