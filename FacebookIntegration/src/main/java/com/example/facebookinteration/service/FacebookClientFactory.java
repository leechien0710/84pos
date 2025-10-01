package com.example.facebookinteration.service;

import com.restfb.FacebookClient;

public interface FacebookClientFactory {
    FacebookClient create(String accessToken);
} 