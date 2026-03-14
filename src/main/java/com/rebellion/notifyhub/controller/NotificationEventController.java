package com.rebellion.notifyhub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rebellion.notifyhub.dto.request.NotificationEventRequestDto;
import com.rebellion.notifyhub.service.NotificationEventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class NotificationEventController {

    private final NotificationEventService notificationEventService;

    @PostMapping
    public ResponseEntity<Void> createEvent(@RequestBody NotificationEventRequestDto request) {
        notificationEventService.ingestEvent(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
