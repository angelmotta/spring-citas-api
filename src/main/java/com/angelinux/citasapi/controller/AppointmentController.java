package com.angelinux.citasapi.controller;

import com.angelinux.citasapi.dto.AppointmentDTO;
import com.angelinux.citasapi.entity.Appointment;
import com.angelinux.citasapi.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/{requestedId}")
    public ResponseEntity<Appointment> findById(@PathVariable Long requestedId) {
        Optional<Appointment> appointmentOptional = appointmentService.getAppointment(requestedId);
        if (appointmentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(appointmentOptional.get());
    }
}
