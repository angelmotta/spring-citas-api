package com.angelinux.citasapi.specialty;

import com.angelinux.citasapi.specialty.domain.Specialty;
import com.angelinux.citasapi.specialty.domain.SpecialtyDTO;
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
public class SpecialtyService {
    private final SpecialtyRepository specialtyRepository;

    private final SpecialtyMapper specialtyMapper;

    public SpecialtyService(SpecialtyRepository specialtyRepository, SpecialtyMapper specialtyMapper) {
        this.specialtyRepository = specialtyRepository;
        this.specialtyMapper = specialtyMapper;
    }

    public Optional<SpecialtyDTO> getSpecialty(Integer specialtyId) {
        return specialtyRepository.findById(specialtyId).map(specialtyMapper::toSpecialtyDto);
    }

    public List<SpecialtyDTO> findAll() {
        Iterable<Specialty> specialties = specialtyRepository.findAll();
        return StreamSupport.stream(specialties.spliterator(), false)
                .map(specialtyMapper::toSpecialtyDto)
                .collect(Collectors.toList());
    }

    public Page<SpecialtyDTO> findAll(Pageable pageable) {
        var pageResult = specialtyRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by(Sort.Direction.ASC, "specialtyName")
                )
        );
        return pageResult.map(specialtyMapper::toSpecialtyDto);
    }
}
