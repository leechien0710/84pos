package com.example.businessservice.repository;

import com.example.businessservice.entity.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageEntityRepository extends JpaRepository<PageEntity, String> {
    // Các phương thức tùy chỉnh có thể được thêm ở đây nếu cần
}
