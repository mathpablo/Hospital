package com.example.Hospital.Controller;

import com.example.Hospital.Dto.PatientDto;
import com.example.Hospital.Service.PatientService;
import com.example.Hospital.model.Patient;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("listar-pacientes")
    public ResponseEntity<List<PatientDto>> listarPacientes() {
        List<PatientDto> pacientes = patientService.listarTodos();
        return ResponseEntity.ok(pacientes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPaciente(@PathVariable Long id) {
        patientService.deletarPaciente(id);
        return ResponseEntity.noContent().build();
    }

}
