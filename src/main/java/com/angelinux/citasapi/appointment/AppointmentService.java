package com.angelinux.citasapi.appointment;

import com.angelinux.citasapi.appointment.domain.AppointmentDTO;
import com.angelinux.citasapi.appointment.domain.AppointmentRequestDTO;
import com.angelinux.citasapi.appointment.domain.Appointment;
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
        var page = appointmentRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "specialtyId"))
                ));

        return page.map(appointmentMapper::toAppointmentDto);
    }

    public Optional<AppointmentDTO> updateAppointment(Long idAppointment, AppointmentRequestDTO updateAppointmentRequest) {
        var isAnExistingAppointment = appointmentRepository.existsById(idAppointment);
        if (!isAnExistingAppointment) {
            return Optional.empty();
        }

        Appointment updatedAppointment = appointmentMapper.toEntity(updateAppointmentRequest, idAppointment);
        var theUpdatedAppointment = appointmentRepository.save(updatedAppointment);
        return Optional.ofNullable(appointmentMapper.toAppointmentDto(theUpdatedAppointment));
    }

    public boolean deleteAppointment(Long idAppointment) {
        var isAnExistingAppointment = appointmentRepository.existsById(idAppointment);
        if (isAnExistingAppointment) {
            appointmentRepository.deleteById(idAppointment);
            return true;
        }
        return false;
    }

}
