package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import jakarta.persistence.Entity;
@Entity
@Table(name = "hotel_rooms")
public class HotelRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;  // 房型唯一标识

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    @JsonBackReference  // 防止序列化时无限循环
    private Hotel hotel;  // 所属酒店

    @Column(nullable = false, length = 100)
    private String roomType;  // 房型名称

    @Column(nullable = false)
    private BigDecimal price;  // 房型价格

    @Column(length = 255)
    private String description;  // 房型描述

    // getters and setters
    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
