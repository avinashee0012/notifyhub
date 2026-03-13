package com.rebellion.notifyhub.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rebellion.notifyhub.dto.request.PreferenceUpdateDto;
import com.rebellion.notifyhub.dto.response.PreferenceResponseDto;
import com.rebellion.notifyhub.entity.NotificationPreference;
import com.rebellion.notifyhub.repository.NotificationPreferenceRepo;
import com.rebellion.notifyhub.service.impl.NotificationPreferenceServiceImpl;

@ExtendWith(MockitoExtension.class)
public class NotificationPreferenceServiceTest {

    @Mock
    private NotificationPreferenceRepo notificationPreferenceRepo;

    @InjectMocks
    private NotificationPreferenceServiceImpl notificationPreferenceService;

    @Test
    void shouldReturnPreferenceWhenExists() {
        Long userId = 1L;
        NotificationPreference pref = new NotificationPreference();
        pref.updateEmailPreference(true);
        pref.updatePushPreference(false);

        when(notificationPreferenceRepo.findByUserId(userId)).thenReturn(Optional.of(pref));

        PreferenceResponseDto response = notificationPreferenceService.getPreferences(userId);

        assertTrue(response.isEmailEnabled());
        assertFalse(response.isPushEnabled());
    }

    @Test
    void shouldUpdatePreference() {
        Long userId = 1L;

        NotificationPreference pref = new NotificationPreference();
        pref.updateEmailPreference(true);
        pref.updatePushPreference(true);

        PreferenceUpdateDto dto = new PreferenceUpdateDto();
        dto.setEmailEnabled(false);
        dto.setPushEnabled(true);

        when(notificationPreferenceRepo.findByUserId(userId)).thenReturn(Optional.of(pref));

        notificationPreferenceService.updatePreferences(userId, dto);

        verify(notificationPreferenceRepo).save(pref);

        assertFalse(pref.isEmailEnabled());
    }
}
