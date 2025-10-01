package com.example.facebookinteration.repository;

import com.example.facebookinteration.entity.FbPostEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FbPostRepo extends JpaRepository<FbPostEntity, String> {
        Page<FbPostEntity> findAllByPageIdAndIsPublishedTrue(String pageId, Pageable pageable);

        @Query("SELECT fp FROM FbPostEntity fp JOIN FacebookVideoEntity fv ON fp.id = fv.postId " +
                        "WHERE fv.liveStatus = :liveStatus AND fp.pageId = :pageId AND fp.isPublished = true")
        Page<FbPostEntity> findPostsWithVideosByLiveStatusAndPageId(@Param("liveStatus") String liveStatus,
                        @Param("pageId") String pageId,
                        Pageable pageable);

        Page<FbPostEntity> findAllByPageIdAndStatusTypeAndIsPublishedTrue(String pageId, String statusType,
                        Pageable pageable);

}
