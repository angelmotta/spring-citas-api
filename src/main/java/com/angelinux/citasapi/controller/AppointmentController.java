package com.angelinux.citasapi.controller;

import com.angelinux.citasapi.dto.AppointmentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @GetMapping("/{requestedId}")
    public ResponseEntity<AppointmentDTO> findById(@PathVariable Long requestedId) {
        if (requestedId.equals(1000L)) {
            return ResponseEntity.notFound().build();
        }
        AppointmentDTO appointmentResponse = new AppointmentDTO(99L, "Angel", "Motta", "42685123", 1);
        return ResponseEntity.ok(appointmentResponse);
    }
}
