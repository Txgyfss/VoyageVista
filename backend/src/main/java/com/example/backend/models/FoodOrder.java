package com.example.backend.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
@Entity
@Table(name = "food_orders")
public class FoodOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;  // 订单唯一标识

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 订购用户

    @ManyToOne
    @JoinColumn(name = "package_id", nullable = false)
    private FoodPackage foodPackage;  // 订购的套餐

    @Column(nullable = false)
    private LocalDateTime orderDate;  // 订单日期

    @Column(nullable = false)
    private BigDecimal totalPrice;  // 订单总价

    public long getOrderId() {
        return orderId;
    }

    public FoodPackage getFoodPackage() {
        return foodPackage;
    }

    public Object getOrderDate() {
        return orderDate;
    }

    public Object getTotalPrice() {
        return totalPrice;
    }

    public void setUser(User user) {
        this.user=user;
    }

    public void setFoodPackage(FoodPackage foodPackage) {
        this.foodPackage=foodPackage;
    }

    public void setTotalPrice(BigDecimal price) {
        this.totalPrice=price;
    }

    public void setOrderDate(LocalDateTime now) {
        this.orderDate=now;
    }


    // getters and setters
}
