package com.rebellion.notifyhub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rebellion.notifyhub.dto.request.PreferenceUpdateDto;
import com.rebellion.notifyhub.dto.response.PreferenceResponseDto;
import com.rebellion.notifyhub.service.NotificationPreferenceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/preferences")
@RequiredArgsConstructor
public class NotificationPreferenceController {
    
    private final NotificationPreferenceService notificationPreferenceService;

    @GetMapping("/{userId}")
    public ResponseEntity<PreferenceResponseDto> getNotificationPreferences(@PathVariable Long userId){
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationPreferenceService.getPreferences(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<PreferenceResponseDto> updateNotificationPreferences(@PathVariable Long userId, @RequestBody PreferenceUpdateDto request){
        return ResponseEntity.status(HttpStatus.OK).body(notificationPreferenceService.updatePreferences(userId, request));
    }
}
