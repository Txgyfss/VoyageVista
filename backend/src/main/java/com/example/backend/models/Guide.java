package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "guides")
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guideId;  // 攻略唯一标识

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference  // 被管理的部分
    private User user;  // 攻略作者

    @Column(nullable = false, length = 255)
    private String title;  // 攻略标题

    @Column(columnDefinition = "TEXT")
    private String content;  // 攻略内容

    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL)
    @JsonManagedReference  // 管理的部分
    private Set<GuideTagMap> tags;  // 攻略的标签


    @Column(nullable = false)
    private Integer days;
    @Column(nullable = false)
    private Boolean isPublic ;  // 攻略是否公开
    @Column(precision = 10, scale = 2)
    private BigDecimal expensePerPerson;

    @Column(precision = 2, scale = 1)
    private BigDecimal rating;  // 攻略评分

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // 创建时间

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();  // 更新时间

    // getters and setters
    public Long getGuideId() {
        return guideId;
    }

    public void setGuideId(Long guideId) {
        this.guideId = guideId;
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



    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Set<GuideTagMap> getTags() {
        return tags;
    }

    public void setTags(Set<GuideTagMap> tags) {
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

    public BigDecimal getExpensePerPerson() {
        return expensePerPerson;
    }

    public void setExpensePerPerson(BigDecimal expensePerPerson) {
        this.expensePerPerson=expensePerPerson;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days=days;
    }
}
