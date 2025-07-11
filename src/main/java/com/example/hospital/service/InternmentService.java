package com.example.hospital.service;

import com.example.hospital.dto.InternmentPatientDto;
import com.example.hospital.dto.RoomPatientResponseDto;
import com.example.hospital.Enum.Specialty;
import com.example.hospital.Enum.StatusLeito;
import com.example.hospital.model.InternmentLog;
import com.example.hospital.model.Leito;
import com.example.hospital.model.Patient;
import com.example.hospital.model.Room;
import com.example.hospital.projection.HistoricoInternmentLeitoProjection;
import com.example.hospital.projection.HistoricoInternmentProjection;
import com.example.hospital.projection.InternmentDetailProjection;
import com.example.hospital.projection.InternmentPatientProjection;
import com.example.hospital.repository.InternmentRepository;
import com.example.hospital.repository.LeitoRepository;
import com.example.hospital.repository.PatientRepository;
import com.example.hospital.repository.RoomRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InternmentService {

    @Autowired private InternmentRepository internmentRepository;
    @Autowired private PatientService patientService;
    @Autowired private LeitoService leitoService;
    @Autowired private LeitoRepository leitoRepository;
    @Autowired private RoomRepository roomRepository;
    @Autowired private PatientRepository patientRepository;

    public InternmentLog buscarPorId(Long id) {
        return internmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Internação não encontrada com o ID: " + id));
    }

    public List<HistoricoInternmentLeitoProjection> buscarHistoricoPorLeito(String codigoLeito) {
        return internmentRepository.buscarHistoricoPorLeito(codigoLeito);
    }

    @Transactional
    public InternmentLog internarPaciente(InternmentPatientDto dto) {
        Patient patient = patientService.buscarPorId(dto.getPatientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado com ID: " + dto.getPatientId()));

        Specialty specialty = parseSpecialty(dto.getSpecialty());

        if (internmentRepository.existsByPatientIdAndInternacaoAtiva(patient.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Paciente já está internado.");
        }

        Leito leito = leitoService.buscarLeitoDisponivelPorEspecialidade(specialty)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum leito disponível para a especialidade: " + specialty));

        ocuparLeito(leito, patient, specialty);

        InternmentLog log = new InternmentLog();
        log.setPatient(patient);
        log.setLeito(leito);
        log.setDateInternamento(LocalDateTime.now());

        return internmentRepository.save(log);
    }

    @Transactional
    public InternmentLog darAltaPaciente(Long internmentLogId) {
        InternmentLog log = buscarPorId(internmentLogId);

        if (log.getDataAlta() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Paciente já recebeu alta.");
        }

        log.setDataAlta(LocalDateTime.now());
        liberarLeito(log.getLeito());

        return internmentRepository.save(log);
    }

    public List<InternmentLog> listarInternacoesAtivas() {
        return internmentRepository.findInternacoesAtivas();
    }

    public Page<HistoricoInternmentProjection> buscarHistoricoPaciente(Long patientId, Pageable pageable) {
        return internmentRepository.findHistoryByPatientId(patientId, pageable);
    }

    public RoomPatientResponseDto getQuartoPacienteInternado(Long patientId) {
        InternmentLog log = internmentRepository.findByPatientIdAndDataAltaIsNull(patientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não está internado."));

        Leito leito = log.getLeito();
        Room room = leito.getRoom();

        return new RoomPatientResponseDto(
                leito.getId(),
                leito.getCodigo(),
                leito.getStatus().toString(),
                leito.getSpecialty().toString(),
                room.getId(),
                room.getCodigo()
        );
    }

    public Optional<InternmentDetailProjection> getDetalhesInternacaoAtivaPorPaciente(Long patientId) {
        return internmentRepository.findInternacaoAtivaDetalhesPorPaciente(patientId);
    }

    public List<InternmentPatientProjection> listarPacientesInternadosPorEspecialidade() {
        return internmentRepository.listarInternacoesAgrupadas();
    }

    private Specialty parseSpecialty(String rawSpecialty) {
        try {
            return Specialty.fromString(rawSpecialty);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private void ocuparLeito(Leito leito, Patient patient, Specialty specialty) {
        leito.setStatus(StatusLeito.OCUPADO);
        leito.setSpecialty(specialty);
        leito.setPatient(patient);
        leitoRepository.save(leito);

        Room room = leito.getRoom();
        if (room != null && room.getLeitos().stream().allMatch(l -> l.getStatus() == StatusLeito.OCUPADO)) {
            room.setStatus(StatusLeito.OCUPADO);
            roomRepository.save(room);
        }
    }

    public void liberarLeito(Leito leito) {
        leito.setStatus(StatusLeito.LIVRE);
        leito.setPatient(null);
        leitoRepository.save(leito);

        Room room = leito.getRoom();
        if (room != null && room.getLeitos().stream().allMatch(l -> l.getStatus() == StatusLeito.LIVRE)) {
            room.setStatus(StatusLeito.LIVRE);
            roomRepository.save(room);
        }
    }
}
