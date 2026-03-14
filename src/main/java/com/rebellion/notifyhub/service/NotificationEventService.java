package com.rebellion.notifyhub.service;

import com.rebellion.notifyhub.dto.request.NotificationEventRequestDto;

public interface NotificationEventService {
    void ingestEvent(NotificationEventRequestDto request);
}
