package com.example.backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Table(name = "attractions")
public class Attraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attractionId;  // 景点唯一标识

    @Column(nullable = false, length = 100)
    private String name;  // 景点名称

    @Column(precision = 2, scale = 1)
    private BigDecimal rating;  // 景点评级

    @Column(nullable = false, length = 255)
    private String location;  // 地理位置

    @Column(length = 50)
    private String type;  // 景点类型（如自然景观、文化遗产等）

    @Column(columnDefinition = "TEXT")
    private String description;  // 景点描述



    @Column(length = 255)
    private String imageUrl;  // 景点图片链接

    @Column(nullable = false)
    private boolean isOpen;  // 景点是否开放

    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL)
    private Set<AttractionTicket> tickets;  // 景点的票型

    @Column
    private LocalTime openingStart; // 开门时间
    @Column
    private LocalTime openingEnd; // 关门时间
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // 创建时间

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();  // 更新时间

    @Column(nullable = false)
    private String city;  // 城市信息

    @Column(nullable = false)
    private Integer duration;  // 游玩时长

    @Column(nullable = false)
    private String targetAudience;  // 目标观众

    // getters and setters
    public Long getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(Long attractionId) {
        this.attractionId = attractionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
    public  String getImageUrl(){return imageUrl; }
    public void setImageUrl(String imageUrl){
        this.imageUrl=imageUrl;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getTargetAudience() {
        return targetAudience;
    }

    public void setTargetAudience(String targetAudience) {
        this.targetAudience = targetAudience;
    }

    public Set<AttractionTicket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<AttractionTicket> tickets) {
        this.tickets = tickets;
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

    public void setOpeningStart(LocalTime openingStart) {
        this.openingStart=openingStart;
    }

    public void setOpeningEnd(LocalTime openingEnd) {
        this.openingEnd=openingEnd;
    }


    public LocalTime getOpeningStart() {
        return openingStart;
    }

    public LocalTime getOpeningEnd() {
        return openingEnd;
    }
}
