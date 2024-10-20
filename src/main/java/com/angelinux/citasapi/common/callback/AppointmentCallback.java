package com.angelinux.citasapi.common.callback;

import com.angelinux.citasapi.appointment.domain.Appointment;
import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AppointmentCallback implements BeforeConvertCallback<Appointment> {

    @Override
    @NonNull
    public Appointment onBeforeConvert(@NonNull Appointment appointment) {
        if (appointment.getCreatedAt() == null) {
            appointment.setCreatedAt(Instant.now());
        }
        return appointment;
    }
}
