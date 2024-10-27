package com.example.backend.controllers;
import com.example.backend.BackendApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.backend.models.News;
import com.example.backend.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/news")
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(BackendApplication.class);
    @Autowired
    private NewsRepository newsRepository;

    // 获取所有资讯
    @GetMapping
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    // 根据ID获取单条资讯
    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable int id) {
        Optional<News> news = newsRepository.findById(id);
        return news.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 新增资讯
    @PostMapping
    public ResponseEntity<String> createNews(@RequestBody News news) {
        // 验证标题和内容是否为空
        if (news.getTitle() == null || news.getTitle().isEmpty() ||
                news.getContent() == null || news.getContent().isEmpty()) {
            return ResponseEntity.badRequest().body("资讯的标题和内容不能为空");
        }

        // 设置创建和更新时间
        news.setCreatedAt(LocalDateTime.now());
        news.setUpdatedAt(LocalDateTime.now());

        // 保存资讯
        News savedNews = newsRepository.save(news);
        System.out.println("Pinned status: " + news.isPinned());  // 打印pinned状态
        return ResponseEntity.ok("资讯创建成功，ID: " + savedNews.getNewsId());
    }

    // 更新资讯
    // Update existing news
    @PutMapping("/{id}")
    public ResponseEntity<News> updateNews(@PathVariable int id, @RequestBody News newsDetails) {
        Optional<News> newsOptional = newsRepository.findById(id);
        logger.info("接收到的完整数据: " + newsDetails);
        if (newsOptional.isPresent()) {
            News existingNews = newsOptional.get();
            existingNews.setTitle(newsDetails.getTitle());
            existingNews.setContent(newsDetails.getContent());
            existingNews.setPublishedAt(newsDetails.getPublishedAt());
            existingNews.setCategory(newsDetails.getCategory());
            existingNews.setPinned(newsDetails.isPinned());
            existingNews.setUpdatedAt(LocalDateTime.now());

            newsRepository.save(existingNews);
            logger.info("是否置顶: " + newsDetails.isPinned());
            return ResponseEntity.ok(existingNews);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 删除资讯
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable int id) {
        newsRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // 获取资讯总数
    @GetMapping("/count")
    public long getNewsCount() {
        return newsRepository.count();
    }
}
