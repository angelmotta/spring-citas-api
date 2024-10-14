package com.angelinux.citasapi.service;

import com.angelinux.citasapi.dto.AppointmentDTO;
import com.angelinux.citasapi.dto.CreateAppointmentRequestDTO;
import com.angelinux.citasapi.entity.Appointment;
import com.angelinux.citasapi.mapper.AppointmentMapper;
import com.angelinux.citasapi.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    public AppointmentService(AppointmentRepository arp, AppointmentMapper amp) {
        this.appointmentRepository = arp;
        this.appointmentMapper = amp;
    }

    public Optional<AppointmentDTO> getAppointment(Long idRequestedAppointment) {
        return appointmentRepository.findById(idRequestedAppointment)
                .map(appointmentMapper::toAppointmentDto);
    }

    public AppointmentDTO createAppointment(CreateAppointmentRequestDTO requestNewAppointment) {
        Appointment theNewAppointment = appointmentMapper.toEntity(requestNewAppointment);
        Appointment savedAppointment = appointmentRepository.save(theNewAppointment);

        return appointmentMapper.toAppointmentDto(savedAppointment);
    }

    public List<AppointmentDTO> findAll() {
        Iterable<Appointment> appointments = appointmentRepository.findAll();
        return StreamSupport.stream(appointments.spliterator(), false)
                .map(appointmentMapper::toAppointmentDto)
                .collect(Collectors.toList());
    }
}
