package com.example.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HospitalDto {
    private Long id;

    @NotBlank(message = "Nome do hospital é obrigatório")
    private String name;
}
