package com.example.facebookinteration.repository;

import com.example.facebookinteration.entity.LiveVideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiveVideoRepo extends JpaRepository<LiveVideoEntity, String> {
    LiveVideoEntity findByLiveVideoId(String liveVideoId);
}
