package com.example.Hospital.Service;

import com.example.Hospital.Enum.Specialty;
import com.example.Hospital.Enum.StatusLeito;
import com.example.Hospital.Repository.InternationallRepository;
import com.example.Hospital.Repository.LeitoRepository;
import com.example.Hospital.model.InternationalLog;
import com.example.Hospital.model.Leito;
import com.example.Hospital.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class InternationallService {

    @Autowired
    private InternationallRepository internationallRepository;

    @Autowired
    private LeitoRepository leitoRepository;

    @Autowired
    private LeitoService leitoService;

    public InternationalLog internarPaciente(Patient patient, Specialty specialty) {
        Optional<Leito> leitoDisponivel = leitoService.buscarLeitoDisponivelPorEspecialidade(specialty);

        if (leitoDisponivel.isEmpty()) {
            throw new RuntimeException("Não há leito disponível para a especialidade " + specialty);
        }

        Leito leito = leitoDisponivel.get();
        leito.setStatus(StatusLeito.OCUPADO);

        InternationalLog internationalLog = new InternationalLog();
        internationalLog.setPatient(patient);
        internationalLog.setLeito(leito);
        internationalLog.setDateInternamento(LocalDateTime.now());
        internationalLog.setDataAlta(null);

        leitoRepository.save(leito);
        return internationallRepository.save(internationalLog);
    }

    public InternationalLog darAltaPaciente(Long internationalLogId) {
        InternationalLog internationalLog = internationallRepository.findById(internationalLogId)
                .orElseThrow(() -> new RuntimeException("Internação não encontrada."));

        if (internationalLog.getDataAlta() != null) {
            throw new RuntimeException("Paciente já teve alta.");
        }

        internationalLog.setDataAlta(LocalDateTime.now());

        Leito leito = internationalLog.getLeito();
        leito.setStatus(StatusLeito.LIVRE);

        leitoRepository.save(leito);
        return internationallRepository.save(internationalLog);
    }
}
