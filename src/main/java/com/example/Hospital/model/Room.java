package com.example.Hospital.model;

import com.example.Hospital.Enum.StatusLeito;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;

    @ManyToOne
    @JoinColumn(name = "ala_id")
    private Ala ala;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Leito> leitos = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private StatusLeito status;
}
