package com.example.facebookinteration.repository;

import com.example.facebookinteration.entity.UserFacebook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFacebookRepo extends JpaRepository<UserFacebook, String> {
    List<UserFacebook> findByUserAppId(Long userAppId);
}
