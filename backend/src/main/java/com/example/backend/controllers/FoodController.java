package com.example.backend.controllers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.backend.models.Attraction;
import com.example.backend.models.Food;
import com.example.backend.models.FoodPackage;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.repositories.FoodPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/foods")
public class FoodController {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodPackageRepository foodPackageRepository;

    // Upload directory for food images
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/image/food/";

    // Create a new food item with image upload
    @PostMapping(consumes = "multipart/form-data")
    public Food createFood(@RequestParam("name") String name,
                           @RequestParam("location") String location,
                           @RequestParam("city") String city,
                           @RequestParam("type") String type,
                           @RequestParam("image") MultipartFile imageFile) throws IOException {

        Food food = new Food();
        food.setName(name);
        food.setLocation(location);
        food.setCity(city);
        food.setType(type);

        // Handle image upload
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();  // Avoid conflicts
            String imagePath = UPLOAD_DIR + fileName;
            Path path = Paths.get(imagePath);
            Files.createDirectories(path.getParent());
            Files.write(path, imageFile.getBytes());
            food.setImageUrl("/image/food/" + fileName);  // Relative path for frontend access
        }

        return foodRepository.save(food);
    }
    // 确保前端提交后，评分更新成功
    private static final Logger logger = LoggerFactory.getLogger(FoodController.class);

    // 更新美食评分
    @PutMapping("/{id}/rating")
    public ResponseEntity<Food> updateFoodRating(@PathVariable int id, @RequestBody BigDecimal newRating) {
        Optional<Food> food = foodRepository.findById(id);
        if (food.isPresent()) {
            logger.info("更新前的评分: {}", food.get().getRating());  // 使用日志输出原来的评分
            logger.info("新评分: {}", newRating);  // 使用日志输出新的评分

            food.get().setRating(newRating);  // 更新评分
            Food updatedFood = foodRepository.save(food.get());

            logger.info("更新后的评分: {}", updatedFood.getRating());  // 使用日志输出更新后的评分
            return ResponseEntity.ok(updatedFood);
        } else {
            logger.warn("未找到 id 为 {} 的美食", id);  // 记录未找到美食的警告
            return ResponseEntity.notFound().build();
        }
    }

    // Get a single food item by ID
    @GetMapping("/{id}")
    public ResponseEntity<Food> getFoodById(@PathVariable int id) {
        Optional<Food> food = foodRepository.findById(id);
        return food.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get a list of food items, with optional query parameters
    @GetMapping
    public List<Food> getFoods(@RequestParam(required = false) String name,
                               @RequestParam(required = false) String city,
                               @RequestParam(required = false) String type) {
        if (name != null || city != null || type != null) {
            return foodRepository.findByQueryParams(name, city, type);  // Implement custom query
        } else {
            return foodRepository.findAll();
        }
    }

    // Update a food item, including image update
    @PutMapping("/{id}")
    public ResponseEntity<Food> updateFood(@PathVariable int id,
                                           @RequestParam("name") String name,
                                           @RequestParam("location") String location,
                                           @RequestParam("city") String city,
                                           @RequestParam("type") String type,
                                           @RequestParam(value = "image", required = false) MultipartFile imageFile) throws IOException {
        Optional<Food> existingFood = foodRepository.findById(id);
        if (existingFood.isPresent()) {
            Food food = existingFood.get();
            food.setName(name);
            food.setLocation(location);
            food.setCity(city);
            food.setType(type);

            // Handle image update
            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                String imagePath = UPLOAD_DIR + fileName;
                Path path = Paths.get(imagePath);
                Files.createDirectories(path.getParent());
                Files.write(path, imageFile.getBytes());
                food.setImageUrl("/image/food/" + fileName);  // Update relative path for frontend access
            }

            foodRepository.save(food);
            return ResponseEntity.ok(food);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a food item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable int id) {
        if (foodRepository.existsById(id)) {
            foodRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get all food packages for a specific food item
    @GetMapping("/{id}/packages")
    public List<FoodPackage> getPackagesByFoodId(@PathVariable Long id) {
        return foodPackageRepository.findByFood_FoodId(id);
    }

    // Add a package to a food item
    @PostMapping("/{id}/packages")
    public FoodPackage addPackageToFood(@PathVariable int id, @RequestBody FoodPackage foodPackage) {
        Optional<Food> food = foodRepository.findById(id);
        if (food.isPresent()) {
            foodPackage.setFood(food.get());
            return foodPackageRepository.save(foodPackage);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "餐厅或美食不存在");
        }
    }

    // Update a food package
    @PutMapping("/packages/{packageId}")
    public ResponseEntity<FoodPackage> updatePackage(@PathVariable Long packageId, @RequestBody FoodPackage updatedPackage) {
        Optional<FoodPackage> existingPackage = foodPackageRepository.findById(packageId);
        if (existingPackage.isPresent()) {
            FoodPackage foodPackage = existingPackage.get();
            foodPackage.setPackageName(updatedPackage.getPackageName());
            foodPackage.setPrice(updatedPackage.getPrice());
            foodPackage.setDescription(updatedPackage.getDescription());
            foodPackageRepository.save(foodPackage);
            return ResponseEntity.ok(foodPackage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a food package
    @DeleteMapping("/packages/{packageId}")
    public ResponseEntity<Void> deletePackage(@PathVariable Long packageId) {
        if (foodPackageRepository.existsById(packageId)) {
            foodPackageRepository.deleteById(packageId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
