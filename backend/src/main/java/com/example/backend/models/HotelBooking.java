package com.example.backend.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "hotel_bookings")
public class HotelBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;  // 预订唯一标识

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 预订用户

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private HotelRoom room;  // 预订的房间

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false) // Add hotel ID reference
    private Hotel hotel;  // 预订的酒店

    @Column(nullable = false)
    private LocalDate checkIn;  // 入住日期

    @Column(nullable = false)
    private LocalDate checkOut;  // 退房日期

    @Column(nullable = false)
    private BigDecimal totalPrice;  // 总价

    @Column(nullable = false)
    private int nights;  // 预订晚数

    // getters and setters
    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HotelRoom getRoom() {
        return room;
    }

    public void setRoom(HotelRoom room) {
        this.room = room;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkInDate) {
        this.checkIn = checkInDate;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOutDate) {
        this.checkOut = checkOutDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }
}
