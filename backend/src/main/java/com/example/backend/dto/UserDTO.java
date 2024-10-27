package com.example.backend.dto;

import com.example.backend.models.Role;

public class UserDTO {
    private Long userId;
    private String username;
    private String email;
    private String phoneNumber;
    private Role role;
    private String avatarUrl;

    // 构造函数
    public UserDTO(Long userId, String username, String email, String phoneNumber, Role role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    // Getter 和 Setter 方法

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
