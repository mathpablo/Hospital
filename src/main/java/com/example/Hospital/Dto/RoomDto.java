package com.example.Hospital.Dto;

import lombok.Data;

import java.util.List;

@Data
public class RoomDto {
    private Long id;
    private String specialty;
    private String codigo;
    private List<LeitoDto> leitos;
}
