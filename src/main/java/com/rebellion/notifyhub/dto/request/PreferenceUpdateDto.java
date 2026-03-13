package com.rebellion.notifyhub.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreferenceUpdateDto {

    @NotNull(message = "Email preference status is required")
    private Boolean emailEnabled;

    @NotNull(message = "Push preference status is required")
    private Boolean pushEnabled;
}
