package com.rebellion.notifyhub.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ApiResponseDto<T> {
    private final boolean success;
    private final T data;
    private final String message;
}
