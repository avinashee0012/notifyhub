package com.rebellion.notifyhub.dto.request;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEventRequestDto {

    @NotBlank(message = "Event type is required")
    private String eventType;

	@NotNull(message = "Receiver Id cannot be empty")
	private Long userId;

    @NotEmpty(message = "Payload cannot be empty")
    private Map<String, Object> payload;

	@NotBlank(message = "eventId is required")
    private Long eventId; // for idempotency
}
