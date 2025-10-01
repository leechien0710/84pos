package com.example.facebookinteration.service.impl;

import com.example.facebookinteration.service.FacebookClientFactory;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import org.springframework.stereotype.Component;

@Component
public class DefaultFacebookClientFactory implements FacebookClientFactory {
    @Override
    public FacebookClient create(String accessToken) {
        return new DefaultFacebookClient(accessToken, Version.VERSION_22_0);
    }
} 