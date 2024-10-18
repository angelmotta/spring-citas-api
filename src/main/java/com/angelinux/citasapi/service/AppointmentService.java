package com.angelinux.citasapi.service;

import com.angelinux.citasapi.dto.AppointmentDTO;
import com.angelinux.citasapi.dto.AppointmentRequestDTO;
import com.angelinux.citasapi.entity.Appointment;
import com.angelinux.citasapi.mapper.AppointmentMapper;
import com.angelinux.citasapi.repository.AppointmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public AppointmentDTO createAppointment(AppointmentRequestDTO requestNewAppointment) {
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

    public Page<AppointmentDTO> findAll(Pageable pageable) {
        System.out.println("pageSize received = " + pageable.getPageSize());
        var page = appointmentRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "specialty"))
                ));

        return page.map(appointmentMapper::toAppointmentDto);
    }

    public Optional<AppointmentDTO> updateAppointment(Long idAppointment, AppointmentRequestDTO updateAppointmentRequest) {
        // Todo: verify if appointment exists
        //var isAnExistingAppointment = appointmentRepository.existsById(idAppointment);

        Appointment updatedAppointment = appointmentMapper.toEntity(updateAppointmentRequest, idAppointment);
        var theUpdatedAppointment = appointmentRepository.save(updatedAppointment);
        return Optional.ofNullable(appointmentMapper.toAppointmentDto(theUpdatedAppointment));
    }

}
