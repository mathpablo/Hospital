package com.example.hospital.dto;

import com.example.hospital.model.InternmentLog;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class InternmentLogDto {
    private Long id;
    private Long leitoId;
    private Long patientId;

    private String dataInternamento;
    private String horaInternamento;

    private String dataAlta;
    private String horaAlta;


    public InternmentLogDto(InternmentLog log) {
        this.id = log.getId();
        this.leitoId = log.getLeito().getId();
        this.patientId = log.getPatient().getId();

        DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDateTime internamento = log.getDateInternamento();
        if (internamento != null) {
            this.dataInternamento = internamento.toLocalDate().format(dataFormatter);
            this.horaInternamento = internamento.toLocalTime().format(horaFormatter);
        }

        LocalDateTime alta = log.getDataAlta();
        if (alta != null) {
            this.dataAlta = alta.toLocalDate().format(dataFormatter);
            this.horaAlta = alta.toLocalTime().format(horaFormatter);
        }
    }
}

