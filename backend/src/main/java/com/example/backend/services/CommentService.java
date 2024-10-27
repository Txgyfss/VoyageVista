package com.example.backend.services;

import com.example.backend.controllers.CommentController;
import com.example.backend.models.Comment;
import com.example.backend.repositories.AttractionRepository;
import com.example.backend.repositories.CommentRepository;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.repositories.HotelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AttractionRepository attractionRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private FoodRepository foodRepository;

    // 计算平均评分并更新景点评分
    public void updateAttractionRating(Long attractionId) {
        List<BigDecimal> ratings = commentRepository.findRatingsByEntityTypeAndEntityId("attraction", attractionId);

        // 计算平均分
        if (!ratings.isEmpty()) {
            BigDecimal sum = ratings.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal average = sum.divide(new BigDecimal(ratings.size()), 2, RoundingMode.HALF_UP);

            // 更新景点评分
            attractionRepository.updateRatingById(attractionId.intValue(), average);
        }
    }
    // 计算平均评分并更新酒店评分
    public void updateHotelRating(Long hotelId) {
        logger.info("开始更新酒店 {} 的评分", hotelId);
        List<BigDecimal> ratings = commentRepository.findRatingsByEntityTypeAndEntityId("hotel", hotelId);

        if (!ratings.isEmpty()) {
            BigDecimal sum = ratings.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal average = sum.divide(new BigDecimal(ratings.size()), 2, RoundingMode.HALF_UP);

            // 更新酒店评分
            hotelRepository.updateRatingById(hotelId.intValue(), average);
        }
    }
    // 获取酒店的所有评论
    public List<Comment> getHotelComments(Long hotelId) {
        return commentRepository.findByEntityTypeAndEntityId("hotel", hotelId);
    }
    // 计算平均评分并更新美食评分
    public void updateFoodRating(Long foodId) {
        logger.info("开始更新美食 {} 的评分", foodId);
        List<BigDecimal> ratings = commentRepository.findRatingsByEntityTypeAndEntityId("food", foodId);

        if (!ratings.isEmpty()) {
            BigDecimal sum = ratings.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal average = sum.divide(new BigDecimal(ratings.size()), 2, RoundingMode.HALF_UP);
            logger.info("美食 {} 的评分更新成功，更新为{}", foodId,average);
            // 更新美食评分
            foodRepository.updateRatingById(foodId.intValue(), average);
        }
        else{logger.info("美食 {} 的评分更新失败", foodId);}
    }
    // 获取美食的所有评论
    public List<Comment> getFoodComments(Long foodId) {
        return commentRepository.findByEntityTypeAndEntityId("food", foodId);
    }
}
