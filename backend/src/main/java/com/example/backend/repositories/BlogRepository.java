package com.example.backend.repositories;

import com.example.backend.models.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {

    // 根据用户ID查找用户发布的博客
    List<Blog> findByUser_UserId(Long userId);

    // 根据标题查找博客（模糊查询）
    List<Blog> findByTitleContaining(String title);


    // 查找特定发布状态的博客
    List<Blog> findByIsPublic(boolean isPublic);

    // 自定义查询，更新博客信息
    @Modifying
    @Transactional
    @Query("UPDATE Blog b SET b.title = ?2, b.content = ?3, b.isPublic = ?4 WHERE b.blogId = ?1")
    void updateBlogById(Integer id, String title, String content, boolean isPublic);

    // 删除博客
    void deleteByTitle(String title);
}
