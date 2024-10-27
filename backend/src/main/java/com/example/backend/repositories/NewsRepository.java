package com.example.backend.repositories;

import com.example.backend.models.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {

    // 根据标题查找新闻（模糊查询）
    List<News> findByTitleContaining(String title);



    // 查找在某个日期之后发布的新闻
    List<News> findByPublishedAtAfter(LocalDateTime dateTime);

    // 根据新闻ID更新新闻内容
    @Modifying
    @Transactional
    @Query("UPDATE News n SET n.title = :title, n.content = :content, n.category = :category, n.isPinned = :isPinned WHERE n.newsId = :id")
    void updateNewsById(Integer id, String title, String content, String category, boolean isPinned);

    // 删除新闻
    void deleteByTitle(String title);
    //根据id查找新闻
    Optional<News> findByNewsId(Long id);
    List<News> findByCategory(News.Category category);

    List<News> findByIsPinned(boolean isPinned);
}
