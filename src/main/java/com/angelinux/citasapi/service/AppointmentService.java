package com.angelinux.citasapi.service;

import com.angelinux.citasapi.dto.AppointmentDTO;
import com.angelinux.citasapi.entity.Appointment;
import com.angelinux.citasapi.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Optional<Appointment> getAppointment(Long idRequestedAppointment) {
        Optional<Appointment> result = appointmentRepository.findById(idRequestedAppointment);
        return result;
    }
}
