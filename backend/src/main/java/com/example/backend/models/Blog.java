package com.example.backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "blogs")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogId;  // 博客唯一标识

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 博客作者

    @Column(nullable = false, length = 255)
    private String title;  // 博客标题

    @Column(columnDefinition = "TEXT")
    private String content;  // 博客内容

    // 使用 @ElementCollection 将标签存储为一个字符串集合
    // 直接存储为字符串数组
    @Column(nullable = true)
    private String tags;  // 标签，保存为逗号分隔的字符串

    @Column(nullable = false)
    private Boolean isPublic = true;  // 博客是否公开

    @Column
    private String imageUrl;  // 图片链接

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // 创建时间

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();  // 更新时间

    // getters and setters

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getImageurl() {
        return imageUrl;
    }

    public void setImageurl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
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

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl() {
        this.imageUrl= imageUrl;
    }
}
