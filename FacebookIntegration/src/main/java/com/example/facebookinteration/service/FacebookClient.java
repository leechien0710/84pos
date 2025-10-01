package com.example.facebookinteration.service;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface FacebookClient {
    <T> Mono<T> callApi(String path, Map<String, String> params, String accessToken, Class<T> responseType);
    Mono<Object> callApiByUrl(String url);
    <T> T callApiSync(String path, Map<String, String> params, String accessToken, Class<T> responseType);
    Object callApiByUrlSync(String url);
}
