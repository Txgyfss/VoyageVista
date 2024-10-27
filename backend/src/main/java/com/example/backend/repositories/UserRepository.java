package com.example.backend.repositories;

import com.example.backend.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 根据用户名查找用户
    Optional<User> findByUsername(String username);

    // 根据邮箱查找用户
    Optional<User> findByEmail(String email);

    Optional<User> findByUserId(long userId);
    // 根据用户名或邮箱和密码查找用户（用于登录）
    Optional<User> findByUsernameOrEmailAndPassword(String username, String email, String password);

    // 查找所有管理员用户
    List<User> findByRole(String role);

    // 查找某个用户的关注者
   // List<User> findFollowersByUserId(Long userId);

    // 查找某个用户关注的人
    //List<User> findfollowingByUserId(Long userId);

    // 根据用户名删除用户
    void deleteByUsername(String username);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password WHERE u.userId = :id")
    void updatePasswordById(@Param("id") Long id, @Param("password") String password);

    // 根据用户ID更新用户状态
    // void updateStatusById(Long id, String status);

    // 根据用户ID更新头像
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.avatarUrl = :avatarUrl WHERE u.userId = :id")
    void setAvatarUrlById(@Param("id") Long id, @Param("avatarUrl") String avatarUrl);


    // 通过 resetToken 查找用户
    Optional<User> findByResetToken(String resetToken);

    Optional<User> findByUsernameOrEmail(String login, String login1);
}
