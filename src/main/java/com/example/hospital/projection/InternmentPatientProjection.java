package com.example.hospital.projection;

import java.time.LocalDateTime;

;

public interface InternmentPatientProjection {

    String getPatientName();
    String getSpecialty();
    LocalDateTime getDataInternamento();
    Integer getDiasInternados();
}
