package com.example.businessservice.repository;

import com.example.businessservice.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    // Bạn có thể định nghĩa các truy vấn tùy chỉnh nếu cần
    List<Address> findBySenderId (String senderId);
}
