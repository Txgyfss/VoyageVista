package com.example.backend.repositories;

import com.example.backend.models.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {
    // 通过用户 ID 查找美食订单
    List<FoodOrder> findByUser_UserId(Long userId);
}
