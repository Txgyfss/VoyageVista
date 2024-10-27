package com.example.backend.controllers;

import com.example.backend.models.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.dto.UserDTO;  // 引入 UserDTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/user-management")
public class UserManagementController {

    @Autowired
    private UserRepository userRepository;

    // 获取所有用户，返回 UserDTO 列表
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        // 将 User 转换为 UserDTO，只返回基本信息
        List<UserDTO> userDTOs = users.stream()
                .map(user -> new UserDTO(user.getUserId(), user.getUsername(), user.getEmail(), user.getPhoneNumber(), user.getRole()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    // 根据ID获取用户，只返回 UserDTO
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> ResponseEntity.ok(new UserDTO(
                        value.getUserId(),
                        value.getUsername(),
                        value.getEmail(),
                        value.getPhoneNumber(),
                        value.getRole())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 更新用户，只返回 UserDTO
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            existingUser.setUsername(userDetails.getUsername());
            existingUser.setEmail(userDetails.getEmail());
            existingUser.setPhoneNumber(userDetails.getPhoneNumber());
            existingUser.setRole(userDetails.getRole());
            userRepository.save(existingUser);
            UserDTO userDTO = new UserDTO(existingUser.getUserId(), existingUser.getUsername(), existingUser.getEmail(), existingUser.getPhoneNumber(), existingUser.getRole());
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 创建新用户，只返回 UserDTO
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User newUser) {
        // 检查用户名是否已存在
        if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 用户名已存在
        }

        // 使用 BCrypt 对密码进行加密
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        // 保存用户
        User savedUser = userRepository.save(newUser);
        UserDTO userDTO = new UserDTO(savedUser.getUserId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getPhoneNumber(), savedUser.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }
}
