package com.example.Hospital.Controller;

import com.example.Hospital.Dto.InternmentPatientDto;
import com.example.Hospital.Enum.Specialty;
import com.example.Hospital.Service.InternmentService;
import com.example.Hospital.Service.PatientService;
import com.example.Hospital.model.InternmentLog;
import com.example.Hospital.model.Patient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internacoes")
public class InternmentController {

    private final InternmentService internmentService;
    private final PatientService patientService;

    @Autowired
    public InternmentController(InternmentService internmentService, PatientService patientService) {
        this.internmentService = internmentService;
        this.patientService = patientService;
    }

    @PostMapping("/internar")
    public ResponseEntity<InternmentLog> internarPaciente(@Valid @RequestBody InternmentPatientDto internarPacienteDTO) {
        Patient patient = patientService.buscarPorId(internarPacienteDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com id " + internarPacienteDTO.getPatientId()));

        String specialtyStr = internarPacienteDTO.getSpecialty();
        if (specialtyStr == null) {
            throw new RuntimeException("Especialidade não pode ser nula");
        }
        Specialty specialty = Specialty.valueOf(specialtyStr.toUpperCase());

        InternmentLog internmentLog = internmentService.internarPaciente(patient, specialty);

        return ResponseEntity.status(HttpStatus.CREATED).body(internmentLog);
    }

    @PutMapping("/alta/{internmentLogId}")
    public ResponseEntity<InternmentLog> darAltaPaciente(@PathVariable Long internmentLogId) {
        InternmentLog alta = internmentService.darAltaPaciente(internmentLogId);
        return ResponseEntity.ok(alta);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<InternmentLog>> listarAtivos() {
        return ResponseEntity.ok(internmentService.listarInternacoesAtivas());
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<InternmentLog>> listarPorPaciente(@PathVariable Long id) {
        List<InternmentLog> internmentLogs = internmentService.buscarPorPaciente(id);
        return ResponseEntity.ok(internmentLogs);
    }
}
