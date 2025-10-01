package com.example.facebookinteration.service.impl;


import com.example.facebookinteration.convert.FacebookPostDtoToEntityConverter;
import com.example.facebookinteration.dto.facebookdto.PostSync;
import com.example.facebookinteration.entity.FbPostEntity;
import com.example.facebookinteration.repository.FbPostRepo;
import com.example.facebookinteration.service.FacebookClient;
import com.example.facebookinteration.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PostServiceImpl {
    @Autowired
    private FacebookPostDtoToEntityConverter facebookPostDtoToEntityConverter;
    @Autowired
    private FbPostRepo fbPostRepo;
    private final FacebookClient facebookClient;
    public PostServiceImpl(FacebookClient facebookClient) {
        this.facebookClient = facebookClient;
    }

    public void syncPost(String pageId, String accessToken) {
        getPosts(pageId, accessToken)
                .subscribe(
                        res -> handleSyncPost(res.getPosts(), pageId),  
                        error -> log.error("syncPost has error occurred: {}", error.getMessage(), error)
                );
    }

    public Mono<PostSync> getPosts(String pageId, String accessToken) {
        Map<String, String> params = Map.of(
                "fields", "posts{message,id,full_picture,created_time,updated_time,attachments{title},status_type,likes.summary(total_count),comments.summary(total_count)}"
        );

        return facebookClient.callApi(pageId, params, accessToken, PostSync.class)
            .doOnError(e -> log.error("Error during API call or mapping: {}", e.getMessage(), e));
    }

    @Transactional
    public void  handleSyncPost(PostSync.Posts postSync, String pageId){
        List<FbPostEntity> fbPostEntities = new ArrayList<>();
        for (PostSync.Post post : postSync.getData()) {
            FbPostEntity fbPostEntity = facebookPostDtoToEntityConverter.convert(post);
            fbPostEntity.setPageId(pageId);
            fbPostEntities.add(fbPostEntity);
        }
        fbPostRepo.saveAll(fbPostEntities);
        String nextPageUrl = postSync.getPaging().getNext();
        if (nextPageUrl != null && !nextPageUrl.isEmpty()) {
            facebookClient.callApiByUrl(nextPageUrl)
                    .map(response -> DataUtils.convertToTargetType(response, PostSync.Posts.class))
                    .doOnError(e -> log.error("Error fetching next page: {}", e.getMessage(), e))
                    .subscribe(res -> handleSyncPost(res, pageId));
        }
    }
    
}
