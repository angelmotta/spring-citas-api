package com.angelinux.citasapi.specialty;

import com.angelinux.citasapi.specialty.domain.Specialty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SpecialtyRepository extends CrudRepository<Specialty, Integer>, PagingAndSortingRepository<Specialty, Integer> {
}
