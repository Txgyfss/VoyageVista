// Favorite.java
package com.example.backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorites")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long itemId;

    @Column(nullable = false)
    private String itemType;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    // 默认构造函数
    public Favorite() {}
    @Transient  // 用于存储额外的信息，但不保存到数据库中
    private Object itemDetails;
    // 全参数构造函数
    public Favorite(Long userId, Long itemId, String itemType, LocalDateTime createdAt) {
        this.userId = userId;
        this.itemId = itemId;
        this.itemType = itemType;
        this.createdAt = createdAt;
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public Object getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(Object itemDetails) {
        this.itemDetails = itemDetails;
    }
    // toString 方法
    @Override
    public String toString() {
        return "Favorite{" +
                "id=" + id +
                ", userId=" + userId +
                ", itemId=" + itemId +
                ", itemType='" + itemType + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
    // Getters and Setters
}
