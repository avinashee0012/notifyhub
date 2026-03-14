package com.rebellion.notifyhub.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.rebellion.notifyhub.dto.request.PreferenceUpdateDto;
import com.rebellion.notifyhub.dto.response.PreferenceResponseDto;
import com.rebellion.notifyhub.entity.NotificationPreference;
import com.rebellion.notifyhub.entity.User;
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
        User user = new User("user@email.com", "User");
        ReflectionTestUtils.setField(user, "id", 1L); 

        NotificationPreference pref = new NotificationPreference(user);
        pref.updatePushPreference(false);

        when(notificationPreferenceRepo.findByUserId(user.getId())).thenReturn(Optional.of(pref));

        PreferenceResponseDto response = notificationPreferenceService.getPreferences(user.getId());

        assertNotNull(response);
        assertTrue(response.isEmailEnabled());
        assertFalse(response.isPushEnabled());
    }

    @Test
    void shouldUpdatePreference() {
        User user = new User("user@email.com", "User");
        ReflectionTestUtils.setField(user, "id", 1L); 

        NotificationPreference pref = new NotificationPreference(user);
        PreferenceUpdateDto dto = new PreferenceUpdateDto(false, true);

        when(notificationPreferenceRepo.findByUserId(user.getId())).thenReturn(Optional.of(pref));

        notificationPreferenceService.updatePreferences(user.getId(), dto);

        assertFalse(pref.isEmailEnabled());
        assertTrue(pref.isPushEnabled());
    }
}
