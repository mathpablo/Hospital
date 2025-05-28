package com.example.Hospital.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HospitalDto {
    private Long id;

    @NotBlank(message = "Nome do hospital é obrigatório")
    private String name;
}
