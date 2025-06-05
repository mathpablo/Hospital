package com.example.Hospital.Controller;

import com.example.Hospital.Dto.InternmentPatientDto;
import com.example.Hospital.Enum.Specialty;
import com.example.Hospital.Projection.HistoricoInternmentProjection;
import com.example.Hospital.Service.InternmentService;
import com.example.Hospital.Service.PatientService;
import com.example.Hospital.model.InternmentLog;
import com.example.Hospital.model.Patient;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

       Specialty specialty;
       try{
           specialty = Specialty.fromString(internarPacienteDTO.getSpecialty());
       }catch (IllegalArgumentException e){
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
       }

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

    @GetMapping("/paciente/{id}/quarto")
    public ResponseEntity<String> getQuartoPacienteInternado(@PathVariable Long id){
        String room = internmentService.getQuartoPacienteInternado(id);
        return ResponseEntity.ok(room);
    }

    @GetMapping("/historico/paciente/{id}")
    public ResponseEntity<Page<HistoricoInternmentProjection>> historicoInternacao(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "data_internamento", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<HistoricoInternmentProjection> page = internmentService.buscarHistoricoPaciente(id, pageable);
        return ResponseEntity.ok(page);
    }



}

