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
    public InternmentLog internarPaciente(Patient patient, Specialty specialty) {
        Optional<Leito> leitoDisponivel = leitoService.buscarLeitoDisponivelPorEspecialidade(specialty);

        if (leitoDisponivel.isEmpty()) {
            throw new RuntimeException("Não há leito disponível para a especialidade " + specialty);
        }

        Leito leito = leitoDisponivel.get();
        leito.setStatus(StatusLeito.OCUPADO);

        InternmentLog internmentLog = new InternmentLog();
        internmentLog.setPatient(patient);
        internmentLog.setLeito(leito);
        internmentLog.setDateInternamento(LocalDateTime.now());
        internmentLog.setDataAlta(null);

        leitoRepository.save(leito);
        return internmentRepository.save(internmentLog);
    }

    @Transactional
    public InternmentLog darAltaPaciente(Long internmentLogId) {
        InternmentLog internmentLog = internmentRepository.findById(internmentLogId)
                .orElseThrow(() -> new RuntimeException("Internação não encontrada."));

        if (internmentLog.getDataAlta() != null) {
            throw new RuntimeException("Paciente já teve alta.");
        }

        internmentLog.setDataAlta(LocalDateTime.now());

        Leito leito = internmentLog.getLeito();
        leito.setStatus(StatusLeito.LIVRE);
        leito.setPatient(null);

        leitoRepository.save(leito);
        return internmentRepository.save(internmentLog);
    }
}
