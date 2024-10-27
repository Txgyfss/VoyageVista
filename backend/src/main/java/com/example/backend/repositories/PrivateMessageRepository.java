package com.example.backend.repositories;

import com.example.backend.models.PrivateMessage;
import com.example.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {

    // 根据发送者ID查找所有发送的私信
    List<PrivateMessage> findBySenderUserId(Long senderUserId);

    // 根据接收者ID查找所有接收的私信
    List<PrivateMessage> findByReceiverUserId(Long receiverUserId);

    // 查找两用户之间的所有私信
    List<PrivateMessage> findBySenderAndReceiver(User sender, User receiver);

    // 查找未读的私信
    List<PrivateMessage> findByReceiverUserIdAndIsReadFalse(Long receiverUserId);

    // 根据私信ID标记私信为已读
    @Modifying
    @Transactional
    @Query("UPDATE PrivateMessage pm SET pm.isRead = true WHERE pm.messageId = :messageId")
    void markAsReadByMessageId(Long messageId);

    // 删除私信
    void deleteByMessageId(Long messageId);
}
