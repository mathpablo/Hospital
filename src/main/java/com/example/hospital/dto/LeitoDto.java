package com.example.hospital.dto;

import com.example.hospital.model.Leito;
import lombok.Data;

@Data
public class LeitoDto {
    private Long id;
    private String codigo;
    private String status;
    private String specialty;

    public LeitoDto(Leito leito) {
        this.id = leito.getId();
        this.codigo = leito.getCodigo();
        this.status = leito.getStatus() != null ? leito.getStatus().name() : null;
        this.specialty = leito.getSpecialty() != null ? leito.getSpecialty().name() : null;
    }

    public LeitoDto(){}
}
