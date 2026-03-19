package com.rebellion.notifyhub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import com.rebellion.notifyhub.dto.response.NotificationResponseDto;
import com.rebellion.notifyhub.entity.Notification;
import com.rebellion.notifyhub.entity.User;
import com.rebellion.notifyhub.entity.enums.NotificationStatus;
import com.rebellion.notifyhub.repository.NotificationRepo;
import com.rebellion.notifyhub.repository.UserRepo;
import com.rebellion.notifyhub.service.impl.NotificationServiceImpl;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

	@Mock
	private UserRepo userRepo;

	@Mock
	private NotificationRepo notificationRepo;

	@InjectMocks
	private NotificationServiceImpl notificationService;

	private User user;
	private Notification sentNotification;
	private Pageable pageable;

	@BeforeEach
	void setup() {
		user = new User("test@example.com", "Test User");
		ReflectionTestUtils.setField(user, "id", 1L);

		sentNotification = new Notification(user, "New Event", "Payload", "JOB_SHORTLISTED");
		ReflectionTestUtils.setField(sentNotification, "id", 10L);
		ReflectionTestUtils.setField(sentNotification, "status", NotificationStatus.SENT);
		ReflectionTestUtils.setField(sentNotification, "createdAt", LocalDateTime.of(2026, 3, 19, 10, 0));

		pageable = PageRequest.of(0, 10);
	}

	@Test
	void shouldReturnNotificationsForUser() {
		Page<Notification> page = new PageImpl<>(List.of(sentNotification), pageable, 1);
		when(userRepo.existsById(1L)).thenReturn(true);
		when(notificationRepo.findByUserId(1L, pageable)).thenReturn(page);

		Page<NotificationResponseDto> result = notificationService.getNotifications(1L, pageable);

		assertEquals(1, result.getTotalElements());
		assertEquals("New Event", result.getContent().get(0).getTitle());
		assertEquals(NotificationStatus.SENT, result.getContent().get(0).getStatus());
	}

	@Test
	void shouldReturnUnreadNotificationsForUser() {
		Page<Notification> page = new PageImpl<>(List.of(sentNotification), pageable, 1);
		when(userRepo.existsById(1L)).thenReturn(true);
		when(notificationRepo.findByUserIdAndStatus(1L, NotificationStatus.SENT, pageable)).thenReturn(page);

		Page<NotificationResponseDto> result = notificationService.getUnreadNotifications(1L, pageable);

		assertEquals(1, result.getContent().size());
		assertEquals(10L, result.getContent().get(0).getId());
		verify(notificationRepo).findByUserIdAndStatus(1L, NotificationStatus.SENT, pageable);
	}

	@Test
	void shouldMarkNotificationAsRead() {
		when(notificationRepo.findById(10L)).thenReturn(Optional.of(sentNotification));

		NotificationResponseDto response = notificationService.markAsRead(10L);

		assertEquals(NotificationStatus.READ, response.getStatus());
		assertEquals(10L, response.getId());
	}

	@Test
	void shouldThrowWhenUserMissingForNotificationLookup() {
		when(userRepo.existsById(99L)).thenReturn(false);

		assertThrows(EntityNotFoundException.class, () -> notificationService.getNotifications(99L, pageable));

		verify(notificationRepo, never()).findByUserId(any(), any());
	}

	@Test
	void shouldThrowWhenNotificationMissingForMarkRead() {
		when(notificationRepo.findById(404L)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> notificationService.markAsRead(404L));
	}

	@Test
	void shouldThrowWhenMarkReadCalledForPendingNotification() {
		Notification pendingNotification = new Notification(user, "Pending", "Payload", "SYSTEM");
		ReflectionTestUtils.setField(pendingNotification, "id", 12L);
		when(notificationRepo.findById(12L)).thenReturn(Optional.of(pendingNotification));

		assertThrows(IllegalStateException.class, () -> notificationService.markAsRead(12L));
	}
}
