package com.example.facebookinteration.repository;

import com.example.facebookinteration.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepo extends JpaRepository<Post, String> {
}
