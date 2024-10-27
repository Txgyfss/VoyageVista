package com.example.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class GuideDTO {
    private Long guideId;
    private String title;
    private String content;
    private BigDecimal expensePerPerson;
    private Integer days;
    private Boolean isPublic;
    private List<Long> tagIds;
    private List<String> tags;
    private Long userId;  // 新增 userId 字段
    private String username;

    private LocalDateTime createdAt;  // 创建时间

    // Getters and Setters
    public Long getGuideId() {
        return guideId;
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

    public BigDecimal getExpensePerPerson() {
        return expensePerPerson;
    }

    public void setExpensePerPerson(BigDecimal expensePerPerson) {
        this.expensePerPerson = expensePerPerson;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setGuideId(Long guideId) {
        this.guideId=guideId;
    }

    public void setUsername(String username) {
        this.username=username;
    }

    public void setTags(List<String> collect) {
        this.tags=collect;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt=createdAt;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public String  getUsername(){
        return  username;
   }
   public List<String> getTags(){
        return tags;
   }

    @Override
    public String toString() {
        return "GuideDTO{" +
                "guideId=" + guideId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", expensePerPerson=" + expensePerPerson +
                ", days=" + days +
                ", isPublic=" + isPublic +
                ", tags=" + tags +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

}
