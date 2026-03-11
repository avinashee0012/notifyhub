package com.rebellion.notifyhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rebellion.notifyhub.entity.Notification;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long>{
}
