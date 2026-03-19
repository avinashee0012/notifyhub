package com.rebellion.notifyhub.dto.response;

import java.time.LocalDateTime;

import com.rebellion.notifyhub.entity.enums.NotificationStatus;

import lombok.Value;

@Value // Makes the class final and fields private final
public class NotificationResponseDto {
	Long id;
	Long userId;
	String title;
	String message;
	String type;
	NotificationStatus status;
	LocalDateTime createdAt;
}
