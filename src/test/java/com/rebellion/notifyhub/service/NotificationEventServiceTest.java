package com.rebellion.notifyhub.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebellion.notifyhub.dto.request.NotificationEventRequestDto;
import com.rebellion.notifyhub.entity.NotificationEvent;
import com.rebellion.notifyhub.exception.CustomInvalidJsonStructure;
import com.rebellion.notifyhub.messaging.NotificationEventPublisher;
import com.rebellion.notifyhub.repository.NotificationEventRepo;
import com.rebellion.notifyhub.repository.UserRepo;
import com.rebellion.notifyhub.service.impl.NotificationEventServiceImpl;

@ExtendWith(MockitoExtension.class)
public class NotificationEventServiceTest {

	@Mock
	private UserRepo userRepo;

	@Mock
	private NotificationEventRepo notificationEventRepo;

	@Mock
	private NotificationEventPublisher notificationEventPublisher;

	@Mock
	private ObjectMapper objectMapper;

	@InjectMocks
	private NotificationEventServiceImpl notificationEventService;

	@Captor
	private ArgumentCaptor<NotificationEvent> eventCaptor;

	private NotificationEventRequestDto request;

	@BeforeEach
	void setup() {
		request = new NotificationEventRequestDto("JOB_SHORTLISTED", 1L, Map.of("jobTitle", "Backend Engineer"), 100L);
	}

	@Test
	void shouldCreateNotificationEventFromRequest() throws Exception {
		when(objectMapper.writeValueAsString(any())).thenReturn("{\"jobTitle\":\"Backend Engineer\"}");

		when(userRepo.existsById(anyLong())).thenReturn(true);

		notificationEventService.ingestEvent(request);

		verify(notificationEventRepo).save(eventCaptor.capture());

		NotificationEvent event = eventCaptor.getValue();

		assertEquals("JOB_SHORTLISTED", event.getEventType());
		assertEquals(1L, event.getUserId());
		assertEquals("{\"jobTitle\":\"Backend Engineer\"}", event.getPayload());
	}

	@Test
	void shouldSaveEventToRepository() throws Exception {
		when(objectMapper.writeValueAsString(any())).thenReturn("{\"jobTitle\":\"Backend Engineer\"}");

		when(userRepo.existsById(anyLong())).thenReturn(true);

		NotificationEvent savedEvent = new NotificationEvent("JOB_SHORTLISTED", 100L, 1L, "{\"jobTitle\":\"Backend Engineer\"}");

		when(notificationEventRepo.save(any(NotificationEvent.class))).thenReturn(savedEvent);

		notificationEventService.ingestEvent(request);

		verify(notificationEventRepo, times(1)).save(any(NotificationEvent.class));
	}

	@Test
	void shouldPublishSavedEvent() throws Exception {
		when(objectMapper.writeValueAsString(any())).thenReturn("{\"jobTitle\":\"Backend Engineer\"}");

		when(userRepo.existsById(anyLong())).thenReturn(true);

		NotificationEvent savedEvent = new NotificationEvent("JOB_SHORTLISTED", 100L, 1L, "{\"jobTitle\":\"Backend Engineer\"}");

		when(notificationEventRepo.save(any(NotificationEvent.class))).thenReturn(savedEvent);

		notificationEventService.ingestEvent(request);

		verify(notificationEventPublisher).publish(savedEvent);
	}

	@Test
	void shouldCallSaveBeforePublish() throws Exception {
		when(objectMapper.writeValueAsString(any())).thenReturn("{\"jobTitle\":\"Backend Engineer\"}");

		when(userRepo.existsById(anyLong())).thenReturn(true);

		NotificationEvent savedEvent = new NotificationEvent("JOB_SHORTLISTED", 100L, 1L, "{\"jobTitle\":\"Backend Engineer\"}");

		when(notificationEventRepo.save(any(NotificationEvent.class))).thenReturn(savedEvent);

		notificationEventService.ingestEvent(request);

		InOrder inOrder = inOrder(notificationEventRepo, notificationEventPublisher);

		inOrder.verify(notificationEventRepo).save(any(NotificationEvent.class));
		inOrder.verify(notificationEventPublisher).publish(savedEvent);
	}

	@Test
	void shouldThrowExceptionWhenJsonProcessingFails() throws Exception {
		when(userRepo.existsById(anyLong())).thenReturn(true);
		
		when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Error") {});

		assertThrows(CustomInvalidJsonStructure.class, () -> notificationEventService.ingestEvent(request));

		verify(notificationEventRepo, never()).save(any());
		verify(notificationEventPublisher, never()).publish(any());
	}
}