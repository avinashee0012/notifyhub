package com.rebellion.notifyhub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PreferenceResponseDto {
    private final boolean emailEnabled;
    private final boolean pushEnabled;
}
