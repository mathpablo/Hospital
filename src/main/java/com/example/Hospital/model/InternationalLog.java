package com.example.Hospital.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class InternationalLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "leito_id")
    private Leito leito;

    @ManyToOne
    @JoinColumn(name = "pacient_id")
    private Patient patient;

    private LocalDateTime dateInternamento;
    private LocalDateTime dataAlta;
}
