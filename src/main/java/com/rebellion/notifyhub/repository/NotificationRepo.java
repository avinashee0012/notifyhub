package com.rebellion.notifyhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.rebellion.notifyhub.entity.Notification;
import com.rebellion.notifyhub.entity.enums.NotificationStatus;

import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long>{
    List<Notification> findByStatus(NotificationStatus pending);

	Page<Notification> findByUserId(Long userId, Pageable pageable);

	Page<Notification> findByUserIdAndStatus(Long userId, NotificationStatus status, Pageable pageable);
}
