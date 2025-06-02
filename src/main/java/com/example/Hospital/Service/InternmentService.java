package com.example.Hospital.Service;

import com.example.Hospital.Enum.Specialty;
import com.example.Hospital.Enum.StatusLeito;
import com.example.Hospital.Repository.InternmentRepository;
import com.example.Hospital.Repository.LeitoRepository;
import com.example.Hospital.model.InternmentLog;
import com.example.Hospital.model.Leito;
import com.example.Hospital.model.Patient;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InternmentService {

    @Autowired
    private InternmentRepository internmentRepository;

    @Autowired
    private LeitoRepository leitoRepository;

    @Autowired
    private LeitoService leitoService;

    @Transactional
   public  InternmentLog internarPaciente(Patient patient, Specialty specialty){
        Leito leito = leitoService.buscarLeitoDisponivelPorEspecialidade(specialty)
        .orElseThrow(() -> new RuntimeException("Nenhum leito disponível para a especialidade." + specialty));

        leito.setStatus(StatusLeito.OCUPADO);
        leitoRepository.save(leito);

        InternmentLog internmentLog = new InternmentLog();
        internmentLog.setPatient(patient);
        internmentLog.setLeito(leito);
        internmentLog.setDateInternamento(LocalDateTime.now());

        return internmentRepository.save(internmentLog);
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
        leitoRepository.save(leito);

        leitoRepository.save(leito);
        return internmentRepository.save(internmentLog);
    }

    public List<InternmentLog> liatarTodos(){
        return internmentRepository.findInternacoesAtivas();
    }

    public List<InternmentLog> listarAtivos(){
        return internmentRepository.findInternacoesAtivas();
    }

    public List<InternmentLog> buscarPorPaciente(Long pacienteId) {
        return internmentRepository.findByPatientId(pacienteId);
    }

}
