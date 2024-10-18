package com.angelinux.citasapi;


import com.angelinux.citasapi.dto.AppointmentRequestDTO;
import com.angelinux.citasapi.dto.AppointmentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

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
        appointments = new AppointmentDTO[] {
                new AppointmentDTO(1L, "Angel", "Motta", "42685123", 1),
                new AppointmentDTO(2L, "Angel", "Motta", "42685123", 3),
                new AppointmentDTO(3L, "Angel", "Motta", "42685123", 4)
        };
    }

    @Test
    void AppointmentResponseSerializationTest() throws IOException {
        AppointmentDTO newAppointment = new AppointmentDTO(99L, "Angel", "Motta", "42685123", 1);

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
                  { "id": 1, "firstName": "Angel", "lastName": "Motta", "dni": "42685123", "specialtyId": 1 },
                  { "id": 2, "firstName": "Angel", "lastName": "Motta", "dni": "42685123", "specialtyId": 3 },
                  { "id": 3, "firstName": "Angel", "lastName": "Motta", "dni": "42685123", "specialtyId": 4 }
                ]
                """;
        assertThat(jsonTesterList.parse(inputList)).isEqualTo(appointments);
    }
}
