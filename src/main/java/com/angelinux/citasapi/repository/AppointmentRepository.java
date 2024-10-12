package com.angelinux.citasapi.repository;

import com.angelinux.citasapi.entity.Appointment;
import org.springframework.data.repository.CrudRepository;

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
}
