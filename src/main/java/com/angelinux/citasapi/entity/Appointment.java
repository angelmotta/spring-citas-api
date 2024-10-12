package com.angelinux.citasapi.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("appointments")
public class Appointment {
    @Id
    private Long id;

    private String firstName;

    private String lastName;

    private String dni;

    private Integer specialty;


}
