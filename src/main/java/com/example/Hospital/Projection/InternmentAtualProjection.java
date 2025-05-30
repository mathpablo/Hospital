package com.example.Hospital.Projection;

import com.example.Hospital.Enum.Specialty;

import java.time.LocalDateTime;

public interface InternmentAtualProjection {
    String getNomePaciente();
    Specialty getSpecialty();
    LocalDateTime getDataInternamento();
    Long getDiasInternado();
}
