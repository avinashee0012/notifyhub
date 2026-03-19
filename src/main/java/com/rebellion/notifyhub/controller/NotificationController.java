package com.rebellion.notifyhub.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
		@PageableDefault(size = 10, sort = "createdAt") Pageable pageable
	) {
		return ResponseEntity.ok(notificationService.getNotifications(userId, pageable));
	}

	@GetMapping("/unread")
	public ResponseEntity<Page<NotificationResponseDto>> getUnreadNotifications(
		@RequestParam Long userId,
		@PageableDefault(size = 10, sort = "createdAt") Pageable pageable
	) {
		return ResponseEntity.ok(notificationService.getUnreadNotifications(userId, pageable));
	}

	@PatchMapping("/{notificationId}/read")
	public ResponseEntity<NotificationResponseDto> markAsRead(@PathVariable Long notificationId) {
		return ResponseEntity.ok(notificationService.markAsRead(notificationId));
	}
}
