package com.example.backend.repositories;

import com.example.backend.models.HotelRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRoomRepository extends JpaRepository<HotelRoom, Long> {

    // 根据酒店ID查找所有房型
    List<HotelRoom> findByHotel_HotelId(Long hotelId);
}
