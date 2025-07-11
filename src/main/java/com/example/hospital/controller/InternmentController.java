package com.example.hospital.controller;

import com.example.hospital.dto.InternmentLogDto;
import com.example.hospital.dto.InternmentPatientDto;
import com.example.hospital.dto.RoomPatientResponseDto;
import com.example.hospital.model.InternmentLog;
import com.example.hospital.model.Leito;
import com.example.hospital.projection.HistoricoInternmentLeitoProjection;
import com.example.hospital.projection.HistoricoInternmentProjection;
import com.example.hospital.projection.InternmentPatientProjection;
import com.example.hospital.repository.InternmentRepository;
import com.example.hospital.service.InternmentService;
import com.example.hospital.service.LeitoService;
import com.example.hospital.service.PatientService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internar")
public class InternmentController {

    private final InternmentRepository internmentRepository;
    private final InternmentService internmentService;
    private final PatientService patientService;
    private final LeitoService leitoService;

    @Autowired
    public InternmentController(InternmentService internmentService, PatientService patientService, InternmentRepository internmentRepository, LeitoService leitoService) {
        this.internmentService = internmentService;
        this.patientService = patientService;
        this.internmentRepository = internmentRepository;
        this.leitoService = leitoService;
    }

    @PostMapping("/internar-paciente")
    public ResponseEntity<InternmentLog> internarPaciente(@Valid @RequestBody InternmentPatientDto dto) {
        InternmentLog log = internmentService.internarPaciente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(log);
    }

    @PutMapping("/alta/{internmentLogId}")
    public ResponseEntity<InternmentLog> darAltaPaciente(@PathVariable Long internmentLogId) {
        InternmentLog alta = internmentService.darAltaPaciente(internmentLogId);
        return ResponseEntity.ok(alta);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<InternmentLog>> listarInternacoesAtivas() {
        return ResponseEntity.ok(internmentService.listarInternacoesAtivas());
    }

    @GetMapping("/paciente/{id}/quarto")
    public ResponseEntity<RoomPatientResponseDto> getQuartoPaciente(@PathVariable Long id) {
        RoomPatientResponseDto dto = internmentService.getQuartoPacienteInternado(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/historico/paciente/{id}")
    public ResponseEntity<Page<HistoricoInternmentProjection>> historicoPorPaciente(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "data_internamento", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<HistoricoInternmentProjection> page = internmentService.buscarHistoricoPaciente(id, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<Object> buscarInternacaoAtiva(@PathVariable Long id) {
        return internmentService.getDetalhesInternacaoAtivaPorPaciente(id)
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente não está internado."));
    }

    @GetMapping("/ativos/por-especialidade")
    public ResponseEntity<List<InternmentPatientProjection>> listarPorEspecialidade() {
        return ResponseEntity.ok(internmentService.listarPacientesInternadosPorEspecialidade());
    }

    @GetMapping("/historico/leito/{codigoLeito}")
    public List<HistoricoInternmentLeitoProjection> getHistoricoPorLeito(@PathVariable String codigoLeito) {
        return internmentService.buscarHistoricoPorLeito(codigoLeito);
    }
}

