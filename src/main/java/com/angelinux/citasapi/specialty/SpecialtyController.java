package com.angelinux.citasapi.specialty;

import com.angelinux.citasapi.appointment.domain.AppointmentDTO;
import com.angelinux.citasapi.specialty.domain.SpecialtyDTO;
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

    // create static data for test
    private final List<SpecialtyDTO> specialties = new ArrayList<>(List.of(
            new SpecialtyDTO(1, "General"),
            new SpecialtyDTO(2, "Odontología"),
            new SpecialtyDTO(3, "Pediatría"),
            new SpecialtyDTO(4, "Psicología")
    ));

    @GetMapping("/{specialtyId}")
    public ResponseEntity<SpecialtyDTO> findById(@PathVariable Integer specialtyId) {
        if (specialtyId < 0 || specialtyId >= specialties.size()) {
            return ResponseEntity.notFound().build();
        }

        Optional<SpecialtyDTO> specialityFound = specialties.stream().filter(specialty -> specialty.id().equals(specialtyId)).findFirst();
        return specialityFound.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<SpecialtyDTO>> findAll() {
        return ResponseEntity.ok(specialties);
    }
}
