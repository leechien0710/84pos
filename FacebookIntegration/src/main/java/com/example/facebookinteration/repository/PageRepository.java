package com.example.facebookinteration.repository;

import com.example.facebookinteration.entity.PageEntity;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PageRepository extends JpaRepository<PageEntity, String> {
    @Cacheable
    Optional<PageEntity> findByPageId(String pageId);
    
    List<PageEntity> findByUserId(String userId);

    List<PageEntity> findAllByUserIdAndStatus(String userId, Integer status);
}
