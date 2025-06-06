package com.example.Hospital.Controller;

import com.example.Hospital.Dto.PatientDto;
import com.example.Hospital.Service.PatientService;
import com.example.Hospital.model.Patient;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pacientes")
public class PatientController {

    private PatientService patientService;

    public PatientController(PatientService patientService){
        this.patientService = patientService;
    }

    @PostMapping("/create")
    public ResponseEntity<PatientDto> criarPaciente(@RequestBody @Valid PatientDto patientDto){
      PatientDto responseDto = patientService.create(patientDto);
      return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
