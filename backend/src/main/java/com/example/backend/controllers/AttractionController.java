package com.example.backend.controllers;

import com.example.backend.models.Attraction;
import com.example.backend.models.AttractionTicket;
import com.example.backend.models.Comment;

import com.example.backend.repositories.AttractionRepository;
import com.example.backend.repositories.AttractionTicketRepository;
import com.example.backend.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "http://localhost:5173")  // 允许来自 http://localhost:5173 的跨域请求
@RestController
@RequestMapping("/api/attractions")
public class AttractionController {

    @Autowired
    private AttractionRepository attractionRepository;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AttractionTicketRepository attractionTicketRepository;


    @PostMapping
    public Attraction createAttraction(@RequestBody Attraction attraction) {
        return attractionRepository.save(attraction);
    }

    // 根据ID获取单个景点
    @GetMapping("/{id}")
    public ResponseEntity<Attraction> getAttractionById(@PathVariable int id) {
        Optional<Attraction> attraction = attractionRepository.findById(id);
        return attraction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 根据景点ID获取该景点的所有评论
    @GetMapping("/{id}/comments")
    public List<Comment> getCommentsByAttractionId(@PathVariable Long id) {
        return commentRepository.findByEntityTypeAndEntityId("attraction", id);
    }

    // 创建评论
    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> createCommentForAttraction(@PathVariable int id, @RequestBody Comment comment) {
        Optional<Attraction> attraction = attractionRepository.findById(id);
        if (attraction.isPresent()) {
            comment.setEntityType("attraction");
            comment.setEntityId(id);
            Comment savedComment = commentRepository.save(comment);
            return ResponseEntity.ok(savedComment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 更新景点的评分
    @PutMapping("/{id}/rating")
    public ResponseEntity<Attraction> updateAttractionRating(@PathVariable int id, @RequestBody BigDecimal newRating) {
        Optional<Attraction> attraction = attractionRepository.findById(id);
        if (attraction.isPresent()) {
            attraction.get().setRating(newRating);
            Attraction updatedAttraction = attractionRepository.save(attraction.get());
            return ResponseEntity.ok(updatedAttraction);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Attraction> updateAttraction(@PathVariable int id,
                                                       @RequestParam("name") String name,
                                                       @RequestParam("location") String location,
                                                       @RequestParam("type") String type,
                                                       @RequestParam("openingStart") LocalTime openingStart,
                                                       @RequestParam("openingEnd") LocalTime openingEnd,
                                                       @RequestParam("description") String description,
                                                       @RequestParam("city") String city,
                                                       @RequestParam("duration") Integer duration,
                                                       @RequestParam("targetAudience") String targetAudience,
                                                       @RequestParam(value = "image", required = false) MultipartFile imageFile) throws IOException {

        Optional<Attraction> attractionOptional = attractionRepository.findById(id);
        if (!attractionOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Attraction attraction = attractionOptional.get();
        attraction.setName(name);
        attraction.setLocation(location);
        attraction.setType(type);
        attraction.setOpeningStart(openingStart);
        attraction.setOpeningEnd(openingEnd);
        attraction.setDescription(description);
        attraction.setCity(city);
        attraction.setDuration(duration);
        attraction.setTargetAudience(targetAudience);

        // 如果上传了新图片，则更新图片
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();  // 防止文件名冲突
            String imagePath = UPLOAD_DIR + fileName;
            Path path = Paths.get(imagePath);
            Files.createDirectories(path.getParent());
            Files.write(path, imageFile.getBytes());
            attraction.setImageUrl("/image/attraction/" + fileName);  // 更新相对路径用于前端访问
        }

        attractionRepository.save(attraction);
        return ResponseEntity.ok(attraction);
    }


    // 获取项目资源路径的上传目录 (在项目根目录下的 /src/main/resources/static/image/attraction)
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/image/attraction/";

    @GetMapping
    public List<Attraction> getAllAttractions() {
        return attractionRepository.findAll();
    }

    @PostMapping(consumes = "multipart/form-data")
    public Attraction createAttraction(@RequestParam("name") String name,
                                       @RequestParam("location") String location,
                                       @RequestParam("type") String type,
                                       @RequestParam("openingStart") LocalTime openingStart,
                                       @RequestParam("openingEnd") LocalTime openingEnd,
                                       @RequestParam("description") String description,
                                       @RequestParam("city") String city,
                                       @RequestParam("duration") Integer duration,
                                       @RequestParam("targetAudience") String targetAudience,
                                       @RequestParam("image") MultipartFile imageFile) throws IOException {

        Attraction attraction = new Attraction();
        attraction.setName(name);
        attraction.setLocation(location);
        attraction.setType(type);
        attraction.setOpeningStart(openingStart);
        attraction.setOpeningEnd(openingEnd);
        attraction.setDescription(description);
        attraction.setCity(city);
        attraction.setDuration(duration);
        attraction.setTargetAudience(targetAudience);

        // 处理图片上传
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();  // 防止文件名冲突
            String imagePath = UPLOAD_DIR + fileName;
            Path path = Paths.get(imagePath);
            Files.createDirectories(path.getParent());
            Files.write(path, imageFile.getBytes());
            attraction.setImageUrl("/image/attraction/" + fileName);  // 设置相对路径用于前端访问
        }

        return attractionRepository.save(attraction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttraction(@PathVariable int id) {
        if (attractionRepository.existsById(id)) {
            attractionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 根据景点ID获取该景点的所有票型
    @GetMapping("/{id}/tickets")
    public List<AttractionTicket> getTicketsByAttractionId(@PathVariable Long id) {
        return attractionTicketRepository.findByAttraction_AttractionId(id);
    }

    // 为景点添加票型
    @PostMapping("/{id}/tickets")
    public AttractionTicket addTicketToAttraction(@PathVariable int id, @RequestBody AttractionTicket ticket) {
        Optional<Attraction> attraction = attractionRepository.findById(id);
        if (attraction.isPresent()) {
            ticket.setAttraction(attraction.get());
            return attractionTicketRepository.save(ticket);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "景点不存在");
        }
    }

    // 更新票型信息
    @PutMapping("/tickets/{ticketId}")
    public ResponseEntity<AttractionTicket> updateTicket(@PathVariable Long ticketId, @RequestBody AttractionTicket updatedTicket) {
        Optional<AttractionTicket> existingTicket = attractionTicketRepository.findById(ticketId);
        if (existingTicket.isPresent()) {
            AttractionTicket ticket = existingTicket.get();
            ticket.setTicketType(updatedTicket.getTicketType());
            ticket.setPrice(updatedTicket.getPrice());
            ticket.setDescription(updatedTicket.getDescription());
            attractionTicketRepository.save(ticket);
            return ResponseEntity.ok(ticket);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 删除票型
    @DeleteMapping("/tickets/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long ticketId) {
        if (attractionTicketRepository.existsById(ticketId)) {
            attractionTicketRepository.deleteById(ticketId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
