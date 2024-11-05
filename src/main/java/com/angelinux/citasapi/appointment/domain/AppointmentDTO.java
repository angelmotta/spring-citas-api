package com.angelinux.citasapi.appointment.domain;

import java.time.Instant;

public record AppointmentDTO(
        Long id,
        String firstName,
        String lastName,
        String dni,
        Integer specialtyId,
        Instant createdAt
)
{ }
