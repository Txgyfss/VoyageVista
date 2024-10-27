package com.example.backend.services;


import com.example.backend.models.Attraction;
import com.example.backend.models.Food;
import com.example.backend.models.History;
import com.example.backend.models.Hotel;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.repositories.HotelRepository;
import com.example.backend.repositories.AttractionRepository;
import com.example.backend.repositories.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoryService {
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private AttractionRepository attractionRepository;
    @Autowired
    private HotelRepository hotelRepository;

    public History recordHistory(History history) {
        history.setViewDate(LocalDateTime.now());
        return historyRepository.save(history);
    }

    public List<History> getHistoryByUserId(Long userId) {
        List<History> histories = historyRepository.findByUserId(userId);
        for (History history : histories) {
            // 根据 itemType 查询详情信息
            if (history.getItemType().equals("food")) {
                Food food = foodRepository.findById(Math.toIntExact(history.getItemId())).orElse(null);
                history.setItemDetails(food);
            } else if (history.getItemType().equals("hotel")) {
                Hotel hotel = hotelRepository.findById(Math.toIntExact(history.getItemId())).orElse(null);
                history.setItemDetails(hotel);
            } else if (history.getItemType().equals("attraction")) {
                Attraction hotel = attractionRepository.findById(Math.toIntExact(history.getItemId())).orElse(null);
                history.setItemDetails(hotel);
                // 你可以根据需求添加其他类型的项目
            }
        }
            return histories;
        }
    }
