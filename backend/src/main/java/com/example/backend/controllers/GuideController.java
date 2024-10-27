package com.example.backend.controllers;
import com.example.backend.dto.GuideDTO;
import com.example.backend.models.Guide;
import com.example.backend.models.GuideTag;
import com.example.backend.models.GuideTagMap;
import com.example.backend.repositories.GuideRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.repositories.GuideTagMapRepository;
import com.example.backend.repositories.GuideTagRepository;
import com.example.backend.services.GuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.example.backend.models.User;
import com.example.backend.repositories.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@RestController
@RequestMapping("/api/guides")
@CrossOrigin(origins = "http://localhost:5173")
public class GuideController {
    @Autowired
    private UserRepository UserRepository;  // 注入 UserRepository
    @Autowired
    private GuideRepository guideRepository;
    private static final Logger logger = LoggerFactory.getLogger(GuideController.class);
    @Autowired
    private GuideService guideService;

    @Autowired
    private GuideTagRepository guideTagRepository;

    @Autowired
    private GuideTagMapRepository guideTagMapRepository;

    // 获取所有公开的攻略
    @GetMapping
    public ResponseEntity<List<Guide>> getAllGuides() {
        List<Guide> guides = guideRepository.findAll();
        return ResponseEntity.ok(guides);
    }

    // 根据关键词搜索攻略,支持模糊搜索
    // 搜索攻略
    @GetMapping("/search")
    public ResponseEntity<List<GuideDTO>> searchGuides(@RequestParam String keyword) {
        List<Guide> guides = guideRepository.searchGuides(keyword);
        List<GuideDTO> guideDTOs = guides.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(guideDTOs);
    }

    // 将 Guide 转换为 GuideDTO 的方法
    private GuideDTO convertToDTO(Guide guide) {
        GuideDTO dto = new GuideDTO();
        dto.setGuideId(guide.getGuideId());
        dto.setTitle(guide.getTitle());
        dto.setContent(guide.getContent());
        dto.setExpensePerPerson(guide.getExpensePerPerson());
        dto.setDays(guide.getDays());
        dto.setIsPublic(guide.getIsPublic());
        dto.setUsername(guide.getUser().getUsername());
        dto.setCreatedAt(guide.getCreatedAt());
        dto.setTags(guide.getTags().stream().map(tagMap -> tagMap.getTag().getName()).collect(Collectors.toList()));
        return dto;
    }



    @GetMapping("/my")
    public ResponseEntity<List<GuideDTO>> getMyGuides(@RequestParam Integer userId) {
        List<Guide> myGuides = guideRepository.findByUser_UserId(userId);

        // 转换为 GuideDTO
        List<GuideDTO> guideDTOs = myGuides.stream().map(guide -> {
            GuideDTO dto = new GuideDTO();
            dto.setGuideId(guide.getGuideId());  // 确保设置 guideId
            dto.setTitle(guide.getTitle());
            dto.setContent(guide.getContent());
            dto.setExpensePerPerson(guide.getExpensePerPerson());
            dto.setDays(guide.getDays());
            dto.setIsPublic(guide.getIsPublic());
            dto.setTagIds(guide.getTags().stream().map(tag -> tag.getTag().getTagId()).collect(Collectors.toList()));
            dto.setUsername(guide.getUser().getUsername());  // 设置用户名
            dto.setUserId(guide.getUser().getUserId());  // 设置 userId
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(guideDTOs);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Guide> updateGuide(@PathVariable Long id, @RequestBody GuideDTO guideDTO) {
        Guide updatedGuide = guideService.updateGuide(id, guideDTO);
        return ResponseEntity.ok(updatedGuide);
    }


    @GetMapping("/public")
    public ResponseEntity<List<GuideDTO>> getPublicGuides() {
        logger.info("getPublicGuides() 被调用了");  // 确认方法是否被调用
        List<Guide> publicGuides = guideRepository.findByIsPublic(true);

        // 将 Guide 转换为 GuideDTO，包含作者信息、标签、创建时间等
        List<GuideDTO> guideDTOs = publicGuides.stream().map(guide -> {
            GuideDTO dto = new GuideDTO();
            dto.setGuideId(guide.getGuideId());
            dto.setTitle(guide.getTitle());
            dto.setContent(guide.getContent());
            dto.setExpensePerPerson(guide.getExpensePerPerson());
            dto.setDays(guide.getDays());
            dto.setIsPublic(guide.getIsPublic());
            dto.setUsername(guide.getUser().getUsername());  // 包含作者信息
            dto.setCreatedAt(guide.getCreatedAt());  // 包含创建时间
            dto.setTags(guide.getTags().stream()
                    .map(tagMap -> tagMap.getTag().getName())  // 获取标签名称
                    .collect(Collectors.toList()));  // 将标签名称保存到 DTO 中
            logger.info("转换的 GuideDTO: {}", dto);  // 打印转换后的 DTO 对象t(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
        logger.info("GuideDTO 列表: {}", guideDTOs);  // 确认发送的数据
        return ResponseEntity.ok(guideDTOs);
    }

    @GetMapping("/dto")
    public ResponseEntity<List<GuideDTO>> getAllGuidesWithDTO() {
        logger.info("getAllGuidesWithDTO() 被调用了");  // 确认方法是否被调用

        // 获取所有攻略
        List<Guide> guides = guideRepository.findAll();

        // 将 Guide 转换为 GuideDTO，包含作者信息、标签、创建时间等
        List<GuideDTO> guideDTOs = guides.stream().map(guide -> {
            GuideDTO dto = new GuideDTO();
            dto.setGuideId(guide.getGuideId());
            dto.setTitle(guide.getTitle());
            dto.setContent(guide.getContent());
            dto.setExpensePerPerson(guide.getExpensePerPerson());
            dto.setDays(guide.getDays());
            dto.setIsPublic(guide.getIsPublic());
            dto.setUsername(guide.getUser().getUsername());  // 包含作者信息
            dto.setCreatedAt(guide.getCreatedAt());  // 包含创建时间
            dto.setTags(guide.getTags().stream()
                    .map(tagMap -> tagMap.getTag().getName())  // 获取标签名称
                    .collect(Collectors.toList()));  // 将标签名称保存到 DTO 中

            logger.info("转换的 GuideDTO: {}", dto);  // 打印转换后的 DTO 对象
            return dto;
        }).collect(Collectors.toList());

        logger.info("GuideDTO 列表: {}", guideDTOs);  // 确认发送的数据
        return ResponseEntity.ok(guideDTOs);
    }


    // 删除攻略
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuide(@PathVariable Long id) {
        guideService.deleteGuide(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Guide> createGuide(@RequestBody GuideDTO guideDTO) {
        Guide guide = new Guide();
        guide.setTitle(guideDTO.getTitle());
        guide.setContent(guideDTO.getContent());
        guide.setExpensePerPerson(guideDTO.getExpensePerPerson());
        guide.setDays(guideDTO.getDays());
        guide.setIsPublic(guideDTO.getIsPublic());

        // 从 GuideDTO 中获取 userId
        long userId = guideDTO.getUserId();
        User user = UserRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在: " + userId));
        guide.setUser(user);  // 设置 user 字段

        Guide savedGuide = guideRepository.save(guide);

        // 保存标签映射
        for (Long tagId : guideDTO.getTagIds()) {
            GuideTag tag = guideTagRepository.findById(tagId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "标签ID不存在: " + tagId));
            GuideTagMap tagMap = new GuideTagMap();
            tagMap.setGuide(savedGuide);
            tagMap.setTag(tag);
            guideTagMapRepository.save(tagMap);
        }

        return ResponseEntity.ok(savedGuide);
    }





}
