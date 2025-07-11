package com.example.hospital.projection;

import com.example.hospital.Enum.Specialty;

public interface AlaProjection {
    Specialty getSpecialty();
    Long getHospitalId();
}
