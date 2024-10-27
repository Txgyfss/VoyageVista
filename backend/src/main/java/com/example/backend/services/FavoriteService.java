package com.example.backend.services;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.repositories.HotelRepository;
import com.example.backend.repositories.AttractionRepository;
import com.example.backend.repositories.FavoriteRepository;
import com.example.backend.models.*;
import com.example.backend.repositories.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoriteService {
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private AttractionRepository attractionRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;

    public Favorite addFavorite(Favorite favorite) {
        favorite.setCreatedAt(LocalDateTime.now());
        return favoriteRepository.save(favorite);
    }

    public List<Favorite> getFavoritesByUserId(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        for (Favorite favorite : favorites) {
            // 根据 itemType 查询详情信息
            if (favorite.getItemType().equals("food")) {
                Food food = foodRepository.findById(Math.toIntExact(favorite.getItemId())).orElse(null);
                favorite.setItemDetails(food);
            } else if (favorite.getItemType().equals("hotel")) {
                Hotel hotel = hotelRepository.findById(Math.toIntExact(favorite.getItemId())).orElse(null);
                favorite.setItemDetails(hotel);
            } else if (favorite.getItemType().equals("attraction")) {
                Attraction hotel = attractionRepository.findById(Math.toIntExact(favorite.getItemId())).orElse(null);
                favorite.setItemDetails(hotel);
                // 你可以根据需求添加其他类型的项目
            }
        }
        return favorites;
    }
    public void deleteFavoriteById(Long id) {
        favoriteRepository.deleteById(id);  // 删除根据主键 id 删除
    }
}