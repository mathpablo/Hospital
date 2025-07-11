package com.example.hospital.dto;

import com.example.hospital.Enum.Specialty;
import com.example.hospital.projection.QuantidadeLeitoLivreProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuantidadeLeitoLivreDto {
    private Specialty specialty;
    private Long quantidadeLeitoLivres;


    public QuantidadeLeitoLivreDto(QuantidadeLeitoLivreProjection projection) {
        this.specialty = projection.getSpecialty();
        this.quantidadeLeitoLivres = projection.getQuantidadeLeitosLivres();
    }
}


