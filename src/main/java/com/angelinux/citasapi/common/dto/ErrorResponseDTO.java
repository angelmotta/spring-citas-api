package com.angelinux.citasapi.common.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        String message,
        String errorCode,
        LocalDateTime timestamp
) {
    public static ErrorResponseDTO of(String message, String errorCode) {
        return new ErrorResponseDTO(message, errorCode, LocalDateTime.now());
    }
}
