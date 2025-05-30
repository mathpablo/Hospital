package com.example.Hospital.Projection;

import com.example.Hospital.Enum.Specialty;

import java.time.LocalDateTime;

public interface HistoricoInternmentProjection {
    String getNomePatient();
    Specialty getSpecialty();
    LocalDateTime getDataInternamento();
    LocalDateTime getDataAlta();

}
