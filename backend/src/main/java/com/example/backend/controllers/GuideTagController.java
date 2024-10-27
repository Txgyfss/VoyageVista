package com.example.backend.controllers;

import com.example.backend.models.GuideTag;
import com.example.backend.repositories.GuideTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@CrossOrigin(origins = "http://localhost:5173")
public class GuideTagController {

    @Autowired
    private GuideTagRepository guideTagRepository;

    // 获取某个类型的标签
    @GetMapping("/{category}")
    public ResponseEntity<List<GuideTag>> getTagsByCategory(@PathVariable String category) {
        List<GuideTag> tags = guideTagRepository.findByCategoryName(category);
        return ResponseEntity.ok(tags);
    }
}
