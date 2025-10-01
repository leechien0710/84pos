package com.example.businessservice.repository;

import com.example.businessservice.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {

    Phone findBySenderIdAndPhoneNumber(String senderId, String phoneNumber);
    List<Phone> findBySenderId (String senderId);
}
