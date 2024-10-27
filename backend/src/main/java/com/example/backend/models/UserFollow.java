package com.example.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "user_follows")
public class UserFollow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;  // 关注记录唯一标识

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;  // 关注者

    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
    private User following;  // 被关注者

    // getters and setters
    public Long getFollowId() {
        return followId;
    }

    public void setFollowId(Long followId) {
        this.followId = followId;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowing() {
        return following;
    }

    public void setFollowing(User following) {
        this.following = following;
    }
}
