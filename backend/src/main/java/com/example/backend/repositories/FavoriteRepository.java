package com.example.backend.repositories;

// FavoriteRepository.java


import com.example.backend.models.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);

}