package com.example.businessservice.repository;

import com.example.businessservice.entity.UserFacebook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFacebookRepository extends JpaRepository<UserFacebook, Long> {
    // Tìm kiếm tất cả người dùng theo userAppId
    List<UserFacebook> findByUserAppId(Long userAppId); // Trả về danh sách người dùng theo userAppId
}
