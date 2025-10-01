package com.example.facebookinteration.repository;

import com.example.facebookinteration.entity.Sender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SenderRepository extends JpaRepository<Sender,String> {
    Sender findBySenderId(String senderId);
}
