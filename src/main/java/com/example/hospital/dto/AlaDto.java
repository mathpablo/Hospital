package com.example.hospital.dto;

import com.example.hospital.model.Ala;
import lombok.Data;
@Data

public class AlaDto {
    private Long id;
    private String specialty;
    private String hospitalName;
    private Integer quantidadeQuartos;
    private Integer quantidadeLeitosPorQuartos;

    public AlaDto(Ala ala) {
        this.id = ala.getId();
        this.specialty = ala.getSpecialty().toString();

        if (ala.getHospital() != null) {
            this.hospitalName = ala.getHospital().getName();
        }

        this.quantidadeQuartos = ala.getQuantidadeQuartos();
        this.quantidadeLeitosPorQuartos = ala.getQuantidadeLeitosPorQuartos();
    }
}




