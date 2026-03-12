package com.rebellion.notifyhub.service;

import com.rebellion.notifyhub.dto.request.PreferenceUpdateDto;
import com.rebellion.notifyhub.dto.response.PreferenceResponseDto;

public interface NotificationPreferenceService {
    PreferenceResponseDto getPreferences(Long userId);
    PreferenceResponseDto updatePreferences(Long userId, PreferenceUpdateDto request);
}
