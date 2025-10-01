package com.example.businessservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Phone") // Ánh xạ với bảng "Phone"
@Getter
@Setter

public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng giá trị id
    @Column(name = "id") // Tên cột trong bảng
    private Long id;

    @Column(name = "sender_id", nullable = false, unique = true) // Trường không được null, phải duy nhất
    private String senderId;

    @Column(name = "phone_number", nullable = false,columnDefinition = "TEXT") // Trường không được null
    @Pattern(regexp = "^(03|05|07|08|09)[0-9]{8}$", message = "Số điện thoại không hợp lệ. Vui lòng nhập lại.")

    private String phoneNumber;
}
