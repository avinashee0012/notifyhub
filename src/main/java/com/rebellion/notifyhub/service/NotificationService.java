package com.rebellion.notifyhub.service;

import com.rebellion.notifyhub.entity.NotificationEvent;

public interface NotificationService {
	void createNotification(NotificationEvent event);

    void processPendingNotifications();
}
