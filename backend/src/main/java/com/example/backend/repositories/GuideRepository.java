package com.example.backend.repositories;

import com.example.backend.models.Guide;
import com.example.backend.models.GuideTagMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Long> {

    // 根据标题查找攻略（模糊查询）
    List<Guide> findByTitleContaining(String title);


    // 查找特定评分以上的攻略
    List<Guide> findByRatingGreaterThanEqual(BigDecimal rating);
    // Find guides by user
    List<Guide> findByUser_UserId(Integer userId);

    // Search by title, content, or user


    // Filter guides by rating or number of days
    List<Guide> findByRatingGreaterThanEqualAndDaysLessThanEqual(BigDecimal rating, Integer days);

    // Find guides by tag
    @Query("SELECT g FROM Guide g JOIN g.tags t WHERE t.tag.tagId = :tagId")
    List<Guide> findByTagId(@Param("tagId") Integer tagId);


    // 根据攻略ID查找所有标签
    List<GuideTagMap> findTagsByGuideId(Integer guideId);

    // 更新攻略信息
    @Modifying
    @Transactional
    @Query("UPDATE Guide g SET g.title = :title, g.content = :content WHERE g.guideId = :id")
    void updateGuideById(long id, String title, String content);

    // 删除攻略
    void deleteByTitle(String title);

    void deleteByGuideId(Long id);

    Optional<Object> findByGuideId(Long id);

    List<Guide> findByIsPublic(boolean b);

    @Query("SELECT g FROM Guide g JOIN FETCH g.user u LEFT JOIN FETCH g.tags t WHERE g.isPublic = true")
    List<Guide> findByIsPublicWithDetails();
    // 使用 @Query 注解定义自定义查询
    /*
    模糊搜索原理：利用 LIKE 和 % 通配符在数据库字段中查找部分匹配的记录。
    大小写不敏感：通过 LOWER() 函数将字段值和关键字都转换为小写，避免大小写敏感的问题。
    多条件匹配：使用 OR 组合多个字段的匹配条件，确保可以在标题、内容、和用户名中查找关键字。
    LIKE：用于在查询中进行模糊匹配。LIKE 运算符允许你通过指定通配符（如 %）来匹配包含特定字符的字段。
    CONCAT('%', :keyword, '%')：这个函数用于将用户输入的关键字拼接为一个带有通配符的字符串。% 通配符表示任意数量的字符。
    因此，CONCAT('%', :keyword, '%') 会将关键字前后加上 %，生成一个可以匹配任意位置包含该关键字的字符串。
   为了使搜索不区分大小写，查询中使用了 LOWER 函数：

LOWER(g.title)：将 g.title（攻略标题）转换为小写字母。
LOWER(:keyword)：将用户输入的关键词同样转换为小写字母。
    */

    @Query("SELECT g FROM Guide g WHERE LOWER(g.title) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            " OR LOWER(g.content) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            " OR LOWER(g.user.username) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Guide> searchGuides(String keyword);

}
