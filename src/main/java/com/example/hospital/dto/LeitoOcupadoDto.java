package com.example.hospital.dto;

import lombok.Data;

@Data
public class LeitoOcupadoDto {
    private Long leitoId;
    private String identificacao;
    private Long patientId;
    private String patientName;
}
