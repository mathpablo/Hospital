package com.example.Hospital.Projection;

import java.time.LocalDateTime;

public interface HistoricoInternmentLeitoProjection {
    String getHospitalName();
    String getSpecialty();
    String getCodigo();
    String getPatientName();
    LocalDateTime getDataInternamento();
}
