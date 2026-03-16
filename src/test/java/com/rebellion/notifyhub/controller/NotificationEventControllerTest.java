package com.rebellion.notifyhub.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebellion.notifyhub.dto.request.NotificationEventRequestDto;
import com.rebellion.notifyhub.service.NotificationEventService;

@WebMvcTest(NotificationEventController.class)
@AutoConfigureMockMvc(addFilters = false)
public class NotificationEventControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private NotificationEventService notificationEventService;

	@Autowired
	private ObjectMapper objectMapper;

	private NotificationEventRequestDto request;

	@BeforeEach
	void Setup() {
		request = new NotificationEventRequestDto("JOB_SHORTLISTED", "{\"userId\":12,\"jobTitle\":\"Backend Engineer\"}");
	}

	@Test
	void CreateEventShouldReturnAcceptedStatus() throws Exception {
		mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpect(status().isAccepted());
	}

	@Test
	void CreateEventShouldCallEventService() throws Exception {
		mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpect(status().isAccepted());

		verify(notificationEventService).ingestEvent(any(NotificationEventRequestDto.class));
	}

	@Test
	void CreateEventShouldAcceptPayloadWithUserId() throws Exception {
		String body = """
				{
				    "eventType": "JOB_SHORTLISTED",
				    "payload": "{\\"userId\\":12,\\"jobTitle\\":\\"Backend Engineer\\"}"
				}
				""";

		mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isAccepted());
	}

	@Test
	void CreateEventShouldReturnBadRequestWhenEventTypeMissing() throws Exception {
		String body = """
				{
				    "payload": "{\\"userId\\":12}"
				}
				""";

		mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest());
	}
}
