package com.angelinux.citasapi.appointment.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.OffsetDateTime;

public record AppointmentRequestDTO(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "DNI is required")
        @Pattern(regexp = "^[0-9]{8}$", message = "DNI must be 8 digits")
        String dni,

        @NotNull(message = "Specialty ID is required")
        Integer specialtyId,

        @NotNull(message = "Appointment date and time is required")
        OffsetDateTime appointmentDateTime
        )
{ }
