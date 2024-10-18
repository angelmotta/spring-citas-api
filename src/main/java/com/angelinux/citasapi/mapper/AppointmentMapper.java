package com.angelinux.citasapi.mapper;

import com.angelinux.citasapi.dto.AppointmentDTO;
import com.angelinux.citasapi.dto.AppointmentRequestDTO;
import com.angelinux.citasapi.entity.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {
    public Appointment toEntity(AppointmentRequestDTO requestNewAppointment) {
        return toEntity(requestNewAppointment, null);
    }

    public Appointment toEntity(AppointmentRequestDTO requestNewAppointment, Long id) {
        Appointment theNewAppointment = new Appointment();
        if (id != null) {
            // This id comes from an Update request
            theNewAppointment.setId(id);
        }
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
