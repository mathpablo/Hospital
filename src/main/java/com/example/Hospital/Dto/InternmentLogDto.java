package com.example.Hospital.Dto;

import com.example.Hospital.model.InternmentLog;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InternmentLogDto {
    private Long id;
    private Long leitoId;
    private Long patientId;
    private LocalDateTime dataInternamento;
    private LocalDateTime dataAlta;

    public InternmentLogDto(InternmentLog log) {
        this.id = log.getId();
        this.leitoId = log.getLeito().getId();
        this.patientId = log.getPatient().getId();
        this.dataInternamento = log.getDateInternamento();
        this.dataAlta = log.getDataAlta();
    }
}

