package com.example.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomPatientResponseDto {
    private Long leitoId;
    private String leitoCodigo;
    private String status;
    private String specialty;
    private Long roomId;
    private String roomCodigo;
}
