package com.rebellion.notifyhub.dto.response;

import lombok.Value;

@Value // Makes the class final and fields private final
public class PreferenceResponseDto {
    boolean emailEnabled;
    boolean pushEnabled;
}
