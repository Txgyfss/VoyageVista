package com.example.backend.repositories;

import com.example.backend.models.Hotel;
import com.example.backend.models.HotelRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {

    // 根据酒店名称查找酒店
    Hotel findByName(String name);

    // 根据城市查找酒店
    List<Hotel> findByCity(String city);

    // 查找特定评分以上的酒店
    List<Hotel> findByRatingGreaterThanEqual(BigDecimal rating);

    // 查找特定价格范围内的酒店
    List<Hotel> findByRooms_PriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // 根据酒店ID查找所有房型
    List<HotelRoom> findRoomsByHotelId(Integer hotelId);

    // 更新酒店的名称、城市、评分和价格
    @Modifying
    @Transactional
    @Query("UPDATE Hotel h SET h.name = :name, h.city = :city, h.rating = :rating WHERE h.hotelId = :id")
    void updateHotelById(Integer id, String name, String city, BigDecimal rating);

    // 删除酒店
    void deleteByName(String name);

    // 根据名称、城市和类型进行过滤查询
    @Query("SELECT h FROM Hotel h WHERE "
            + "(?1 IS NULL OR h.name LIKE %?1%) AND "
            + "(?2 IS NULL OR h.city = ?2) AND "
            + "(?3 IS NULL OR h.type = ?3)")
    List<Hotel> findByQueryParams(String name, String city, String type);

    // 更新酒店评分
    @Modifying
    @Transactional
    @Query("UPDATE Hotel a SET a.rating = ?2 WHERE a.hotelId = ?1")
    void updateRatingById(Integer id, BigDecimal rating);
}
