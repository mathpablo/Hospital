package com.example.Hospital.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InternmentPatientDto {

    @NotNull(message = "PatientId é obrigatorio.")
    private Long patientId;

    @NotBlank(message = "Specialty é obirgatorio.")
    private String specialty;
}

