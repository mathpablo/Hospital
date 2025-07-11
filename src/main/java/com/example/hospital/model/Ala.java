package com.example.hospital.model;

import com.example.hospital.Enum.Specialty;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Ala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Specialty specialty;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @OneToMany(mappedBy = "ala", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Room> rooms;

    @Column(name = "quantidade_quartos")
    private Integer quantidadeQuartos;

    @Column(name = "quantidade_leitos_por_quartos")
    private Integer quantidadeLeitosPorQuartos;

}
