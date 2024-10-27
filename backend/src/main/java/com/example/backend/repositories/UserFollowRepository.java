package com.example.backend.repositories;

import com.example.backend.models.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, Integer> {

    // 根据用户ID查找所有关注者
    List<UserFollow> findByFollowerUserId(Long followerUserId);

    // 根据用户ID查找所有被关注的用户
   // List<UserFollow> findByfollowingUserId(Long followingUserId);

    // 查找用户是否已经关注某个用户
   // boolean existsByFollowerUserIdAndfollowingUserId(Long followerUserId, Long followingUserId);

    // 删除关注关系
   // void deleteByFollowerUserIdAndfollowingUserId(Long followerUserId, Long followingUserId);
}
