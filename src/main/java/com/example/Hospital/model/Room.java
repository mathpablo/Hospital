package com.example.Hospital.model;

import com.example.Hospital.Enum.Status;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "ala_id")
    private Ala ala;


}
