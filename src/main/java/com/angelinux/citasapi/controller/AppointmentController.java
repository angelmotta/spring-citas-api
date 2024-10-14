package com.angelinux.citasapi.controller;

import com.angelinux.citasapi.dto.AppointmentDTO;
import com.angelinux.citasapi.dto.CreateAppointmentRequestDTO;
import com.angelinux.citasapi.entity.Appointment;
import com.angelinux.citasapi.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/{requestedId}")
    public ResponseEntity<AppointmentDTO> findById(@PathVariable Long requestedId) {
        Optional<AppointmentDTO> appointmentOptional = appointmentService.getAppointment(requestedId);
        return appointmentOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> createAppointment(@RequestBody CreateAppointmentRequestDTO createAppointmentRequest, UriComponentsBuilder ucb) {
        AppointmentDTO savedAppointmentDTO = appointmentService.createAppointment(createAppointmentRequest);
        URI locationNewAppointmentURI = ucb.path("/api/appointments/{id}").buildAndExpand(savedAppointmentDTO.id()).toUri();
        return ResponseEntity.created(locationNewAppointmentURI).build();
    }
}
