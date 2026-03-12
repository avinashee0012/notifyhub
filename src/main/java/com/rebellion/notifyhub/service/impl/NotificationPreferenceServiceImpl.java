package com.rebellion.notifyhub.service.impl;

import org.springframework.stereotype.Service;

import com.rebellion.notifyhub.dto.request.PreferenceUpdateDto;
import com.rebellion.notifyhub.dto.response.PreferenceResponseDto;
import com.rebellion.notifyhub.entity.NotificationPreference;
import com.rebellion.notifyhub.repository.NotificationPreferenceRepo;
import com.rebellion.notifyhub.repository.UserRepo;
import com.rebellion.notifyhub.service.NotificationPreferenceService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationPreferenceServiceImpl implements NotificationPreferenceService {

    private final NotificationPreferenceRepo notificationPreferenceRepo;
    private final UserRepo userRepo;

    @Override
    public PreferenceResponseDto getPreferences(Long userId) {
        if(!userRepo.existsById(userId)) throw new EntityNotFoundException("User not found");
        NotificationPreference preference = notificationPreferenceRepo.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("Preferences not found"));
        return new PreferenceResponseDto(preference.isEmailEnabled(), preference.isPushEnabled());
    }

    @Override
    public PreferenceResponseDto updatePreferences(Long userId, PreferenceUpdateDto request) {
        if(!userRepo.existsById(userId)) throw new EntityNotFoundException("User not found");
        NotificationPreference preference = notificationPreferenceRepo.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("Preferences not found"));
        preference.updateEmailPreference(request.isEmailEnabled());
        preference.updatePushPreference(request.isPushEnabled());
        notificationPreferenceRepo.save(preference);
        return new PreferenceResponseDto(preference.isEmailEnabled(), preference.isPushEnabled());
    }
    
}
