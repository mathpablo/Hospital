package com.example.Hospital.Dto;

import com.example.Hospital.Enum.Specialty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class AlaCreateDto {
    @NotNull
    private Specialty specialty;

    @Min(1)
    private int quantidadeQuartos;

    @Min(1)
    private int quantidadeLeitosPorQuartos;

    @NotNull
    private Long hospitalId;


}
