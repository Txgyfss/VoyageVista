package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import jakarta.persistence.Entity;
@Entity
@Table(name = "food_packages")
public class FoodPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;  // 套餐唯一标识

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    @JsonBackReference  // 防止序列化时无限循环
    private Food food;  // 所属餐厅或美食

    @Column(nullable = false, length = 100)
    private String packageName;  // 套餐名称

    @Column(nullable = false)
    private BigDecimal price;  // 套餐价格

    @Column(length = 255)
    private String description;  // 套餐描述

    // getters and setters
    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
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
