package com.example.facebookinteration.service.impl;

import com.example.facebookinteration.constant.Constant;
import com.example.facebookinteration.service.FacebookClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class FacebookClientImpl implements FacebookClient {

    private final WebClient webClient;

    public FacebookClientImpl(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl(Constant.FACE_BASE_URL)
                .build();
    }

    @Override
    public <T> Mono<T> callApi(String path, Map<String, String> params, String accessToken, Class<T> responseType) {
        Map<String, String> allParams = new HashMap<>(params);
        allParams.put("access_token", accessToken);

        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(path);
                    allParams.forEach(uriBuilder::queryParam);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(responseType);
    }

    @Override
    public Mono<Object> callApiByUrl(String url) {
        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .encode()
                .build(true)
                .toUri();
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Object.class)
                .doOnError(error -> System.err.println("Error calling next page URL: " + error.getMessage()));
    }

    @Override
    public <T> T callApiSync(String path, Map<String, String> params, String accessToken, Class<T> responseType) {
        log.info("FACEBOOK_API_CALL: path={}, token={}...", path, accessToken.substring(0, Math.min(10, accessToken.length())));
        
        try {
            T result = callApi(path, params, accessToken, responseType)
                    .timeout(java.time.Duration.ofSeconds(30))
                    .doOnSuccess(response -> log.info("FACEBOOK_API_SUCCESS: path={}", path))
                    .doOnError(error -> log.error("FACEBOOK_API_ERROR: path={}, error={}", path, error.getMessage()))
                    .block();
            
            log.info("FACEBOOK_API_RESPONSE: path={}, response={}", path, result != null ? "OK" : "NULL");
            return result;
        } catch (Exception e) {
            log.error("FACEBOOK_API_EXCEPTION: path={}, error={}", path, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Object callApiByUrlSync(String url) {
        return callApiByUrl(url)
                .timeout(java.time.Duration.ofSeconds(30))
                .block();
    }

}
