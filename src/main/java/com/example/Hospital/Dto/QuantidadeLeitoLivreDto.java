package com.example.Hospital.Dto;

import com.example.Hospital.Enum.Specialty;
import com.example.Hospital.Projection.QuantidadeLeitoLivreProjection;
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


