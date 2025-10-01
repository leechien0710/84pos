package com.example.facebookinteration.service.impl;

import com.example.facebookinteration.constant.Constant;
import com.example.facebookinteration.constant.exception.CustomException;
import com.example.facebookinteration.entity.PageEntity;
import com.example.facebookinteration.repository.PageRepository;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ConversationsService {
	@Autowired
	private PageRepository pageRepository;

	@Autowired
	private RestTemplate restTemplate;


	@Autowired
	private WebClient.Builder webClientBuilder;


	public Object getFacebookConversations(String pageId, int limit, int offset) {
		try {
			PageEntity pageEntity = pageRepository.findByPageId(pageId)
					.orElseThrow(() -> new IllegalArgumentException("Page access token not found for pageId: " + pageId));

			// Tạo URL cURL
			String url = UriComponentsBuilder.fromHttpUrl(Constant.FACE_BASE_URL + pageId + "/conversations")
					.queryParam("fields", "is_subscribed,can_reply,snippet,senders,unread_count,updated_time")  // Sửa trường fields
					.queryParam("limit", limit)  // Thêm tham số limit
					.queryParam("offset", offset)  // Thêm tham số offset
					.queryParam("access_token", pageEntity.getPageAccessToken())  // Access token
					.build()
					.toUriString();

			// Gọi API và trả về kết quả
			ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
			return response.getBody();
		} catch (Exception e) {
			log.error("getFacebookConversations error: {}", e.getMessage(), e);
			throw new CustomException(400, e.getMessage());
		}
	}

	public Object getFacebookMessages(String conversationId, String pageId, int limit, int offset) {
		try {
			PageEntity pageEntity = pageRepository.findByPageId(pageId)
					.orElseThrow(() -> new IllegalArgumentException("Page access token not found for pageId: " + pageId));

			String url = Constant.FACE_BASE_URL + conversationId;

			// Build fields với encode đúng
			String fields = String.format(
					"messages.limit(%d).offset(%d){message,created_time,from,id,sticker,attachments{id,generic_template,image_data,mime_type,name,size,video_data,file_url},shares{description,id,link,name,template}}",
					limit, offset
			);

			String encodedFields = URLEncoder.encode(fields, StandardCharsets.UTF_8);
			String encodedToken = URLEncoder.encode(pageEntity.getPageAccessToken(), StandardCharsets.UTF_8);

			String fullUrl = String.format("%s?fields=%s&access_token=%s", url, encodedFields, encodedToken);

			Mono<Object> response = webClientBuilder.build()
					.get()
					.uri(URI.create(fullUrl))
					.retrieve()
					.bodyToMono(Object.class);

			return response.block();
		} catch (Exception e) {
			log.error("getFacebookMessages error: {}", e.getMessage(), e);
			throw new CustomException(400, e.getMessage());
		}
	}
	
}
