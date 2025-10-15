package com.example.facebookinteration.service.impl;

import com.example.facebookinteration.constant.enums.PageStatus;
import com.example.facebookinteration.dto.res.PageRes;
import com.example.facebookinteration.entity.PageEntity;
import com.example.facebookinteration.entity.Sender;
import com.example.facebookinteration.repository.PageRepository;
import com.example.facebookinteration.repository.SenderRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PageService {
    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private SenderRepository senderRepository;

    public List<PageRes> getByUser(String userId) {
        return getPages(userId, null); // Không lọc theo status
    }

    public List<PageRes> getByUserAndStatus(String userId) {
        return getPages(userId, PageStatus.ACTIVE.getValue()); // Lọc theo status
    }

    private List<PageRes> getPages(String userId, Integer status) {
        List<PageEntity> pageEntities = (status == null) ? pageRepository.findByUserId(userId)
                : pageRepository.findAllByUserIdAndStatus(userId, status);

        // Trả về mảng rỗng thay vì throw exception
        if (pageEntities == null || pageEntities.isEmpty()) {
            log.info("No pages found for user ID: {} with status: {}", userId, status);
            return new ArrayList<>();
        }

        // Lấy tất cả Sender một lần để giảm số lần truy vấn DB
        Map<String, Sender> senderMap = senderRepository.findAllById(
                pageEntities.stream().map(PageEntity::getPageId).collect(Collectors.toList())).stream()
                .collect(Collectors.toMap(Sender::getSenderId, sender -> sender));

        return pageEntities.stream()
                .map(pageEntity -> {
                    Sender sender = senderMap.get(pageEntity.getPageId());
                    if (sender == null) {
                        log.error("No sender found for page ID: {}", pageEntity.getPageId());
                        return PageRes.builder()
                        .pageId(pageEntity.getPageId())
                        .pageName(null) // hoặc null / "" tùy nhu cầu
                        .pageAvatarUrl(null) // hoặc ảnh mặc định
                        .status(pageEntity.getStatus()) // Thêm status từ PageEntity
                        .build();
                    }

                    return PageRes.builder()
                            .pageId(pageEntity.getPageId())
                            .pageName(sender.getName())
                            .pageAvatarUrl(sender.getAvatar())
                            .status(pageEntity.getStatus()) // Thêm status từ PageEntity
                            .build();
                })
                .collect(Collectors.toList());
    }

    public void updatePageStatus(List<String> pageIds, Integer status){
        List<PageEntity> pageEntities = pageRepository.findAllById(pageIds);
        for (PageEntity entity: pageEntities){
            entity.setStatus(status);
        }
        pageRepository.saveAll(pageEntities);
    }

}