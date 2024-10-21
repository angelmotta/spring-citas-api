package com.angelinux.citasapi.specialty;

import com.angelinux.citasapi.appointment.domain.Appointment;
import com.angelinux.citasapi.appointment.domain.AppointmentDTO;
import com.angelinux.citasapi.specialty.domain.Specialty;
import com.angelinux.citasapi.specialty.domain.SpecialtyDTO;
import org.springframework.stereotype.Component;

@Component
public class SpecialtyMapper {
    public SpecialtyDTO toSpecialtyDto(Specialty specialty) {
        return new SpecialtyDTO(
                specialty.getId(),
                specialty.getSpecialtyName()
        );
    }
}
