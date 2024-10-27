package com.example.backend.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class BookingRequest {

    private int userId;
    private int hotelId; // 添加 hotelId
    private int roomId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkIn;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOut;

    // Getters and Setters
    public long getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getHotelId() { // 添加 getter
        return hotelId;
    }
    @Override
    public String toString() {
        return "BookingRequest{" +
                "hotelId=" + hotelId +
                ", roomId=" + roomId +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                '}';
    }

    public void setHotelId(int hotelId) { // 添加 setter
        this.hotelId = hotelId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }
}
