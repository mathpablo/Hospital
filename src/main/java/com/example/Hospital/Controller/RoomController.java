package com.example.Hospital.Controller;

import com.example.Hospital.Projection.RoomAvailableProjection;
import com.example.Hospital.Service.RoomService;
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
    public ResponseEntity<List<RoomAvailableProjection>>listarQuartosDisponiveis(){
        List<RoomAvailableProjection> room = roomService.listarQuartosComLeitosDisponiveis();
        return ResponseEntity.ok(room);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirQuarto(@PathVariable Long id) {
        try {
            roomService.deletarRoom(id);
            return ResponseEntity.ok("Quarto excluído com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

