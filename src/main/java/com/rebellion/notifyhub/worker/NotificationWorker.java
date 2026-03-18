package com.rebellion.notifyhub.worker;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rebellion.notifyhub.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationWorker {

	private final NotificationService notificationService;

	@Scheduled(fixedDelay = 10000) // runs every 10 seconds
	public void processNotification(){
		notificationService.processPendingNotifications();
	}
}
