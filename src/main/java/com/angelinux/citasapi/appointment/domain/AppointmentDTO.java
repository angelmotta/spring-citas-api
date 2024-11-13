package com.angelinux.citasapi.appointment.domain;

import java.time.Instant;
import java.time.OffsetDateTime;

public record AppointmentDTO(
        Long id,
        String firstName,
        String lastName,
        String dni,
        Integer specialtyId,
        OffsetDateTime appointmentDateTime,
        Instant createdAt
)
{ }
