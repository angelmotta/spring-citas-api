package com.angelinux.citasapi.specialty.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("specialties")
public class Specialty {

    @Id
    private Integer id;

    private String specialtyName;

    private Instant createdAt;

    public Specialty() { }

    public Specialty(Integer id, String specialtyName, Instant createdAt) {
        this.id = id;
        this.specialtyName = specialtyName;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Specialty{" +
                "id=" + id +
                ", specialtyName='" + specialtyName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
