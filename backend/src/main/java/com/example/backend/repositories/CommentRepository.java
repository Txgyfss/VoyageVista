package com.example.backend.repositories;

import com.example.backend.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 根据实体类型和实体ID查找评论
    List<Comment> findByEntityTypeAndEntityId(String entityType, Long entityId);

    // 根据用户ID查找评论
    List<Comment> findByUserId(Long userId);
    // 获取特定实体的所有评分
    @Query("SELECT c.rating FROM Comment c WHERE c.entityType = :entityType AND c.entityId = :entityId")
    List<BigDecimal> findRatingsByEntityTypeAndEntityId(String entityType, Long entityId);
}
