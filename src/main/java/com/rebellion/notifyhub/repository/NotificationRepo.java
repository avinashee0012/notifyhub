package com.rebellion.notifyhub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rebellion.notifyhub.entity.Notification;
import com.rebellion.notifyhub.entity.enums.NotificationStatus;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long>{
    List<Notification> findByStatus(NotificationStatus pending);
}
