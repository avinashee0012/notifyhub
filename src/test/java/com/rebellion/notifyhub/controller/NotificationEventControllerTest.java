package com.rebellion.notifyhub.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;

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
class NotificationEventControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private NotificationEventService notificationEventService;

	@Autowired
	private ObjectMapper objectMapper;

	private NotificationEventRequestDto request;

	@BeforeEach
	void setup() {
		request = new NotificationEventRequestDto("JOB_SHORTLISTED", 12L, Map.of("jobTitle", "Backend Engineer"), 100L);
	}

	@Test
	void createEventShouldReturnAcceptedStatus() throws Exception {
		mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpect(status().isAccepted());
	}

	@Test
	void createEventShouldCallEventService() throws Exception {
		mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpect(status().isAccepted());

		verify(notificationEventService).ingestEvent(any(NotificationEventRequestDto.class));
	}

	@Test
	void createEventShouldAcceptValidPayload() throws Exception {
		String body = """
				{
				  "eventType": "JOB_SHORTLISTED",
				  "userId": 12,
				  "payload": {
				    "jobTitle": "Backend Engineer"
				  },
				  "eventId": 100
				}
				""";

		mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isAccepted());
	}

	@Test
	void createEventShouldReturnBadRequestWhenEventTypeMissing() throws Exception {
		String body = """
				{
				  "userId": 12,
				  "payload": {
				    "jobTitle": "Backend Engineer"
				  },
				  "eventId": 100
				}
				""";

		mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest());
	}

	@Test
	void createEventShouldReturnBadRequestWhenPayloadMissing() throws Exception {
		String body = """
				{
				  "eventType": "JOB_SHORTLISTED",
				  "userId": 12,
				  "eventId": 100
				}
				""";

		mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest());
	}
}