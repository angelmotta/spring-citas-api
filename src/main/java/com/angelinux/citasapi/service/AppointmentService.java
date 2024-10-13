package com.angelinux.citasapi.service;

import com.angelinux.citasapi.dto.AppointmentDTO;
import com.angelinux.citasapi.dto.CreateAppointmentRequestDTO;
import com.angelinux.citasapi.entity.Appointment;
import com.angelinux.citasapi.mapper.AppointmentMapper;
import com.angelinux.citasapi.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    public AppointmentService(AppointmentRepository arp, AppointmentMapper amp) {
        this.appointmentRepository = arp;
        this.appointmentMapper = amp;
    }

    public Optional<Appointment> getAppointment(Long idRequestedAppointment) {
        Optional<Appointment> result = appointmentRepository.findById(idRequestedAppointment);
        System.out.println("executed sql statement done");
        return result;
    }

    public AppointmentDTO createAppointment(CreateAppointmentRequestDTO requestNewAppointment) {
        Appointment theNewAppointment = appointmentMapper.toEntity(requestNewAppointment);
        Appointment savedAppointment = appointmentRepository.save(theNewAppointment);

        return appointmentMapper.toAppointmentDto(savedAppointment);
    }
}
