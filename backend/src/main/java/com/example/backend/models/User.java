package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;  // 用户唯一标识

    @Column(nullable = false, unique = true, length = 50)
    private String username;  // 用户名（唯一）

    @Column(nullable = false, length = 255)
    private String password;  // 密码（应使用加密存储）

    @Column(nullable = false, unique = true, length = 100)
    private String email;  // 邮箱（唯一）

    @Column(length = 20)
    private String phoneNumber;  // 电话号码

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;  // 用户角色（普通用户或管理员）

    @Column(length = 255)
    private String avatarUrl;  // 用户头像链接

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // 创建时间

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();  // 更新时间

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Blog> blogs;  // 用户发表的博客

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference  // 管理的部分
    private Set<Guide> guides;  // 用户发布的攻略

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<HotelBooking> hotelBookings;  // 用户的酒店预订记录

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<FoodOrder> foodOrders;  // 用户的美食订单记录

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private Set<UserFollow> following;  // 用户关注的其他用户

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private Set<PrivateMessage> sentMessages;  // 用户发送的私信

    @Column(length = 36, unique = true)
    private String resetToken;  // 密码重置令牌
    // getters and setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

