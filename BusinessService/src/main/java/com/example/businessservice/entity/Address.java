package com.example.businessservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Address") // Ánh xạ với bảng "Address"
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
