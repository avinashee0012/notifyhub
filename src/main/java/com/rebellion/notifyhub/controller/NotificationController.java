package com.rebellion.notifyhub.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rebellion.notifyhub.dto.response.NotificationResponseDto;
import com.rebellion.notifyhub.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping
	public ResponseEntity<Page<NotificationResponseDto>> getNotifications(
		@RequestParam Long userId,
		@RequestParam(defaultValue = "0") int page,
    	@RequestParam(defaultValue = "10") int size
	) {
		size = Math.max(size, 50); // to prevent abuse
		return ResponseEntity.ok(notificationService.getNotifications(userId, page, size));
	}

	@GetMapping("/unread")
	public ResponseEntity<Page<NotificationResponseDto>> getUnreadNotifications(
		@RequestParam Long userId,
		@RequestParam(defaultValue = "0") int page,
    	@RequestParam(defaultValue = "10") int size
	) {
		size = Math.max(size, 50);
		return ResponseEntity.ok(notificationService.getUnreadNotifications(userId, page, size));
	}

	@PatchMapping("/{notificationId}/read")
	public ResponseEntity<NotificationResponseDto> markAsRead(@PathVariable Long notificationId) {
		return ResponseEntity.ok(notificationService.markAsRead(notificationId));
	}
}
