package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "foods")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodId;  // 餐厅或美食唯一标识

    @Column(nullable = false, length = 100)
    private String name;  // 餐厅名称或美食名称

    @Column(precision = 2, scale = 1)
    private BigDecimal rating;  // 餐厅或美食评级

    @Column(nullable = false, length = 255)
    private String location;  // 餐厅位置

    @Column(length = 50)
    private String type;  // 餐厅类型

    @Column(length = 255)
    private String imageUrl;  // 餐厅图片链接

    @Column(nullable = false, length = 100)
    private String city;  // 城市信息

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL)

    private Set<FoodPackage> packages;  // 餐厅套餐

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // 创建时间

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();  // 更新时间

    // getters and setters
    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Set<FoodPackage> getPackages() {
        return packages;
    }

    public void setPackages(Set<FoodPackage> packages) {
        this.packages = packages;
    }

    public void setImageUrl(String s) {
        this.imageUrl=s;
    }
    public String getImageurl(){
        return imageUrl;
    }
}
