package com.example.Hospital.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class InternmentLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "leito_id")
    private Leito leito;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column(name = "data_internamento")
    private LocalDateTime dateInternamento;

    @Column(name = "data_alta")
    private LocalDateTime dataAlta;
}
