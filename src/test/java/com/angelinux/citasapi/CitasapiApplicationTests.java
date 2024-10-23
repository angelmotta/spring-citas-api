package com.angelinux.citasapi;

import com.angelinux.citasapi.appointment.domain.AppointmentRequestDTO;
import com.angelinux.citasapi.appointment.domain.Appointment;
import com.angelinux.citasapi.appointment.AppointmentRepository;
import com.angelinux.citasapi.specialty.SpecialtyRepository;
import com.angelinux.citasapi.specialty.domain.Specialty;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
    @Autowired
    private SpecialtyRepository specialtyRepository;

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
		appointmentRepository.deleteAll();
	}

	@Test
	void shouldReturnAnAppointmentWhenItExists() {
		var specialtyResponse = specialtyRepository.findById(1);
		if (specialtyResponse.isEmpty()) {
			// should not happen
			throw new RuntimeException("Specialty not found");
		}
		Specialty specialty = specialtyResponse.get();
		var appointmentSaved = appointmentRepository.save(new Appointment(null, "Angel", "Motta", "42685123", specialty.getId()));
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
		AppointmentRequestDTO newAppointmentRequest = new AppointmentRequestDTO("Angel", "Motta", "42685123", 2);
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
	void shouldNotCreateANewAppointmentWithInvalidRequest() {
		// Make a POST request using an invalid request body
		AppointmentRequestDTO newAppointmentRequest = new AppointmentRequestDTO(null, "Motta", "42685123", 2);
		ResponseEntity<Void> createResponseReceived = restTemplate.postForEntity("/api/appointments", newAppointmentRequest, Void.class);
		assertThat(createResponseReceived.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void shouldReturnAppointmentList() {
		// Seed data for database
		// Get Specialties
		var specialty1 = specialtyRepository.findById(1);
		var specialty2 = specialtyRepository.findById(2);
		var specialty3 = specialtyRepository.findById(3);
		if (specialty1.isEmpty() || specialty2.isEmpty() || specialty3.isEmpty()) {
			// should not happen
			throw new RuntimeException("Specialty not found");
		}
		List<Appointment> listAppointments = new ArrayList<>(
				List.of(new Appointment(null, "Angel", "Motta", "42685123", specialty1.get().getId()),
						new Appointment(null, "Angel", "Motta", "42685123", specialty3.get().getId()),
						new Appointment(null, "Angel", "Motta", "42685123", specialty2.get().getId()))
		);

		appointmentRepository.save(listAppointments.get(0));
		appointmentRepository.save(listAppointments.get(1));
		appointmentRepository.save(listAppointments.get(2));

		// Verify HTTP GET: this should receive a List<Appointment>
		ResponseEntity<String> response = restTemplate.getForEntity("/api/appointments", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext doc = JsonPath.parse(response.getBody());
		int lenList = doc.read("$.data.length()");
		assertThat(lenList).isEqualTo(listAppointments.size());

		JSONArray listSpecialties = doc.read("$.data..specialtyId");
		Number[] expectedSpecialties = listAppointments.stream()
											.map(Appointment::getSpecialtyId)
											.toArray(Number[]::new);
		assertThat(listSpecialties).containsExactlyInAnyOrder((Object[]) expectedSpecialties);
	}

	@Test
	void shouldReturnPageOfAppointments() {
		// Seed data for database
		// Get Specialties
		var specialty1 = specialtyRepository.findById(1);
		var specialty3 = specialtyRepository.findById(3);
		var specialty4 = specialtyRepository.findById(4);
		if (specialty1.isEmpty() || specialty3.isEmpty() || specialty4.isEmpty()) {
			// should not happen
			throw new RuntimeException("Specialty not found");
		}

		List<Appointment> listAppointments = new ArrayList<>(
				List.of(new Appointment(null, "Angel", "Motta", "42685123", specialty1.get().getId()),
						new Appointment(null, "Angel", "Motta", "42685123", specialty3.get().getId()),
						new Appointment(null, "Angel", "Motta", "42685123", specialty4.get().getId()))
		);

		appointmentRepository.save(listAppointments.get(0));
		appointmentRepository.save(listAppointments.get(1));
		appointmentRepository.save(listAppointments.get(2));

		// HTTP GET request to receive a Page of Appointments (idx, size)
		ResponseEntity<String> response = restTemplate.getForEntity("/api/appointments?page=0&size=1", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray page = documentContext.read("$.data[*]");
		assertThat(page.size()).isEqualTo(1); // size = 1
	}

	/*
	@Test
	void shouldReturnSortedPageOfAppointments() {
		// Seed data for database
		// Get Specialties
		var specialty1 = specialtyRepository.findById(1);
		var specialty3 = specialtyRepository.findById(3);
		var specialty4 = specialtyRepository.findById(4);
		if (specialty1.isEmpty() || specialty3.isEmpty() || specialty4.isEmpty()) {
			// should not happen
			throw new RuntimeException("Specialty not found");
		}

		List<Appointment> listAppointments = new ArrayList<>(
				List.of(new Appointment(null, "Angel", "Motta", "42685123", specialty3.get().getId()),
						new Appointment(null, "Angel", "Motta", "42685123", specialty1.get().getId()),
						new Appointment(null, "Angel", "Motta", "42685123", specialty4.get().getId()))
		);

		appointmentRepository.save(listAppointments.get(0));
		appointmentRepository.save(listAppointments.get(1));
		appointmentRepository.save(listAppointments.get(2));

		// HTTP GET request with Page size and Sort parameters
		ResponseEntity<String> response = restTemplate.getForEntity("/api/appointments?page=0&size=1&sort=specialtyId,desc", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray read = documentContext.read("$.data[*]");
		assertThat(read.size()).isEqualTo(1); // size = 1

		// Verify correct sorting
		int specialty = documentContext.read("$[0].specialtyId");
		assertThat(specialty).isEqualTo(4);
	}*/

	@Test
	void shouldReturnSortedPageOfAppointmentsUsingDefaultsParameters() {
		// Seed data for database
		// Get Specialties
		var specialty1 = specialtyRepository.findById(1);
		var specialty3 = specialtyRepository.findById(3);
		var specialty4 = specialtyRepository.findById(4);
		if (specialty1.isEmpty() || specialty3.isEmpty() || specialty4.isEmpty()) {
			// should not happen
			throw new RuntimeException("Specialty not found");
		}
		List<Appointment> listAppointments = new ArrayList<>(
				List.of(new Appointment(null, "Angel", "Motta", "42685123", specialty3.get().getId()),
						new Appointment(null, "Angel", "Motta", "42685123", specialty1.get().getId()),
						new Appointment(null, "Angel", "Motta", "42685123", specialty4.get().getId()))
		);

		var app1 = appointmentRepository.save(listAppointments.get(0));
		var app2 = appointmentRepository.save(listAppointments.get(1));
		var app3 = appointmentRepository.save(listAppointments.get(2));

		// HTTP GET request with No parameters about page size and soring
		// API will be using defaults: sort=specialties,asc)
		ResponseEntity<String> response = restTemplate.getForEntity("/api/appointments", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray pageReceived = documentContext.read("$.data[*]");
		assertThat(pageReceived.size()).isEqualTo(3); // Default size = 10

		// Pagination metadata verification
		Long totalAppointments = documentContext.read("$.totalItems", Long.class);
		assertThat(totalAppointments).isEqualTo(3L);

		// Verify correct sorting
		JSONArray idAppointments = documentContext.read("$.data..id");
		// Convert JSONArray elements to Long
		List<Long> actualAppointmentsId = new ArrayList<>();
		for (Object id : idAppointments) {
			actualAppointmentsId.add(Long.valueOf(id.toString())); // Convert each id to Long
		}

		assertThat(actualAppointmentsId).containsExactly(app1.getId(), app2.getId(), app3.getId()); // ordered ASC
	}

	@Test
	void shouldUpdateAnExistingAppointment() {
		// Pre-conditions
		var specialty1 = specialtyRepository.findById(1);
		if (specialty1.isEmpty()) {
			// should not happen
			throw new RuntimeException("Specialty not found");
		}
		var theAppointment = new Appointment(null, "Angel", "Motta", "42685123", specialty1.get().getId());
		var existingAppointment = appointmentRepository.save(theAppointment);

		// Update existing appointment (update speciality from 1 to 5)
		var updatedAppointment = new AppointmentRequestDTO("Angel", "Motta", "42685123", 4);
		HttpEntity<AppointmentRequestDTO> requestEntity = new HttpEntity<>(updatedAppointment);
		ResponseEntity<Void> response = restTemplate
											.exchange("/api/appointments/" + existingAppointment.getId(), HttpMethod.PUT, requestEntity, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		// Verify reading your update
		ResponseEntity<String> getResponse = restTemplate
												.getForEntity("/api/appointments/" + existingAppointment.getId(), String.class);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		Number specialtyId = documentContext.read("$.specialtyId");
		assertThat(specialtyId).isEqualTo(4);
	}

	@Test
	void shouldNotUpdateExistingAppointmentWithInvalidRequest() {
		// Pre-conditions
		var specialty1 = specialtyRepository.findById(1);
		if (specialty1.isEmpty()) {
			// should not happen
			throw new RuntimeException("Specialty not found");
		}
		var theAppointment = new Appointment(null, "Angel", "Motta", "42685123", specialty1.get().getId());
		var existingAppointment = appointmentRepository.save(theAppointment);

		// Try to update existing appointment (update speciality from 1 to 4)
		var updatedAppointment = new AppointmentRequestDTO(null, "Motta", "42685123", 4);
		HttpEntity<AppointmentRequestDTO> requestEntity = new HttpEntity<>(updatedAppointment);
		ResponseEntity<Void> response = restTemplate
											.exchange("/api/appointments/" + existingAppointment.getId(),
													HttpMethod.PUT, requestEntity,
													Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void shouldNotUpdateAnAppointmentDoesNotExist() {
		// Creation of a new appointment using PUT is now allowed
		// Try to update an appointment does not exist
		var updatedAppointment = new AppointmentRequestDTO("Angel", "Motta", "42685123", 4);
		HttpEntity<AppointmentRequestDTO> requestEntity = new HttpEntity<>(updatedAppointment);
		ResponseEntity<Void> response = restTemplate
				.exchange("/api/appointments/1111", HttpMethod.PUT, requestEntity, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldDeleteExistingAppointment() {
		// Pre-conditions
		var specialty1 = specialtyRepository.findById(1);
		if (specialty1.isEmpty()) {
			// should not happen
			throw new RuntimeException("Specialty not found");
		}
		var theAppointment = new Appointment(null, "Angel", "Motta", "42685123", specialty1.get().getId());
		var existingAppointment = appointmentRepository.save(theAppointment);

		ResponseEntity<Void> response = restTemplate
											.exchange("/api/appointments/" + existingAppointment.getId(), HttpMethod.DELETE, null, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		// Verify resource is actually deleted
		ResponseEntity<String> getResponse = restTemplate
												.getForEntity("/api/appointments/" + existingAppointment.getId(), String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldNotDeleteAnAppointmentThatDoesNotExist() {
		ResponseEntity<Void> response = restTemplate
											.exchange("/api/appointments/1111", HttpMethod.DELETE, null, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
}
