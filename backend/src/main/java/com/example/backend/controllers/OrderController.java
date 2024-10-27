package com.example.backend.controllers;

import com.example.backend.dto.OrderResponse;
import com.example.backend.models.FoodOrder;
import com.example.backend.models.HotelBooking;
import com.example.backend.repositories.FoodOrderRepository;
import com.example.backend.repositories.HotelBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    @Autowired
    private FoodOrderRepository foodOrderRepository;

    @Autowired
    private HotelBookingRepository hotelBookingRepository;

    // 根据 userId 获取所有订单
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getUserOrders(@RequestParam Long userId) {
        List<FoodOrder> foodOrders = foodOrderRepository.findByUser_UserId(userId);
        List<HotelBooking> hotelBookings = hotelBookingRepository.findByUser_UserId(userId);

        // 将美食订单和酒店订单都转换为统一的格式返回
        List<OrderResponse> orders = foodOrders.stream().map(foodOrder -> {
            return new OrderResponse(
                    "food",
                    foodOrder.getOrderId(),
                    foodOrder.getFoodPackage().getFood().getName(),
                    foodOrder.getFoodPackage().getFood().getCity(),
                    foodOrder.getOrderDate(),
                    foodOrder.getTotalPrice(),
                    null  // 美食订单没有天数，所以传入 null
            );
        }).collect(Collectors.toList());

        orders.addAll(hotelBookings.stream().map(hotelBooking -> {
            return new OrderResponse(
                    "hotel",
                    hotelBooking.getBookingId(),
                    hotelBooking.getHotel().getName(),
                    hotelBooking.getHotel().getCity(),
                    hotelBooking.getCheckIn().atStartOfDay(),  // 使用 checkIn 作为订单日期
                    hotelBooking.getTotalPrice(),
                    hotelBooking.getNights()  // 酒店订单包含天数
            );
        }).collect(Collectors.toList()));

        return ResponseEntity.ok(orders);
    }
}
