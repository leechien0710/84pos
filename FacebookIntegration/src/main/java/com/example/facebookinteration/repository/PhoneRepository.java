package com.example.facebookinteration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.facebookinteration.entity.Phone;

@Repository
public interface PhoneRepository extends JpaRepository<Phone,Long>{
    List<Phone> findByPhoneNumberIn(List<String> phones);

    List<Phone> findAllByPhoneNumberIn(List<String> phoneNumbers);
    
    Phone findBySenderIdAndPhoneNumber(String senderId, String phoneNumber);
    List<Phone> findBySenderId(String senderId);

} 
