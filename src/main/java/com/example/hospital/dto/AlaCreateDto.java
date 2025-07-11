package com.example.hospital.dto;

import com.example.hospital.Enum.Specialty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class AlaCreateDto {

    @NotNull(message = "HospitalId é obrigatorio.")
    private Long hospitalId;

    @NotNull(message = "Specialty é obrigatorio.")
    private Specialty specialty;

    @Min(value = 1, message = "Deve haver ao menos 1 quarto.")
    private int quantidadeQuartos;

    @Min(value = 1, message = "Deve haver ao menos 1 leito por quarto.")
    private int quantidadeLeitosPorQuartos;
}
