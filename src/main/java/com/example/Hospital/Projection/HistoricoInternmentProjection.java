package com.example.Hospital.Projection;

import com.example.Hospital.Enum.Specialty;

import java.time.LocalDateTime;

public interface HistoricoInternmentProjection {
    String getPatientName();
    Specialty getSpecialty();
    String getHospitalName();
    LocalDateTime getAdmissionDate();
    LocalDateTime getDischargeDate();

}
