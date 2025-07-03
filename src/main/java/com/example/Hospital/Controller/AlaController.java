package com.example.Hospital.Controller;

import com.example.Hospital.Dto.AlaCreateDto;
import com.example.Hospital.Service.AlaService;
import com.example.Hospital.model.Ala;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
