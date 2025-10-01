package com.example.businessservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "User")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Tự động tăng giá trị id
    @Column(name = "id")  // Tên cột giống với tên field
    private Long id;

    @Column(name = "username", nullable = false, unique = true)  // Đảm bảo username là duy nhất
    private String username;

    @Column(name = "password", nullable = false)  // Mật khẩu không được null
    private String password;

    @Column(name = "role", nullable = false)  // Vai trò không được null
    private String role;

    @Column(name = "created_at", nullable = false,columnDefinition = "TIMESTAMP(6)")  // Ngày tạo không được null
    private LocalDateTime created_at;

    @Column(name = "update_at", nullable = false,columnDefinition = "TIMESTAMP(6)")  // Ngày cập nhật không được null
    private LocalDateTime update_at;
}
