package com.example.facebookinteration.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_address") // Ánh xạ với bảng "user_address"
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng giá trị id
    @Column(name = "id") // Tên cột là "id"
    private Long id;

    @Column(name = "sender_id", nullable = false, unique = true) // Tên cột giữ nguyên là "sender_id"
    private String senderId; // Field đổi thành camelCase: senderId

    @Column(name = "address", nullable = false, columnDefinition = "TEXT") // Tên cột giữ nguyên là "address"
    private String address; // Field đổi thành camelCase: address
}
