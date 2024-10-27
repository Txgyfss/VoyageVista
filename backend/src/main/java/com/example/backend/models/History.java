package com.example.backend.models;
// History.java


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long itemId;
    private String itemType;
    @Transient  // 用于存储额外的信息，但不保存到数据库中
    private Object itemDetails;
    private LocalDateTime viewDate;
    // 默认构造函数
    public History() {}

    // 全参数构造函数
    public History(Long userId, Long itemId, String itemType, LocalDateTime viewDate) {
        this.userId = userId;
        this.itemId = itemId;
        this.itemType = itemType;
        this.viewDate = viewDate;
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

    public LocalDateTime getViewDate() {
        return viewDate;
    }

    public void setViewDate(LocalDateTime viewDate) {
        this.viewDate = viewDate;
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
        return "History{" +
                "id=" + id +
                ", userId=" + userId +
                ", itemId=" + itemId +
                ", itemType='" + itemType + '\'' +
                ", viewDate=" + viewDate +
                '}';
    }
    // Getters and Setters
}
