package com.angelinux.citasapi.specialty;

import com.angelinux.citasapi.appointment.domain.AppointmentDTO;
import com.angelinux.citasapi.specialty.domain.SpecialtyDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/specialties")
public class SpecialtyController {
    private final SpecialtyService specialtyService;

    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @GetMapping("/{specialtyId}")
    public ResponseEntity<SpecialtyDTO> findById(@PathVariable Integer specialtyId) {
        var result = specialtyService.getSpecialty(specialtyId);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<SpecialtyDTO>> findAll(Pageable pageable) {
        var result = specialtyService.findAll(pageable);
        return ResponseEntity.ok(result.getContent());
    }
}
