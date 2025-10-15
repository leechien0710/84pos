package com.example.facebookinteration.repository;

import com.example.facebookinteration.entity.UserFacebook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFacebookRepository extends JpaRepository<UserFacebook, String> {
}
