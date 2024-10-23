package com.angelinux.citasapi.appointment;

import com.angelinux.citasapi.appointment.domain.*;
import com.angelinux.citasapi.common.exception.EntityNotFoundException;
import com.angelinux.citasapi.specialty.SpecialtyRepository;
import com.angelinux.citasapi.specialty.domain.Specialty;
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
    private final SpecialtyRepository specialtyRepository;

    public AppointmentService(AppointmentRepository arp, AppointmentMapper amp, SpecialtyRepository sr) {
        this.appointmentRepository = arp;
        this.appointmentMapper = amp;
        this.specialtyRepository = sr;
    }

    public Optional<AppointmentDTO> getAppointment(Long idRequestedAppointment) {
        return appointmentRepository.findById(idRequestedAppointment)
                .map(appointmentMapper::toAppointmentDto);
    }

    public AppointmentDTO createAppointment(AppointmentRequestDTO requestNewAppointment) {
        // Check existence of specialty
        Specialty specialty = specialtyRepository.findById(requestNewAppointment.specialtyId())
                .orElseThrow(() -> new EntityNotFoundException("Specialty with id " + requestNewAppointment.specialtyId() + " not found."));

        Appointment theNewAppointment = appointmentMapper.toEntity(requestNewAppointment);

        Appointment savedAppointment = appointmentRepository.save(theNewAppointment);

        return appointmentMapper.toAppointmentDto(savedAppointment);
    }

    public PaginatedResponse<AppointmentDetailsDTO> findAllAppointmentsWithDetails(Pageable pageable) {
        int limit = pageable.getPageSize();
        int offset = pageable.getPageNumber() * limit;
        Sort.Order sortFieldOrder = pageable.getSort().stream().findFirst().orElse(Sort.Order.by("first_name"));
        String sortField = sortFieldOrder.getProperty();
        String sortDirection = sortFieldOrder.getDirection().toString();

        List<AppointmentDetailsDTO> appointments = appointmentRepository.findAllAppointmentsDetails(sortField, sortDirection, limit, offset);
        Long totalItems = appointmentRepository.count();
        int totalPages = (int) Math.ceil((double) totalItems / limit);
        return new PaginatedResponse<>(appointments, pageable.getPageNumber(), totalPages, totalItems);
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
        // Check existence of requested appointment
        var isAnExistingAppointment = appointmentRepository.existsById(idAppointment);
        if (!isAnExistingAppointment) {
            return Optional.empty();
        }

        // Check existence of specialty
        Specialty specialty = specialtyRepository.findById(updateAppointmentRequest.specialtyId())
                .orElseThrow(() -> new EntityNotFoundException("Specialty with id " + updateAppointmentRequest.specialtyId() + " not found."));

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
