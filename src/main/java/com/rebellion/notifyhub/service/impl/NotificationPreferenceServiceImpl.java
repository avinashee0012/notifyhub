package com.rebellion.notifyhub.service.impl;

import org.springframework.stereotype.Service;

import com.rebellion.notifyhub.dto.request.PreferenceUpdateDto;
import com.rebellion.notifyhub.dto.response.PreferenceResponseDto;
import com.rebellion.notifyhub.entity.NotificationPreference;
import com.rebellion.notifyhub.repository.NotificationPreferenceRepo;
import com.rebellion.notifyhub.service.NotificationPreferenceService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationPreferenceServiceImpl implements NotificationPreferenceService {

    private final NotificationPreferenceRepo notificationPreferenceRepo;

    @Override
    public PreferenceResponseDto getPreferences(Long userId) {
        NotificationPreference preference = notificationPreferenceRepo.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("Preference not found"));
        return new PreferenceResponseDto(preference.isEmailEnabled(), preference.isPushEnabled());
    }

    @Override
    @Transactional
    public PreferenceResponseDto updatePreferences(Long userId, PreferenceUpdateDto request) {
        NotificationPreference preference = notificationPreferenceRepo.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("Preference not found"));
        preference.updateEmailPreference(request.isEmailEnabled());
        preference.updatePushPreference(request.isPushEnabled());
        notificationPreferenceRepo.save(preference);
        return new PreferenceResponseDto(preference.isEmailEnabled(), preference.isPushEnabled());
    }
    
}
