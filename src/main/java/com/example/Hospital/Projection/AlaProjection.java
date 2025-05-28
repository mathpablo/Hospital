package com.example.Hospital.Projection;

import com.example.Hospital.Enum.Specialty;

public interface AlaProjection {
    Specialty getSpecialty();
    Long getHospitalId();
}
