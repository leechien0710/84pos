package com.example.facebookinteration.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Phone") // Ánh xạ với bảng "Phone"
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng giá trị id
    @Column(name = "id") 
    private Long id;

    @Column(name = "sender_id") 
    private String senderId;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;
}

