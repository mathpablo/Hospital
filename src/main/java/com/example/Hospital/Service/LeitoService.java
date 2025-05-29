package com.example.Hospital.Service;

import com.example.Hospital.Enum.Specialty;
import com.example.Hospital.Enum.StatusLeito;
import com.example.Hospital.Repository.LeitoRepository;
import com.example.Hospital.model.Leito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LeitoService {

    @Autowired
    private LeitoRepository leitoRepository;

    public Optional<Leito> buscarLeitoDisponivelPorEspecialidade(Specialty specialty){
        return leitoRepository.findFirstByRoom_Ala_SpecialtyAndStatus(specialty, StatusLeito.LIVRE);
    }
}
