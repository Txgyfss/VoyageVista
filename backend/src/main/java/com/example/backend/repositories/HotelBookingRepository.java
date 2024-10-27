package com.example.backend.repositories;

import com.example.backend.models.HotelBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface HotelBookingRepository extends JpaRepository<HotelBooking, Integer> {
    // 查找用户的所有酒店预订记录
    List<HotelBooking> findByUser_UserId(Long userId);  // 使用 User 实体的 userId 字段


}

