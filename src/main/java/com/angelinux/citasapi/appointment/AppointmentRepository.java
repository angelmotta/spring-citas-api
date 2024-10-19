package com.angelinux.citasapi.appointment;

import com.angelinux.citasapi.appointment.domain.Appointment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AppointmentRepository extends CrudRepository<Appointment, Long>, PagingAndSortingRepository<Appointment, Long> {
}
