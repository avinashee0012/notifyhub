package com.rebellion.notifyhub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponseDto<T> {
    private final boolean success;
    private final T data;
    private final String message;
}
