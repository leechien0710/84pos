package com.example.facebookinteration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.facebookinteration.entity.FacebookVideoEntity;

@Repository
public interface FbVideoRepo extends JpaRepository<FacebookVideoEntity,String>{
    
}
