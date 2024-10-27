package com.example.backend.controllers;

import com.example.backend.models.Role;
import com.example.backend.models.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.EmailService;
import com.example.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@CrossOrigin(origins = "http://localhost:5173")  // 允许来自 http://localhost:5173 的跨域请求
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // 初始化 BCryptPasswordEncoder
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 用户注册方法
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        // 校验用户名是否存在
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }

        // 校验密码长度和格式
        if (user.getPassword().length() < 8 || !user.getPassword().matches(".*\\d.*") || !user.getPassword().matches(".*[a-zA-Z].*")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must be 8-16 characters long, containing both letters and numbers");
        }

        // 使用 BCrypt 对密码进行加密
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // 保存用户
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("Registered successfully");
    }

    // 检查用户名是否已存在
    @GetMapping("/check-username/{username}")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@PathVariable String username) {
        boolean exists = userRepository.findByUsername(username).isPresent();
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    // 用户登录方法
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        Optional<User> userOptional = userRepository.findByUsernameOrEmail(request.getLogin(), request.getLogin());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                String token = JwtUtil.generateToken(user.getUsername(), user.getRole().name(),user.getUserId());
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("role", user.getRole().name());  // 返回角色信息
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    public static class LoginRequest {
        private String login;
        private String password;

        // Getters and Setters
        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


    // 忘记密码方法
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            userRepository.save(user);

            String resetUrl = "http://your-domain.com/reset-password?token=" + resetToken;

            String subject = "重置您的密码";
            String text = "您好，" + user.getUsername() + "：\n\n" +
                    "您请求了密码重置，请点击以下链接进行重置：\n" +
                    resetUrl + "\n\n" +
                    "如果您没有请求此操作，请忽略此邮件。\n\n" +
                    "感谢您的使用！";
            emailService.sendSimpleMessage(email, subject, text);
        }

        return "redirect:/login.jsp?info=Password reset instructions sent";
    }

    // 处理密码重置请求
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (newPassword.equals(confirmPassword)) {
                user.setPassword(passwordEncoder.encode(newPassword));  // 确保密码加密
                user.setResetToken(null);  // 重置token
                userRepository.save(user);
                return "redirect:/login.jsp?success=Password reset successfully";
            } else {
                return "redirect:/reset-password.jsp?token=" + token + "&error=Passwords do not match";
            }
        } else {
            return "redirect:/login.jsp?error=Invalid token";
        }
    }




}
