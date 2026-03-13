package com.rebellion.notifyhub.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebellion.notifyhub.dto.request.PreferenceUpdateDto;
import com.rebellion.notifyhub.dto.response.PreferenceResponseDto;
import com.rebellion.notifyhub.service.NotificationPreferenceService;

@WebMvcTest(NotificationPreferenceController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotificationPreferenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationPreferenceService notificationPreferenceService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnPreferences() throws Exception {
        Long userId = 1L;

        PreferenceResponseDto response = new PreferenceResponseDto(true, false);

        when(notificationPreferenceService.getPreferences(userId)).thenReturn(response);

        mockMvc.perform(get("/api/preferences/{userId}", userId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.emailEnabled").value(true))
                .andExpect(jsonPath("$.pushEnabled").value(false));

        verify(notificationPreferenceService).getPreferences(userId);
    }

    @Test
    void shouldUpdatePreferences() throws Exception {
        Long userId = 1L;

        PreferenceUpdateDto request = new PreferenceUpdateDto();
        request.setEmailEnabled(true);
        request.setPushEnabled(true);

        PreferenceResponseDto response = new PreferenceResponseDto(true, true);

        when(notificationPreferenceService.updatePreferences(eq(userId), any(PreferenceUpdateDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/preferences/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailEnabled").value(true))
                .andExpect(jsonPath("$.pushEnabled").value(true));

        verify(notificationPreferenceService).updatePreferences(userId, request);
    }
}
