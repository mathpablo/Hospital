package com.example.Hospital.Controller;

import com.example.Hospital.Dto.HospitalDto;
import com.example.Hospital.Service.HospitalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hospitais")
public class HospitalController {

    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService){
        this.hospitalService = hospitalService;
    }

    @PostMapping("/create")
    public ResponseEntity<HospitalDto> criarHospital(@RequestBody @Valid HospitalDto dto){
        System.out.println("Recebido: " + dto);
        HospitalDto criado = hospitalService.criarHospital(dto);
        System.out.println("Criado: " + criado);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }




}
