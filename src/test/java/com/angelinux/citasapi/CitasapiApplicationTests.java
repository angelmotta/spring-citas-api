package com.angelinux.citasapi;

import com.angelinux.citasapi.dto.AppointmentDTO;
import com.angelinux.citasapi.dto.CreateAppointmentRequestDTO;
import com.angelinux.citasapi.entity.Appointment;
import com.angelinux.citasapi.repository.AppointmentRepository;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CitasapiApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	AppointmentRepository appointmentRepository;

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

	@BeforeEach
	void setUp() {
//		appointmentRepository.deleteAll();
	}

	@Test
	void shouldReturnAnAppointmentWhenItExists() {
		var appointmentSaved = appointmentRepository.save(new Appointment(null, "Angel", "Motta", "42685123", 1));
		Long appointmentId = appointmentSaved.getId();

		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/appointments/" + appointmentId, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		Number id = documentContext.read("$.id");
		assertThat(id.longValue()).isEqualTo(appointmentId);

		String dni = documentContext.read("$.dni");
		assertThat(dni).isEqualTo("42685123");
	}


	@Test
	void shouldNotReturnAnAppointWithUnknownId() {
		ResponseEntity<String> response = restTemplate.getForEntity("/api/appointments/1000", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody()).isBlank();
	}

	@Test
	void shouldCreateANewAppointment() {
		// Make a POST request to create a new resource
		CreateAppointmentRequestDTO newAppointmentRequest = new CreateAppointmentRequestDTO("Angel", "Motta", "42685123", 2);
		ResponseEntity<Void> createResponseReceived = restTemplate.postForEntity("/api/appointments", newAppointmentRequest, Void.class);
		assertThat(createResponseReceived.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		// Make a GET request to verify existence of new resource.
		URI locationOfNewAppointment = createResponseReceived.getHeaders().getLocation();
		ResponseEntity<String> getResponseReceived = restTemplate.getForEntity(locationOfNewAppointment, String.class);
		assertThat(getResponseReceived.getStatusCode()).isEqualTo(HttpStatus.OK);

		// Verify fields from received response in GET request
		DocumentContext documentContext = JsonPath.parse(getResponseReceived.getBody());
		Number id = documentContext.read("$.id");
		String dni = documentContext.read("$.dni");

		assertThat(id).isNotNull();
		assertThat(dni).isEqualTo("42685123");
	}

	@Test
	void shouldReturnAppointmentList() {
		// Seed data for database
		List<Appointment> listAppointments = new ArrayList<>(
				List.of(new Appointment(null, "Angel", "Motta", "42685123", 1),
						new Appointment(null, "Angel", "Motta", "42685123", 3),
						new Appointment(null, "Angel", "Motta", "42685123", 5))
		);

		var app1 = appointmentRepository.save(listAppointments.get(0));
		var app2 = appointmentRepository.save(listAppointments.get(1));
		var app3 = appointmentRepository.save(listAppointments.get(2));

		// Verify HTTP GET: this should receive a List<Appointment>
		ResponseEntity<String> response = restTemplate.getForEntity("/api/appointments", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext doc = JsonPath.parse(response.getBody());
		int lenList = doc.read("$.length()");
		assertThat(lenList).isEqualTo(listAppointments.size());

		JSONArray listSpecialties = doc.read("$..specialty");
		Number[] expectedSpecialties = listAppointments.stream()
											.map(Appointment::getSpecialty)
											.toArray(Number[]::new);
		assertThat(listSpecialties).containsExactlyInAnyOrder((Object[]) expectedSpecialties);
	}
}
