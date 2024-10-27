package com.example.backend.controllers;

import com.example.backend.dto.BlogDTO;
import com.example.backend.models.Blog;
import com.example.backend.models.User;
import com.example.backend.repositories.BlogRepository;
import com.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin(origins = "http://localhost:5173")
public class BlogController {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    // 文件保存路径，可以将其改为你所使用的路径或云存储路径
    // Upload directory for food images
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/image/blog/";

    @GetMapping("/my/{userId}")
    public ResponseEntity<List<BlogDTO>> getUserBlogs(@PathVariable Long userId) {
        List<Blog> userBlogs = blogRepository.findByUser_UserId(userId);

        List<BlogDTO> blogDTOs = userBlogs.stream().map(blog -> {
            BlogDTO dto = new BlogDTO();
            dto.setBlogId(blog.getBlogId());
            dto.setTitle(blog.getTitle());
            dto.setContent(blog.getContent());
            dto.setIsPublic(blog.getIsPublic());
            dto.setUsername(blog.getUser().getUsername());
            dto.setCreatedAt(blog.getCreatedAt());
            dto.setUpdatedAt(blog.getUpdatedAt());

            // 对 tags 进行 null 检查，避免 split() 调用报错
            String tags = blog.getTags();
            if (tags != null && !tags.isEmpty()) {
                dto.setTags(Arrays.asList(tags.split(",")));  // 将 tags 按逗号分割成 List
            } else {
                dto.setTags(Collections.emptyList());  // 如果 tags 为 null 或空字符串，则设置为空列表
            }

            dto.setImageurl(blog.getImageurl());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(blogDTOs);
    }


    @GetMapping("/public")
    public ResponseEntity<List<BlogDTO>> getPublicBlogs() {
        List<Blog> publicBlogs = blogRepository.findByIsPublic(true);

        List<BlogDTO> blogDTOs = publicBlogs.stream().map(blog -> {
            BlogDTO dto = new BlogDTO();
            dto.setBlogId(blog.getBlogId());
            dto.setTitle(blog.getTitle());
            dto.setContent(blog.getContent());
            dto.setIsPublic(blog.getIsPublic());
            dto.setUsername(blog.getUser().getUsername());
            dto.setCreatedAt(blog.getCreatedAt());
            dto.setUpdatedAt(blog.getUpdatedAt());

            // 对 tags 进行 null 检查，避免 split() 调用报错
            String tags = blog.getTags();
            if (tags != null) {
                dto.setTags(Arrays.asList(tags.split(",")));  // 将 tags 按逗号分割成 List
            } else {
                dto.setTags(Collections.emptyList());  // 如果 tags 为 null，则设置为空列表
            }

            dto.setImageurl(blog.getImageurl());  // 添加图片链接
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(blogDTOs);
    }



    // 编辑博客
    @PutMapping("/{blogId}")
    public ResponseEntity<BlogDTO> updateBlog(
            @PathVariable int blogId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("isPublic") Boolean isPublic,
            @RequestParam("tags") List<String> tags,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "博客不存在"));

        blog.setTitle(title);
        blog.setContent(content);
        blog.setIsPublic(isPublic);

        // 将 tags 列表转换为逗号分隔的字符串存储
        String tagsString = String.join(",", tags);
        blog.setTags(tagsString);  // 将 List<String> 转换为逗号分隔的字符串

        // 处理上传图片
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                File destFile = new File(UPLOAD_DIR + fileName);
                file.transferTo(destFile);
                blog.setImageurl("/image/blog/" + fileName);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "图片上传失败");
            }
        }

        blogRepository.save(blog);

        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setBlogId(blog.getBlogId());
        blogDTO.setTitle(blog.getTitle());
        blogDTO.setContent(blog.getContent());
        blogDTO.setIsPublic(blog.getIsPublic());

        // 将逗号分隔的字符串转换为 List<String> 返回给前端
        blogDTO.setTags(Arrays.asList(blog.getTags().split(",")));

        blogDTO.setImageurl(blog.getImageurl());

        return ResponseEntity.ok(blogDTO);
    }

    // 创建博客
    @PostMapping
    public ResponseEntity<BlogDTO> createBlog(
            @RequestParam("userId") Long userId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("isPublic") Boolean isPublic,
            @RequestParam("tags") List<String> tags, // 接受 List<String> 作为标签
            @RequestParam(value = "file", required = false) MultipartFile file) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在"));

        Blog blog = new Blog();
        blog.setUser(user);
        blog.setTitle(title);
        blog.setContent(content);
        blog.setIsPublic(isPublic);
        blog.setTags(String.join(",", tags));  // 将标签列表转为逗号分隔的字符串

        // 处理图片上传...
        // 处理上传图片
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                File destFile = new File(UPLOAD_DIR + fileName);
                file.transferTo(destFile);
                blog.setImageurl("/image/blog/" + fileName);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "图片上传失败");
            }
        }

        Blog savedBlog = blogRepository.save(blog);

        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setBlogId(savedBlog.getBlogId());
        blogDTO.setTitle(savedBlog.getTitle());
        blogDTO.setContent(savedBlog.getContent());
        blogDTO.setIsPublic(savedBlog.getIsPublic());
        blogDTO.setUsername(savedBlog.getUser().getUsername());
        blogDTO.setTags(Arrays.asList(savedBlog.getTags().split(",")));  // 将字符串转为列表
        blogDTO.setImageUrl(savedBlog.getImageUrl());

        return ResponseEntity.status(HttpStatus.CREATED).body(blogDTO);
    }





    // 删除博客
    @DeleteMapping("/{blogId}")
    public ResponseEntity<Void> deleteBlog(@PathVariable int blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "博客不存在"));

        blogRepository.delete(blog);
        return ResponseEntity.noContent().build();
    }

}
