package com.rebellion.notifyhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rebellion.notifyhub.entity.NotificationEvent;

@Repository
public interface NotificationEventRepo extends JpaRepository<NotificationEvent, Long>{
	boolean existsByEventId(Long eventId);
}
