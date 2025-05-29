package com.example.Hospital.Controller;

import com.example.Hospital.Dto.AlaCreateDto;
import com.example.Hospital.Service.AlaService;
import com.example.Hospital.model.Ala;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alas")
public class AlaController {

    private AlaService alaService;

    public AlaController(AlaService alaService){
        this.alaService = alaService;
    }

    @PostMapping("/create")
    public ResponseEntity<Ala> CriarAla(@RequestBody @Valid AlaCreateDto dto){
        Ala alaCriada = alaService.criarAlaComQuartosLeitos(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(alaCriada);
    }
}
