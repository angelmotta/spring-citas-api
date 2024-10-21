package com.angelinux.citasapi;

import com.angelinux.citasapi.appointment.domain.Appointment;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpecialtyApplicationTests {
    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldReturnASpecialtyWhenItExists() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/specialties/2", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id.longValue()).isEqualTo(2);

        String specialtyName = documentContext.read("$.specialtyName");
        assertThat(specialtyName).isEqualTo("Odontología");
    }

    @Test
    void shouldNotReturnASpecialtyWithUnknownId() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/specialties/9999", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(response.getBody()).isBlank();
    }

    @Test
    void shouldReturnASpecialtyList() {
        // Pre-conditions
        final int SIZELISTSPECIALTIES = 4; // todo: change this for value from repository
        // HTTP GET: this should receive a List<Appointment>
        ResponseEntity<String> response = restTemplate.getForEntity("/api/specialties", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext doc = JsonPath.parse(response.getBody());
        int lenList = doc.read("$.length()");
        assertThat(lenList).isEqualTo(SIZELISTSPECIALTIES);

        JSONArray listSpecialties = doc.read("$..id");
        assertThat(listSpecialties).containsExactlyInAnyOrder(1, 2, 3, 4);
    }
}
