package com.rebellion.notifyhub.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.rebellion.notifyhub.dto.response.NotificationResponseDto;
import com.rebellion.notifyhub.entity.enums.NotificationStatus;
import com.rebellion.notifyhub.service.NotificationService;

@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotificationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private NotificationService notificationService;

	@Test
	void shouldReturnNotificationsPage() throws Exception {
		NotificationResponseDto response = new NotificationResponseDto(
			1L,
			5L,
			"Welcome",
			"Hello there",
			"SYSTEM",
			NotificationStatus.SENT,
			LocalDateTime.of(2026, 3, 19, 10, 0)
		);
		when(notificationService.getNotifications(eq(5L), any()))
			.thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1));

		mockMvc.perform(get("/api/notifications").param("userId", "5"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].id").value(1))
			.andExpect(jsonPath("$.content[0].title").value("Welcome"))
			.andExpect(jsonPath("$.content[0].status").value("SENT"));

		verify(notificationService).getNotifications(eq(5L), any());
	}

	@Test
	void shouldReturnUnreadNotificationsPage() throws Exception {
		NotificationResponseDto response = new NotificationResponseDto(
			2L,
			5L,
			"Unread",
			"Please review",
			"SYSTEM",
			NotificationStatus.SENT,
			LocalDateTime.of(2026, 3, 19, 10, 5)
		);
		when(notificationService.getUnreadNotifications(eq(5L), any()))
			.thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1));

		mockMvc.perform(get("/api/notifications/unread").param("userId", "5"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].id").value(2))
			.andExpect(jsonPath("$.content[0].message").value("Please review"));

		verify(notificationService).getUnreadNotifications(eq(5L), any());
	}

	@Test
	void shouldMarkNotificationAsRead() throws Exception {
		NotificationResponseDto response = new NotificationResponseDto(
			3L,
			5L,
			"Read me",
			"Updated",
			"SYSTEM",
			NotificationStatus.READ,
			LocalDateTime.of(2026, 3, 19, 10, 10)
		);
		when(notificationService.markAsRead(3L)).thenReturn(response);

		mockMvc.perform(patch("/api/notifications/{notificationId}/read", 3L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(3))
			.andExpect(jsonPath("$.status").value("READ"));

		verify(notificationService).markAsRead(3L);
	}
}
