package com.example.facebookinteration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Vô hiệu hóa CSRF cho API
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()) // Cho phép H2 console
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/h2-console/**", "/api/face/ws/**").permitAll()
                        .anyRequest().permitAll() // Tạm thời cho phép tất cả các request khác
                );
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // Spring Security cần được bảo bỏ qua đường dẫn đầy đủ, bao gồm cả context-path
        return (web) -> web.ignoring().requestMatchers("/api/face/ws/**");
    }
}
