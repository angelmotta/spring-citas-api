package com.angelinux.citasapi.appointment.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("appointments")
public class Appointment {
    @Id
    private Long id;

    private String firstName;

    private String lastName;

    private String dni;

    private Integer specialtyId;

    @CreatedDate
    private Instant createdAt;

    public Appointment() {
    }

    public Appointment(Long id, String firstName, String lastName, String dni, Integer specialtyId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dni = dni;
        this.specialtyId = specialtyId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Integer getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(Integer specialtyId) {
        this.specialtyId = specialtyId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dni='" + dni + '\'' +
                ", specialtyId=" + specialtyId +
                '}';
    }
}
