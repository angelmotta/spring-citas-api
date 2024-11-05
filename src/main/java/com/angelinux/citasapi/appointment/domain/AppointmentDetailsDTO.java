package com.angelinux.citasapi.appointment.domain;

import java.time.Instant;

public record AppointmentDetailsDTO(
        Long id,
        String firstName,
        String lastName,
        String dni,
        Integer specialtyId,
        String specialtyName,
        Instant createdAt
) { }
