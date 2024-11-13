package com.angelinux.citasapi.appointment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.Instant;
import java.time.OffsetDateTime;

@JsonPropertyOrder({"id", "firstName", "lastName", "dni", "specialtyId", "specialtyName", "appointmentDateTime", "createdAt"})
public record AppointmentDetailsDTO(
        Long id,
        String firstName,
        String lastName,
        String dni,
        Integer specialtyId,
        String specialtyName,
        @JsonProperty("appointmentDateTime") OffsetDateTime appointmentDatetime,
        Instant createdAt
) { }
