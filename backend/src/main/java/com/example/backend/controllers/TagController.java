package com.example.backend.controllers;
import com.example.backend.models.GuideTagMap;
import com.example.backend.models.Guide;
import com.example.backend.models.GuideTag;
import com.example.backend.dto.TagDTO;
import com.example.backend.repositories.GuideTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@CrossOrigin(origins = "http://localhost:5173")
public class TagController {

    @Autowired
    private GuideTagRepository guideTagRepository;

    // 根据标签分类获取标签列表
    @GetMapping
    public ResponseEntity<List<TagDTO>> getTagsByCategory(@RequestParam String category) {
        List<GuideTag> tags = guideTagRepository.findByCategoryName(category);

        if (tags.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<TagDTO> tagDTOs = tags.stream()
                .map(tag -> new TagDTO(tag.getTagId(), tag.getName()))
                .toList();

        return ResponseEntity.ok(tagDTOs);
    }
}
