package com.angelinux.citasapi;


import com.angelinux.citasapi.appointment.domain.AppointmentDetailsDTO;
import com.angelinux.citasapi.appointment.domain.AppointmentRequestDTO;
import com.angelinux.citasapi.appointment.domain.AppointmentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class AppointmentJsonTest {

    @Autowired
    private JacksonTester<AppointmentDTO> jsonTesterResponse;

    @Autowired
    private JacksonTester<AppointmentRequestDTO> jsonTesterRequest;

    @Autowired
    private JacksonTester<AppointmentDetailsDTO[]> jsonTesterList;

    private AppointmentDetailsDTO[] appointments;

    @BeforeEach
    void setUp() {
        // This will work because it's in ISO-8601 format
        Instant createdAt1 = Instant.parse("2024-11-03T01:47:01.291402Z");
        Instant createdAt2 = Instant.parse("2024-11-03T01:48:01.291402Z");
        Instant createdAt3 = Instant.parse("2024-11-03T01:49:01.291402Z");

        OffsetDateTime appointmentDateTime1 = OffsetDateTime.parse("2024-12-01T10:00:00-05:00").withOffsetSameInstant(ZoneOffset.UTC);
        OffsetDateTime appointmentDateTime2 = OffsetDateTime.parse("2024-12-02T10:00:00-05:00").withOffsetSameInstant(ZoneOffset.UTC);
        OffsetDateTime appointmentDateTime3 = OffsetDateTime.parse("2024-12-03T10:00:00-05:00").withOffsetSameInstant(ZoneOffset.UTC);
        appointments = new AppointmentDetailsDTO[] {
                new AppointmentDetailsDTO(1L, "Angel", "Motta", "42685123", 1, "General", appointmentDateTime1, createdAt1),
                new AppointmentDetailsDTO(2L, "Angel", "Motta", "42685123", 3, "Pediatría", appointmentDateTime2, createdAt2),
                new AppointmentDetailsDTO(3L, "Angel", "Motta", "42685123", 4, "Psicología", appointmentDateTime3, createdAt3)
        };
    }

    @Test
    void AppointmentResponseSerializationTest() throws IOException {
        Instant createdAt1 = Instant.parse("2024-11-03T01:47:01.291402Z");
        OffsetDateTime appointmentDateTime = OffsetDateTime.parse("2024-12-01T10:00:00-05:00").withOffsetSameInstant(ZoneOffset.UTC);
        AppointmentDTO newAppointment = new AppointmentDTO(99L, "Angel", "Motta", "42685123", 1, appointmentDateTime, createdAt1);

        assertThat(jsonTesterResponse.write(newAppointment)).isStrictlyEqualToJson("singleAppointment.json");

        assertThat(jsonTesterResponse.write(newAppointment)).hasJsonPathNumberValue("@.id");
        assertThat(jsonTesterResponse.write(newAppointment)).extractingJsonPathNumberValue("@.id").isEqualTo(99);

        assertThat(jsonTesterResponse.write(newAppointment)).hasJsonPathStringValue("@.firstName");
        assertThat(jsonTesterResponse.write(newAppointment)).extractingJsonPathStringValue("@.firstName").isEqualTo("Angel");

        assertThat(jsonTesterResponse.write(newAppointment)).hasJsonPathStringValue("@.lastName");
        assertThat(jsonTesterResponse.write(newAppointment)).extractingJsonPathStringValue("@.lastName").isEqualTo("Motta");

        assertThat(jsonTesterResponse.write(newAppointment)).hasJsonPathNumberValue("@.specialtyId");
        assertThat(jsonTesterResponse.write(newAppointment)).extractingJsonPathNumberValue("@.specialtyId").isEqualTo(1);

        assertThat(jsonTesterResponse.write(newAppointment)).hasJsonPathStringValue("@.appointmentDateTime");
        assertThat(jsonTesterResponse.write(newAppointment)).extractingJsonPathStringValue("@.appointmentDateTime").isEqualTo("2024-12-01T15:00:00Z");
    }

    @Test
    void CreateAppointmentRequestDeserialization() throws IOException {
        // appointmentDateTime is in ISO-8601 string format
        String createAppointmentRequest = """
                {
                    "firstName": "Angel",
                    "lastName": "Motta",
                    "dni": "42685123",
                    "specialtyId": 1,
                    "appointmentDateTime": "2024-12-01T10:00:00-05:00"
                }
                """;

        OffsetDateTime expectedDateTime = OffsetDateTime.parse("2024-12-01T10:00:00-05:00").withOffsetSameInstant(ZoneOffset.UTC);
        AppointmentRequestDTO expectedRequest = new AppointmentRequestDTO("Angel", "Motta", "42685123", 1, expectedDateTime);

        assertThat(jsonTesterRequest.parse(createAppointmentRequest)).isEqualTo(expectedRequest);
    }

    @Test
    void appointmentListSerializationTest() throws IOException {
        assertThat(jsonTesterList.write(appointments)).isStrictlyEqualToJson("listAppointments.json");
    }

    @Test
    void appointmentDeserializationTest() throws IOException {
        String inputList = """
                [
                  { "id": 1, "firstName": "Angel", "lastName": "Motta", "dni": "42685123", "specialtyId": 1, "specialtyName": "General","appointmentDateTime": "2024-12-01T10:00:00-05:00", "createdAt": "2024-11-03T01:47:01.291402Z"},
                  { "id": 2, "firstName": "Angel", "lastName": "Motta", "dni": "42685123", "specialtyId": 3, "specialtyName": "Pediatría", "appointmentDateTime": "2024-12-02T10:00:00-05:00", "createdAt": "2024-11-03T01:48:01.291402Z"},
                  { "id": 3, "firstName": "Angel", "lastName": "Motta", "dni": "42685123", "specialtyId": 4, "specialtyName": "Psicología", "appointmentDateTime": "2024-12-03T10:00:00-05:00", "createdAt": "2024-11-03T01:49:01.291402Z"}
                ]
                """;
        assertThat(jsonTesterList.parse(inputList)).isEqualTo(appointments);
    }
}
