package com.angelinux.citasapi.controller;

import com.angelinux.citasapi.dto.AppointmentDTO;
import com.angelinux.citasapi.dto.AppointmentRequestDTO;
import com.angelinux.citasapi.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
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
    public ResponseEntity<Void> createAppointment(@Valid @RequestBody AppointmentRequestDTO createAppointmentRequest, UriComponentsBuilder ucb) {
        AppointmentDTO savedAppointmentDTO = appointmentService.createAppointment(createAppointmentRequest);
        URI locationNewAppointmentURI = ucb.path("/api/appointments/{id}").buildAndExpand(savedAppointmentDTO.id()).toUri();
        return ResponseEntity.created(locationNewAppointmentURI).build();
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> findAll(Pageable pageable) {
        Page<AppointmentDTO> page = appointmentService.findAll(pageable);
        return ResponseEntity.ok(page.getContent());
    }

    @PutMapping("/{idAppointment}")
    public ResponseEntity<Void> updateAppointment(@PathVariable Long idAppointment, @Valid @RequestBody AppointmentRequestDTO updateRequest) {
        var response = appointmentService.updateAppointment(idAppointment, updateRequest);
        if (response.isEmpty()) {
            return ResponseEntity.notFound().build(); // HTTP 404
        }
        return ResponseEntity.noContent().build(); // HTTP 204
    }

    @DeleteMapping("/{idAppointment}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long idAppointment) {
        var wasDeleted = appointmentService.deleteAppointment(idAppointment);
        if (wasDeleted) {
            return ResponseEntity.noContent().build(); // HTTP 204
        }
        return ResponseEntity.notFound().build(); // HTTP 404
    }
}
