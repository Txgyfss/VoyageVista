package com.example.backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "private_messages")
public class PrivateMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;  // 私信唯一标识

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;  // 发送者

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;  // 接收者

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;  // 私信内容

    @Column(nullable = false)
    private LocalDateTime sentAt = LocalDateTime.now();  // 发送时间

    @Column(nullable = false)
    private boolean isRead = false;  // 标记是否已读，默认为未读

    // getters and setters
    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
