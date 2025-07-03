package com.example.Hospital.Service;

import com.example.Hospital.Dto.InternmentPatientDto;
import com.example.Hospital.Dto.RoomPatientResponseDto;
import com.example.Hospital.Enum.Specialty;
import com.example.Hospital.Enum.StatusLeito;
import com.example.Hospital.Projection.HistoricoInternmentLeitoProjection;
import com.example.Hospital.Projection.HistoricoInternmentProjection;
import com.example.Hospital.Projection.InternmentDetailProjection;
import com.example.Hospital.Projection.InternmentPatientProjection;
import com.example.Hospital.Repository.InternmentRepository;
import com.example.Hospital.Repository.LeitoRepository;
import com.example.Hospital.Repository.PatientRepository;
import com.example.Hospital.Repository.RoomRepository;
import com.example.Hospital.model.InternmentLog;
import com.example.Hospital.model.Leito;
import com.example.Hospital.model.Patient;
import com.example.Hospital.model.Room;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.print.DocFlavor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InternmentService {

    @Autowired
    private InternmentRepository internmentRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private LeitoRepository leitoRepository;

    @Autowired
    private LeitoService leitoService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private PatientRepository patientRepository;

    public void liberarLeito(Leito leito) {
        leito.setStatus(StatusLeito.LIVRE);
        leito.setPatient(null);
        leitoRepository.save(leito);

        Room room = leito.getRoom();
        if (room != null) {
            boolean todosLivres = room.getLeitos().stream()
                    .allMatch(l -> l.getStatus() == StatusLeito.LIVRE);

            if (todosLivres) {
                room.setStatus(StatusLeito.LIVRE);
                roomRepository.save(room);
            }
        }
    }

    public InternmentLog buscarPorId(Long id){
        return  internmentRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Internações não encontrada com o id:" + id));
    }


    @Transactional
    public InternmentLog internarPaciente(InternmentPatientDto dto) {
        Patient patient = patientService.buscarPorId(dto.getPatientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado com id " + dto.getPatientId()));

        Specialty specialty;
        try {
            specialty = Specialty.fromString(dto.getSpecialty());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        boolean jaInternado = internmentRepository.existsByPatientIdAndInternacaoAtiva(patient.getId());
        if (jaInternado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Paciente já está internado.");
        }

        Leito leito = leitoService.buscarLeitoDisponivelPorEspecialidade(specialty)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Nenhum leito disponível para a especialidade: " + specialty));

        leito.setStatus(StatusLeito.OCUPADO);
        leito.setSpecialty(specialty);
        leito.setPatient(patient);
        leito = leitoRepository.save(leito);

        Room room = leito.getRoom();
        if (room != null) {
            boolean todosOcupados = room.getLeitos().stream()
                    .allMatch(l -> l.getStatus() == StatusLeito.OCUPADO);

            if (todosOcupados) {
                room.setStatus(StatusLeito.OCUPADO);
                roomRepository.save(room);
            }
        }

        InternmentLog log = new InternmentLog();
        log.setPatient(patient);
        log.setLeito(leito);
        log.setDateInternamento(LocalDateTime.now());

        return internmentRepository.save(log);
    }


    @Transactional
    public InternmentLog darAltaPaciente(Long internmentLogId) {
        InternmentLog internmentLog = buscarPorId(internmentLogId);

        if (internmentLog.getDataAlta() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Paciente já recebeu alta.");
        }

        internmentLog.setDataAlta(LocalDateTime.now());

        liberarLeito(internmentLog.getLeito());

        return internmentRepository.save(internmentLog);
    }



    public List<InternmentLog> listarInternacoesAtivas() {
        return internmentRepository.findInternacoesAtivas();
    }

    public Page<HistoricoInternmentProjection> buscarHistoricoPaciente(Long patientId, Pageable pageable) {
        return internmentRepository.findHistoryByPatientId(patientId, pageable);
    }

    public RoomPatientResponseDto getQuartoPacienteInternado(Long patientId) {
        Optional<InternmentLog> internacaoAtiva = internmentRepository
                .findByPatientIdAndDataAltaIsNull(patientId);

        if (internacaoAtiva.isPresent()){
            Leito leito = internacaoAtiva.get().getLeito();
            Room room = leito.getRoom();

            return new RoomPatientResponseDto(
                    leito.getId(),
                    leito.getCodigo(),
                    leito.getStatus().toString(),
                    leito.getSpecialty().toString(),
                    room.getId(),
                    room.getCodigo()
            );
        }else {
            throw new RuntimeException("Paciente não está internado.");
        }
    }

    public Optional<InternmentDetailProjection> getDetalhesInternacaoAtivaPorPaciente(Long patientId) {
        return internmentRepository.findInternacaoAtivaDetalhesPorPaciente(patientId);
    }

    public List<InternmentPatientProjection> listarPacientesInternadosPorEspecialidade(){
        return internmentRepository.listarInternacoesAgrupadas();
    }

    public List<HistoricoInternmentLeitoProjection> buscarHistoricoPorLeito(String codigoLeito){
        return internmentRepository.buscarHistoricoPorLeito(codigoLeito);
    }

}
