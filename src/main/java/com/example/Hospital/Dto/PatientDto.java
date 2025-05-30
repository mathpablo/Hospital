package com.example.Hospital.Dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientDto {
    private Long id;
    private String name;
    private LocalDate dataNascimento;
}
