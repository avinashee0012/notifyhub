package com.rebellion.notifyhub.service;

import com.rebellion.notifyhub.entity.NotificationEvent;
import com.rebellion.notifyhub.dto.response.NotificationResponseDto;

import org.springframework.data.domain.Page;

public interface NotificationService {
	void createNotification(NotificationEvent event);

    void processPendingNotifications();

	Page<NotificationResponseDto> getNotifications(Long userId, int page, int size);

	Page<NotificationResponseDto> getUnreadNotifications(Long userId, int page, int size);

	NotificationResponseDto markAsRead(Long notificationId);
}
