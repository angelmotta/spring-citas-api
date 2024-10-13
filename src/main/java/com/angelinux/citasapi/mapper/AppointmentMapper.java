package com.angelinux.citasapi.mapper;

import com.angelinux.citasapi.dto.AppointmentDTO;
import com.angelinux.citasapi.dto.CreateAppointmentRequestDTO;
import com.angelinux.citasapi.entity.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {
    public Appointment toEntity(CreateAppointmentRequestDTO requestNewAppointment) {
        Appointment theNewAppointment = new Appointment();
        theNewAppointment.setFirstName(requestNewAppointment.firstName());
        theNewAppointment.setLastName(requestNewAppointment.lastName());
        theNewAppointment.setDni(requestNewAppointment.dni());
        theNewAppointment.setSpecialty(requestNewAppointment.specialty());

        return theNewAppointment;
    }

    public AppointmentDTO toAppointmentDto(Appointment appointment) {
        return new AppointmentDTO(
                appointment.getId(),
                appointment.getFirstName(),
                appointment.getLastName(),
                appointment.getDni(),
                appointment.getSpecialty()
        );
    }
}
