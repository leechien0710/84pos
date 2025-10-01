package com.example.facebookinteration.repository;

import com.example.facebookinteration.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    // Bạn có thể định nghĩa các truy vấn tùy chỉnh nếu cần
    List<Address> findBySenderId (String senderId);
}
