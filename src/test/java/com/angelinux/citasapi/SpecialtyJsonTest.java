package com.angelinux.citasapi;

import com.angelinux.citasapi.specialty.domain.SpecialtyDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class SpecialtyJsonTest {
    @Autowired
    private JacksonTester<SpecialtyDTO> jsonTester;

    private SpecialtyDTO[] specialties;

    @Autowired
    private JacksonTester<SpecialtyDTO[]> jsonTesterList;

    @BeforeEach
    void setUp() {
        specialties = new SpecialtyDTO[] {
                new SpecialtyDTO(1, "General"),
                new SpecialtyDTO(2, "Odontología"),
                new SpecialtyDTO(3, "Pediatría"),
                new SpecialtyDTO(4, "Psicología")
        };
    }

    @Test
    void SpecialtySerializationTest() throws IOException {
        SpecialtyDTO specialtyDTO = new SpecialtyDTO(1, "General");

        assertThat(jsonTester.write(specialtyDTO)).isStrictlyEqualToJson("singleSpecialty.json");

        assertThat(jsonTester.write(specialtyDTO)).hasJsonPathNumberValue("@.id");
        assertThat(jsonTester.write(specialtyDTO)).extractingJsonPathNumberValue("@.id").isEqualTo(1);

        assertThat(jsonTester.write(specialtyDTO)).hasJsonPathStringValue("@.specialtyName");
        assertThat(jsonTester.write(specialtyDTO)).extractingJsonPathStringValue("@.specialtyName").isEqualTo("General");
    }

    @Test
    void SpecialtyListSerializationTest() throws IOException {
        assertThat(jsonTesterList.write(specialties)).isStrictlyEqualToJson("listSpecialties.json");
    }
}
