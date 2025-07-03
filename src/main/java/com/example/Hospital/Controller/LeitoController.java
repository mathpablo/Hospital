package com.example.Hospital.Controller;

import com.example.Hospital.Dto.QuantidadeLeitoLivreDto;
import com.example.Hospital.Service.LeitoService;
import com.example.Hospital.model.Leito;
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
