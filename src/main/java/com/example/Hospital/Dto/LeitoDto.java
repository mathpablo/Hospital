package com.example.Hospital.Dto;

import com.example.Hospital.Enum.StatusLeito;
import lombok.Data;

@Data
public class LeitoDto {
    private Long id;
    private StatusLeito status;
}

