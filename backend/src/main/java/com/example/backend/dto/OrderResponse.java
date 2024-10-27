package com.example.backend.dto;

public class OrderResponse {
    private String orderType;  // 美食或酒店订单
    private Long orderId;
    private String name;
    private String city;
    private Object orderDate;
    private Object totalPrice;
    private Integer nights;  // 酒店订单的天数（美食订单没有天数）

    public OrderResponse(String orderType, Long orderId, String name, String city, Object orderDate, Object totalPrice, Integer nights) {
        this.orderType = orderType;
        this.orderId = orderId;
        this.name = name;
        this.city = city;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.nights = nights;
    }

    // Getters and setters
    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Object getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Object orderDate) {
        this.orderDate = orderDate;
    }

    public Object getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Object totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getNights() {
        return nights;
    }

    public void setNights(Integer nights) {
        this.nights = nights;
    }
}
