package com.handcbar.myproject.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "\"user\"")  // PostgreSQL予約語のためダブルクォート必須
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(unique = true)
    private String email;

    private String password;

    private String role;

    @Column(name = "is_active")
    private Boolean isActive = true;
}