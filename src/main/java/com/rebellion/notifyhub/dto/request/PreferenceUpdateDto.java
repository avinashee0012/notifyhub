package com.rebellion.notifyhub.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PreferenceUpdateDto {
    private boolean emailEnabled;
    private boolean pushEnabled; 
}
