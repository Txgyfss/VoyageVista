package com.example.backend.controllers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.backend.models.Favorite;
import com.example.backend.services.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    private static final Logger logger = LoggerFactory.getLogger(FavoriteController.class);

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/add")
    public ResponseEntity<?> addFavorite(@RequestBody Favorite favorite) {
        // 使用 logger 打印收到的请求数据
        logger.info("收到的收藏数据: userId={}, itemId={}, itemType={}", favorite.getUserId(), favorite.getItemId(), favorite.getItemType());

        try {
            Favorite savedFavorite = favoriteService.addFavorite(favorite);
            return ResponseEntity.ok(savedFavorite);
        } catch (Exception e) {
            // 打印错误日志
            logger.error("收藏操作失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("收藏操作失败");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Favorite>> getFavoritesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteService.getFavoritesByUserId(userId));
    }
    // 取消收藏
    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long favoriteId) {
        favoriteService.deleteFavoriteById(favoriteId);
        return ResponseEntity.noContent().build(); // 返回204状态码，表示成功但无内容返回
    }
}

