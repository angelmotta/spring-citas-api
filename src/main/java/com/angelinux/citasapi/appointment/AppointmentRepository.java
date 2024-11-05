package com.angelinux.citasapi.appointment;

import com.angelinux.citasapi.appointment.domain.Appointment;
import com.angelinux.citasapi.appointment.domain.AppointmentDTO;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AppointmentRepository extends CrudRepository<Appointment, Long>, PagingAndSortingRepository<Appointment, Long> {

    @Query("SELECT a.id, a.first_name, a.last_name, a.dni, a.specialty_id, s.specialty_name, a.created_at " +
            "FROM appointments a " +
            "JOIN specialties s ON a.specialty_id = s.id " +
            "ORDER BY a.id ASC " +
            "LIMIT :limit OFFSET :offset"
    )
    List<AppointmentDTO> findAllAppointmentsDetails(String sortField, String sortDirection, int limit, int offset);
}
