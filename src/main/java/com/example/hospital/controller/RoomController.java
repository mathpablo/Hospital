package com.example.hospital.controller;

import com.example.hospital.dto.RoomDto;
import com.example.hospital.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<RoomDto>> listarQuartosDisponiveis(){
        List<RoomDto> rooms = roomService.listarQuartosComLeitosDisponiveis();
        return ResponseEntity.ok(rooms);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirQuarto(@PathVariable Long id) {
        try {
            roomService.deletarRoom(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}

