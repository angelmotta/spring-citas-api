package com.angelinux.citasapi.repository;

import com.angelinux.citasapi.entity.Appointment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AppointmentRepository extends CrudRepository<Appointment, Long>, PagingAndSortingRepository<Appointment, Long> {
}
