package com.example.businessservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sender {
    @Id
    private String senderId;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String avatar;
}