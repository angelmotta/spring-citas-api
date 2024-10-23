package com.angelinux.citasapi.appointment.domain;

public record AppointmentDetailsDTO(
        Long id,
        String firstName,
        String lastName,
        String dni,
        Integer specialtyId,
        String specialtyName
) { }
