package com.example.lab01.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "jpa_users")
public class JpaUserEntity {

    @Id
    private Long id;

    @Column(name = "username", nullable = false, length = 64)
    private String username;

    @Column(name = "email", nullable = false, length = 128)
    private String email;

    @Column(name = "role", nullable = false, length = 32)
    private String role;

    @Column(name = "password_hash", nullable = false, length = 128)
    private String passwordHash;

    public JpaUserEntity() {
    }

    public JpaUserEntity(Long id, String username, String email, String role, String passwordHash) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.passwordHash = passwordHash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
