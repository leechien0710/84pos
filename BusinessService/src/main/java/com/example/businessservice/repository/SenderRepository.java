package com.example.businessservice.repository;

import com.example.businessservice.entity.Sender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SenderRepository extends JpaRepository<Sender, String> {
    // Các phương thức tùy chỉnh có thể được thêm ở đây nếu cần
    Sender findBySenderId(String senderId);
}
