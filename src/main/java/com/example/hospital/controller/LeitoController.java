package com.example.hospital.controller;

import com.example.hospital.dto.QuantidadeLeitoLivreDto;
import com.example.hospital.service.LeitoService;
import com.example.hospital.model.Leito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/leitos")
public class LeitoController {


    @Autowired
    private LeitoService leitoService;

    @GetMapping("/quantidades-livres")
    public ResponseEntity<List<QuantidadeLeitoLivreDto>> listarQuantidadeLeitosLivres() {
        return ResponseEntity.ok(leitoService.listarQuantidadeDeLeitosLivres());
    }

    @PutMapping("/leito/{id}/status")
    public ResponseEntity<Leito> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        Leito atualizado = leitoService.atualizarStatus(id, status);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/leito/{id}")
    public ResponseEntity<String> deletarLeito(@PathVariable Long id) {
        try {
            leitoService.deletarLeito(id);
            return ResponseEntity.ok("Leito deletado com sucesso.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}
