package com.example.Hospital.Controller;

import com.example.Hospital.Dto.InternmentPatientDto;
import com.example.Hospital.Dto.RoomPatientResponseDto;
import com.example.Hospital.Enum.Specialty;
import com.example.Hospital.Projection.HistoricoInternmentLeitoProjection;
import com.example.Hospital.Projection.HistoricoInternmentProjection;
import com.example.Hospital.Projection.InternmentPatientProjection;
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
    public ResponseEntity<InternmentLog> internarPaciente(@Valid @RequestBody InternmentPatientDto dto) {
        InternmentLog internmentLog = internmentService.internarPaciente(dto);
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
    public ResponseEntity<RoomPatientResponseDto> getQuartoPacienteInternado(@PathVariable Long id){
        RoomPatientResponseDto response = internmentService.getQuartoPacienteInternado(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/historico/paciente/{id}")
    public ResponseEntity<Page<HistoricoInternmentProjection>> historicoInternacao(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "data_internamento", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<HistoricoInternmentProjection> page = internmentService.buscarHistoricoPaciente(id, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("paciente/{id}")
    public ResponseEntity<Object> buscarInternacoesAtivasPorPacientes(@PathVariable Long id){
        return internmentService.getDetalhesInternacaoAtivaPorPaciente(id)
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente não está internado."));
    }

    @GetMapping("/ativos/por-especialidade")
    public ResponseEntity<List<InternmentPatientProjection>> listarInternadosṔorEspecialidade(){
        return ResponseEntity.ok(internmentService.listarPacientesInternadosPorEspecialidade());
    }

    @GetMapping("/historico/leito/{codigoLeito}")
    public ResponseEntity<List<HistoricoInternmentLeitoProjection>> buscarHistoricoPorLeito(@PathVariable String codigoLeito){
        List<HistoricoInternmentLeitoProjection> historico = internmentService.buscarHistoricoPorLeito(codigoLeito);
        return ResponseEntity.ok(historico);
    }



}

