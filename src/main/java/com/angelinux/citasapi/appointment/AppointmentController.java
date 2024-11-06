package com.angelinux.citasapi.appointment;

import com.angelinux.citasapi.appointment.domain.AppointmentDTO;
import com.angelinux.citasapi.appointment.domain.AppointmentDetailsDTO;
import com.angelinux.citasapi.appointment.domain.AppointmentRequestDTO;
import com.angelinux.citasapi.appointment.domain.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointment Controller", description = "API operations for managing appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Operation(summary = "Find an appointment by ID")
    @GetMapping("/{requestedId}")
    public ResponseEntity<AppointmentDTO> findById(@PathVariable Long requestedId) {
        Optional<AppointmentDTO> appointmentOptional = appointmentService.getAppointment(requestedId);
        return appointmentOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new appointment")
    @PostMapping
    public ResponseEntity<Void> createAppointment(@Valid @RequestBody AppointmentRequestDTO createAppointmentRequest, UriComponentsBuilder ucb) {
        AppointmentDTO savedAppointmentDTO = appointmentService.createAppointment(createAppointmentRequest);
        URI locationNewAppointmentURI = ucb.path("/api/appointments/{id}").buildAndExpand(savedAppointmentDTO.id()).toUri();
        return ResponseEntity.created(locationNewAppointmentURI).build();
    }

    @Operation(summary = "Find all appointments")
    @GetMapping
    public ResponseEntity<PaginatedResponse<AppointmentDetailsDTO>> findAll(Pageable pageable) {
        var paginatedResponse = appointmentService.findAllAppointmentsWithDetails(pageable);
        return ResponseEntity.ok(paginatedResponse);
    }

    @Operation(summary = "Update an appointment")
    @PutMapping("/{idAppointment}")
    public ResponseEntity<Void> updateAppointment(@PathVariable Long idAppointment, @Valid @RequestBody AppointmentRequestDTO updateRequest) {
        var response = appointmentService.updateAppointment(idAppointment, updateRequest);
        if (response.isEmpty()) {
            return ResponseEntity.notFound().build(); // HTTP 404
        }
        return ResponseEntity.noContent().build(); // HTTP 204
    }

    @Operation(summary = "Delete an appointment")
    @DeleteMapping("/{idAppointment}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long idAppointment) {
        var wasDeleted = appointmentService.deleteAppointment(idAppointment);
        if (wasDeleted) {
            return ResponseEntity.noContent().build(); // HTTP 204
        }
        return ResponseEntity.notFound().build(); // HTTP 404
    }
}
