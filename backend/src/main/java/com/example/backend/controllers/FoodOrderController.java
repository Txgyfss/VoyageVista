package com.example.backend.controllers;
import java.time.LocalDateTime;
import com.example.backend.models.FoodOrder;
import com.example.backend.models.FoodPackage;
import com.example.backend.models.User;
import com.example.backend.repositories.FoodOrderRepository;
import com.example.backend.repositories.FoodPackageRepository;
import com.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/foodOrders")
@CrossOrigin(origins = "http://localhost:5173")
public class FoodOrderController {

    @Autowired
    private FoodOrderRepository foodOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodPackageRepository foodPackageRepository;

    // 创建新的订单
    @PostMapping("/purchase")
    public ResponseEntity<FoodOrder> createFoodOrder(@RequestParam Long userId, @RequestParam Long packageId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<FoodPackage> foodPackage = foodPackageRepository.findById(packageId);

        if (user.isPresent() && foodPackage.isPresent()) {
            FoodOrder foodOrder = new FoodOrder();
            foodOrder.setUser(user.get());
            foodOrder.setFoodPackage(foodPackage.get());
            foodOrder.setOrderDate(LocalDateTime.now());
            foodOrder.setTotalPrice(foodPackage.get().getPrice());

            return ResponseEntity.ok(foodOrderRepository.save(foodOrder));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
