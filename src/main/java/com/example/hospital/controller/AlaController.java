package com.example.hospital.controller;

import com.example.hospital.dto.AlaCreateDto;
import com.example.hospital.dto.AlaDto;
import com.example.hospital.dto.AtualizarAlaDto;
import com.example.hospital.service.AlaService;
import com.example.hospital.model.Ala;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alas")
public class AlaController {

    private AlaService alaService;

    public AlaController(AlaService alaService){
        this.alaService = alaService;
    }

    @PostMapping("/create")
    public ResponseEntity<Ala> criarAla(@RequestBody @Valid AlaCreateDto dto){
        Ala alaCriada = alaService.criarAlaComQuartosLeitos(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(alaCriada);
    }

    @DeleteMapping("/ala/{id}")
    public ResponseEntity<?> deletarAla(@PathVariable Long id) {
        try {
            alaService.deletarAla(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/listar-alas")
    public ResponseEntity<List<AlaDto>>listar(){
        List<AlaDto> alas = alaService.listarAlas();
        return ResponseEntity.ok(alas);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<Void> atualizarAla(@PathVariable Long id, @RequestBody AtualizarAlaDto dto) {
        alaService.atualizarEstrutura(id, dto);
        return ResponseEntity.ok().build();
    }
}
