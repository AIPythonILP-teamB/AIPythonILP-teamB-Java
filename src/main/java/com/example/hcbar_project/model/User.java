package com.example.hcbar_project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "\"user\"") // PostgreSQL予約語のためダブルクォート必須
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
    private boolean isActive = true;


    // --- Getter and Setter ---

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
        this.isActive = active;
    }

    public void setId(Long id) {
        this.id = id;
    }
}