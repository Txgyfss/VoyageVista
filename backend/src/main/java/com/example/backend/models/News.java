package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsId;  // 资讯唯一标识

    @Column(nullable = false, length = 255)
    private String title;  // 资讯标题
    public enum Category {
        WEATHER, ACTIVITY, OFFER, NOTICE
    }
    @Column(columnDefinition = "TEXT")
    private String content;  // 资讯内容

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // 创建时间

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();  // 更新时间
    @Column(nullable = false)
    private LocalDateTime publishedAt;  // 发布时间
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;  // New category field

    @JsonProperty("pinned")
    @Column(name = "is_pinned", nullable = false)
    private boolean isPinned;



    // getters and setters
    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }


    public void setCreatedAt(LocalDateTime now) {
        this.createdAt=now;
    }

    public void setUpdatedAt(LocalDateTime now) {
        this.updatedAt=now;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public void setPinned(boolean isPinned) {
        this.isPinned = isPinned;
    }
}
