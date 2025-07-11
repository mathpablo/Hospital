package com.example.hospital.dto;

import com.example.hospital.model.Room;
import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.stream.Collectors;


@Data
public class RoomDto {
    private Long id;
    private String codigo;
    private Long alaId;
    private String status;
    private String specialty;
    private List<LeitoDto> leitos;

    public RoomDto (){}
    public RoomDto(Room room) {
        this.id = room.getId();
        this.codigo = room.getCodigo();

        if (room.getAla() != null) {
            this.alaId = room.getAla().getId();

            if (room.getAla().getSpecialty() != null) {
                this.specialty = room.getAla().getSpecialty().name();
            }
        }

        if (room.getStatus() != null) {
            this.status = room.getStatus().name();
        }

        if (room.getLeitos() != null) {
            this.leitos = room.getLeitos().stream()
                    .map(LeitoDto::new)
                    .collect(Collectors.toList());
        }
    }
}

