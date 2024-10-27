package com.example.backend.controllers;

import com.example.backend.models.Comment;
import com.example.backend.repositories.CommentRepository;
import com.example.backend.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @PostMapping("/add")
    public ResponseEntity<String> addComment(@RequestBody Comment comment) {
        // 确认请求已被触发
        logger.debug("收到新评论: {}", comment);

        // 继续处理评论
        commentRepository.save(comment);

        if ("hotel".equals(comment.getEntityType())) {
            logger.debug("Updating hotel rating for entityId: {}", comment.getEntityId());
            commentService.updateHotelRating((long) comment.getEntityId());
        } else if ("attraction".equals(comment.getEntityType())) {
            logger.debug("Updating attraction rating for entityId: {}", comment.getEntityId());
            commentService.updateAttractionRating((long) comment.getEntityId());
        } else if ("food".equals(comment.getEntityType())) {
            logger.debug("Updating food rating for entityId: {}", comment.getEntityId());
            commentService.updateFoodRating((long) comment.getEntityId());
        } else {
            logger.warn("Unknown entityType: {}", comment.getEntityType());
            return ResponseEntity.badRequest().body("Unknown entity type");
        }

        logger.debug("Comment processed successfully");
        return ResponseEntity.ok("Comment submitted and rating updated");
    }
    // 获取某个实体的所有评论（例如酒店或景点）
    @GetMapping("/{entityType}/{entityId}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable String entityType, @PathVariable Long entityId) {
        List<Comment> comments = commentRepository.findByEntityTypeAndEntityId(entityType, entityId);
        return ResponseEntity.ok(comments);
    }
    // 获取酒店评论的API
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<Comment>> getHotelComments(@PathVariable Long hotelId) {
        List<Comment> comments = commentService.getHotelComments(hotelId);
        return ResponseEntity.ok(comments);
    }
    // 获取美食评论的API
    @GetMapping("/food/{foodId}")
    public ResponseEntity<List<Comment>> getFoodComments(@PathVariable Long foodId) {
        List<Comment> comments = commentService.getFoodComments(foodId);
        return ResponseEntity.ok(comments);
    }
}
