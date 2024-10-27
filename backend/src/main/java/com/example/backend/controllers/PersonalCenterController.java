package com.example.backend.controllers;

import com.example.backend.dto.UserDTO;
import com.example.backend.models.User;
import com.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/api/personal-center")
@CrossOrigin(origins = "http://localhost:5173") // 允许跨域请求
public class PersonalCenterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/image/user/";

    // 获取用户信息
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDTO userDTO = new UserDTO(
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getRole()
            );
            userDTO.setAvatarUrl(user.getAvatarUrl()); // 设置头像URL
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 更新用户信息
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO updatedUser) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            userRepository.save(existingUser);

            updatedUser.setAvatarUrl(existingUser.getAvatarUrl()); // 保持头像信息
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 更新用户密码
    @PutMapping("/change-password/{id}")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestParam String oldPassword, @RequestParam String newPassword) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return ResponseEntity.ok("Password changed successfully");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Old password is incorrect");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    // 上传头像
    @PostMapping("/upload-avatar/{id}")
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam("avatar") MultipartFile avatarFile) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // 为了防止文件名冲突，使用时间戳作为前缀
            String fileName = System.currentTimeMillis() + "_" + avatarFile.getOriginalFilename();

            String imagePath = UPLOAD_DIR + fileName;
            Path path = Paths.get(imagePath);

            try {
                // 如果目录不存在，创建目录
                Files.createDirectories(path.getParent());

                // 将文件写入磁盘
                Files.write(path, avatarFile.getBytes());

                // 设置相对路径用于前端访问
                user.setAvatarUrl("/image/user/" + fileName);

                // 保存用户更新信息
                userRepository.save(user);

                return ResponseEntity.ok("Avatar uploaded successfully");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Avatar upload failed");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
