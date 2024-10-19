package com.angelinux.citasapi.appointment;

import com.angelinux.citasapi.appointment.domain.AppointmentDTO;
import com.angelinux.citasapi.appointment.domain.AppointmentRequestDTO;
import com.angelinux.citasapi.appointment.domain.Appointment;
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
        theNewAppointment.setSpecialtyId(requestNewAppointment.specialtyId());

        return theNewAppointment;
    }

    public AppointmentDTO toAppointmentDto(Appointment appointment) {
        return new AppointmentDTO(
                appointment.getId(),
                appointment.getFirstName(),
                appointment.getLastName(),
                appointment.getDni(),
                appointment.getSpecialtyId()
        );
    }
}
