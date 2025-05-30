package com.example.Hospital.Controller;

import com.example.Hospital.Dto.PatientDto;
import com.example.Hospital.Service.PatientService;
import com.example.Hospital.model.Patient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pacientes")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/create")
    public ResponseEntity<Patient> criarPaciente(@RequestBody @Valid PatientDto patientDto){
        Patient patient = new Patient();
        patient.setName(patientDto.getName());
        patient.setDataNascimento(patientDto.getDataNascimento());

        Patient savePatient = patientService.save(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(savePatient);
    }
}
