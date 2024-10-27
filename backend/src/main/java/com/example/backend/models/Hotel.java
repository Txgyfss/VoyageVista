package com.example.backend.models;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hotelId;  // 酒店唯一标识

    @Column(nullable = false, length = 100)
    private String name;  // 酒店名称

    @Column(precision = 2, scale = 1)
    private BigDecimal rating;  // 酒店评级

    @Column(nullable = false, length = 255)
    private String location;  // 酒店位置

    @Column(nullable = false, length = 100)
    private String city;  // 酒店所在城市

    @Column(length = 50)
    private String type;  // 酒店类型

    @Column(length = 255)
    private String imageUrl;  // 酒店图片链接

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
     // 忽略rooms字段，防止序列化时循环引用
    private Set<HotelRoom> rooms;  // 酒店房型

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // 创建时间

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();  // 更新时间

    // getters and setters
    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<HotelRoom> getRooms() {
        return rooms;
    }

    public void setRooms(Set<HotelRoom> rooms) {
        this.rooms = rooms;
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

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
