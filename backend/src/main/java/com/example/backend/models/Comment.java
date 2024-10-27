package com.example.backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;  // 评论的唯一标识

    @Column(nullable = false, length = 50)
    private String entityType;  // 被评论的实体类型，例如'attraction'

    @Column(nullable = false)
    private int entityId;  // 被评论的实体的ID，例如景点ID

    @Column(nullable = false)
    private int userId;  // 发表评论的用户ID

    @Column(precision = 2, scale = 1)
    private BigDecimal rating;  // 用户评分，范围为0到5

    @Column(columnDefinition = "TEXT")
    private String commentText;  // 用户评论内容

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // 评论创建时间

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();  // 评论更新时间

    // Getters and setters
    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
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
