package com.example.Hospital.Projection;

import java.time.LocalDateTime;

;

public interface InternmentPatientProjection {

    String getPatientName();
    String getSpecialty();
    LocalDateTime getDataInternamento();
    Integer getDiasInternados();
}
