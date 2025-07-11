package com.example.hospital.controller;

import com.example.hospital.dto.HospitalDto;
import com.example.hospital.service.HospitalService;
import com.example.hospital.model.Hospital;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hospitais")
public class HospitalController {

    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService){
        this.hospitalService = hospitalService;
    }

    @PostMapping("/create")
    public ResponseEntity<Hospital> criarHospital(@RequestBody @Valid Hospital hospital){
        Hospital criado = hospitalService.criarHospital(hospital);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> excluirHospital(@PathVariable Long id){
        return hospitalService.deletarHospital(id);
    }

    @GetMapping("/listar-hospitais")
    public List<HospitalDto>listar(){
        return hospitalService.listarHospitais();
    }

}
