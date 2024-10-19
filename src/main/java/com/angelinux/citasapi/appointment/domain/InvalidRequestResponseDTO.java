package com.angelinux.citasapi.appointment.domain;

import java.time.LocalDateTime;
import java.util.List;

public record InvalidRequestResponseDTO(
        LocalDateTime timestamp,
        String message,
        List<String> details
) {}
