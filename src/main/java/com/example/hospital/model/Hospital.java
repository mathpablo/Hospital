package com.example.hospital.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

}
