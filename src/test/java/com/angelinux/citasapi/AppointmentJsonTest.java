package com.angelinux.citasapi;


import com.angelinux.citasapi.appointment.domain.AppointmentRequestDTO;
import com.angelinux.citasapi.appointment.domain.AppointmentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class AppointmentJsonTest {

    @Autowired
    private JacksonTester<AppointmentDTO> jsonTesterResponse;

    @Autowired
    private JacksonTester<AppointmentRequestDTO> jsonTesterRequest;

    @Autowired
    private JacksonTester<AppointmentDTO[]> jsonTesterList;

    private AppointmentDTO[] appointments;

    @BeforeEach
    void setUp() {
        // This will work because it's in ISO-8601 format
        Instant createdAt1 = Instant.parse("2024-11-03T01:47:01.291402Z");
        Instant createdAt2 = Instant.parse("2024-11-03T01:48:01.291402Z");
        Instant createdAt3 = Instant.parse("2024-11-03T01:49:01.291402Z");
        appointments = new AppointmentDTO[] {
                new AppointmentDTO(1L, "Angel", "Motta", "42685123", 1, createdAt1),
                new AppointmentDTO(2L, "Angel", "Motta", "42685123", 3, createdAt2),
                new AppointmentDTO(3L, "Angel", "Motta", "42685123", 4, createdAt3)
        };
    }

    @Test
    void AppointmentResponseSerializationTest() throws IOException {
        Instant createdAt1 = Instant.parse("2024-11-03T01:47:01.291402Z");
        AppointmentDTO newAppointment = new AppointmentDTO(99L, "Angel", "Motta", "42685123", 1, createdAt1);

        assertThat(jsonTesterResponse.write(newAppointment)).isStrictlyEqualToJson("singleAppointment.json");

        assertThat(jsonTesterResponse.write(newAppointment)).hasJsonPathNumberValue("@.id");
        assertThat(jsonTesterResponse.write(newAppointment)).extractingJsonPathNumberValue("@.id").isEqualTo(99);

        assertThat(jsonTesterResponse.write(newAppointment)).hasJsonPathStringValue("@.firstName");
        assertThat(jsonTesterResponse.write(newAppointment)).extractingJsonPathStringValue("@.firstName").isEqualTo("Angel");

        assertThat(jsonTesterResponse.write(newAppointment)).hasJsonPathStringValue("@.lastName");
        assertThat(jsonTesterResponse.write(newAppointment)).extractingJsonPathStringValue("@.lastName").isEqualTo("Motta");

        assertThat(jsonTesterResponse.write(newAppointment)).hasJsonPathNumberValue("@.specialtyId");
        assertThat(jsonTesterResponse.write(newAppointment)).extractingJsonPathNumberValue("@.specialtyId").isEqualTo(1);
    }

    @Test
    void CreatAppointmentRequestDeserialization() throws IOException {
        String createAppointmentRequest = """
                {
                    "firstName": "Angel",
                    "lastName": "Motta",
                    "dni": "42685123",
                    "specialtyId": 1
                }
                """;
        assertThat(jsonTesterRequest.parse(createAppointmentRequest)).isEqualTo(new AppointmentRequestDTO("Angel", "Motta", "42685123", 1));
    }

    @Test
    void appointmentListSerializationTest() throws IOException {
        assertThat(jsonTesterList.write(appointments)).isStrictlyEqualToJson("listAppointments.json");
    }

    @Test
    void appointmentDeserializationTest() throws IOException {
        String inputList = """
                [
                  { "id": 1, "firstName": "Angel", "lastName": "Motta", "dni": "42685123", "specialtyId": 1, "createdAt": "2024-11-03T01:47:01.291402Z"},
                  { "id": 2, "firstName": "Angel", "lastName": "Motta", "dni": "42685123", "specialtyId": 3, "createdAt": "2024-11-03T01:48:01.291402Z"},
                  { "id": 3, "firstName": "Angel", "lastName": "Motta", "dni": "42685123", "specialtyId": 4, "createdAt": "2024-11-03T01:49:01.291402Z"}
                ]
                """;
        assertThat(jsonTesterList.parse(inputList)).isEqualTo(appointments);
    }
}
