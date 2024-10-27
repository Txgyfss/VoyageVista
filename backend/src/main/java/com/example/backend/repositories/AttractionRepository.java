package com.example.backend.repositories;

import java.math.BigDecimal;
import com.example.backend.models.Attraction;
import com.example.backend.models.AttractionTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Integer> {

    // 根据景点名称查找景点
    Attraction findByName(String name);

    // 根据城市查找景点
    List<Attraction> findByCity(String city);

    // 查找特定评分以上的景点
    List<Attraction> findByRatingGreaterThanEqual(BigDecimal rating);

    // 查找适合特定人群的景点
    List<Attraction> findByTargetAudience(String targetAudience);

    // 根据景点ID查找所有票型
    List<AttractionTicket> findTicketsByAttractionId(Integer attractionId);

    // 根据开放状态查找景点
    List<Attraction> findByIsOpen(boolean isOpen);

    // 更新景点的名称、城市和描述
    @Modifying
    @Transactional
    @Query("UPDATE Attraction a SET a.name = ?2, a.city = ?3, a.description = ?4 WHERE a.attractionId = ?1")
    void updateAttractionDetailsById(Integer id, String name, String city, String description);

    // 更新景点评分
    @Modifying
    @Transactional
    @Query("UPDATE Attraction a SET a.rating = ?2 WHERE a.attractionId = ?1")
    void updateRatingById(Integer id, BigDecimal rating);

    // 删除景点
    void deleteByName(String name);
}
