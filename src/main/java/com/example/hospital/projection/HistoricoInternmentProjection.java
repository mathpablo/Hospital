package com.example.hospital.projection;

import com.example.hospital.Enum.Specialty;

import java.time.LocalDateTime;

public interface HistoricoInternmentProjection {
    String getPatientName();
    String getSpecialty();
    String getHospitalName();
    LocalDateTime getAdmissionDate();
    LocalDateTime getDischargeDate();
    String getRoomCodigo();
    String getLeitoCodigo();

}
