package com.example.Hospital.Dto;

import com.example.Hospital.Enum.Specialty;
import lombok.Data;

@Data

public class AlaCreateDto {
    private Long hospitalId;
    private Specialty specialty;
    private int quantidadeQuartos;
    private int quantidadeLeitosPorQuartos;
}
