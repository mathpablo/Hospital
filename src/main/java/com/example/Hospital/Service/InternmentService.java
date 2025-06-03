package com.example.Hospital.Service;

import com.example.Hospital.Enum.Specialty;
import com.example.Hospital.Enum.StatusLeito;
import com.example.Hospital.Projection.HistoricoInternmentProjection;
import com.example.Hospital.Repository.InternmentRepository;
import com.example.Hospital.Repository.LeitoRepository;
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

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InternmentService {

    @Autowired
    private InternmentRepository internmentRepository;

    @Autowired
    private LeitoRepository leitoRepository;

    @Autowired
    private LeitoService leitoService;

    @Autowired
    private RoomRepository roomRepository;

    @Transactional
    public InternmentLog internarPaciente(Patient patient, Specialty specialty) {
        boolean jaInternado = internmentRepository.existsByPatientIdAndInternacaoAtiva(patient.getId());
        System.out.println("Paciente já internado? " + jaInternado);
        if (jaInternado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Paciente já está internado e não pode ser internado novamente.");
        }

        Leito leito = leitoService.buscarLeitoDisponivelPorEspecialidade(specialty)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum leito disponível para a especialidade: " + specialty));

        leito.setStatus(StatusLeito.OCUPADO);
        leito.setPatient(patient);
        leito = leitoRepository.save(leito);

        Room room = leito.getRoom();
        if (room != null) {
            room.setStatus(StatusLeito.OCUPADO);
            roomRepository.save(room);
        }

        InternmentLog internmentLog = new InternmentLog();
        internmentLog.setPatient(patient);
        internmentLog.setLeito(leito);
        internmentLog.setDateInternamento(LocalDateTime.now());

        System.out.println("Salvando internação para paciente " + patient.getName());
        InternmentLog saved = internmentRepository.save(internmentLog);
        internmentRepository.flush();
        InternmentLog check = internmentRepository.findById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Internação salva não encontrada!"));

        System.out.println("Internação salva confirmada no banco: " + check);
        return saved;

    }



    @Transactional
    public InternmentLog darAltaPaciente(Long internmentLogId) {
        InternmentLog internmentLog = internmentRepository.findById(internmentLogId)
                .orElseThrow(() -> new RuntimeException("Internação não encontrada."));

        if (internmentLog.getDataAlta() != null) {
            throw new RuntimeException("Paciente já recebeu alta.");
        }

        internmentLog.setDataAlta(LocalDateTime.now());

        Leito leito = internmentLog.getLeito();
        leito.setStatus(StatusLeito.LIVRE);
        leito.setPatient(null);
        leitoRepository.save(leito);

        Room room = leito.getRoom();
        if (room != null){
            boolean todosLivres = room.getLeitos().stream()
            .allMatch(l -> l.getStatus() == StatusLeito.LIVRE);

            if (todosLivres){
                room.setStatus(StatusLeito.LIVRE);
                roomRepository.save(room);
            }
        }

        Leito atualizado = leitoRepository.findById(leito.getId()).orElseThrow();
        System.out.println("Leito atualizado: " + atualizado);


        return internmentRepository.save(internmentLog);
    }

    public List<InternmentLog> listarInternacoesAtivas() {
        return internmentRepository.findInternacoesAtivas();
    }

    public List<InternmentLog> buscarPorPaciente(Long pacienteId) {
        return internmentRepository.findByPatientId(pacienteId);
    }

    public Page<HistoricoInternmentProjection> buscarHistoricoPaciente(Long patientId, Pageable pageable) {
        return internmentRepository.findHistoryByPatientId(patientId, pageable);
    }
}
