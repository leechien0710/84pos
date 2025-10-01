package com.example.facebookinteration.convert;

import org.springframework.stereotype.Component;

import com.example.facebookinteration.dto.facebookdto.FacebookVideoResponse;
import com.example.facebookinteration.entity.FacebookVideoEntity;

@Component
public class FacebookVideoConvert implements Converter<FacebookVideoResponse.VideoData, FacebookVideoEntity>{

    @Override
    public FacebookVideoEntity convert(FacebookVideoResponse.VideoData source) {
        FacebookVideoEntity entity = new FacebookVideoEntity();
        entity.setId(source.getId());
        entity.setPostId(source.getPostId());
        entity.setEmbedHtml(source.getEmbedHtml());
        entity.setLiveStatus(source.getLiveStatus());
        return entity;
    }
    
    public FacebookVideoEntity convert(FacebookVideoResponse.VideoData source, String pageId) {
        FacebookVideoEntity entity = new FacebookVideoEntity();
        entity.setId(source.getId());
        entity.setEmbedHtml(source.getEmbedHtml());
        entity.setLiveStatus(source.getLiveStatus());
        if (source.getLiveStatus() == null) {
            entity.setPostId(ConvertPostIdDtoToPostIdEntity(pageId, source.getPostId()));
        } else {
            entity.setPostId(ConvertPostIdDtoToPostIdEntity(pageId, source.getId()));
        }

        return entity;
    }

    private String ConvertPostIdDtoToPostIdEntity(String pageId, String postIdDto) {
        return pageId + "_" + postIdDto;
    }
}
