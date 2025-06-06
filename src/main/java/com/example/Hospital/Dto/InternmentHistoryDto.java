package com.example.Hospital.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InternmentHistoryDto {
    private String patientName;
    private String specialty;
    private String hospitalName;
    private LocalDateTime admissionDate;
    private LocalDateTime dischargeDate;

    public InternmentHistoryDto(String patientName, String specialty, LocalDateTime admissionDate, LocalDateTime dischargeDate){
        this.patientName = patientName;
        this.specialty = specialty;
        this.hospitalName = hospitalName;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
    }

}
