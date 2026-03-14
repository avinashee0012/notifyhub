package com.rebellion.notifyhub.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEventRequestDto {

    @NotBlank(message = "Event type is required")
    private String eventType;

    @NotBlank(message = "Payload cannot be empty")
    private String payload;
}
