package com.example.Hospital.Service;

import com.example.Hospital.Dto.AlaCreateDto;
import com.example.Hospital.Repository.AlaRepository;
import com.example.Hospital.Repository.HospitalRepository;
import com.example.Hospital.Repository.LeitoRepository;
import com.example.Hospital.Repository.QuartoRepository;
import com.example.Hospital.model.Ala;
import com.example.Hospital.model.Hospital;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AlaService {

    private  AlaRepository alaRepository;
    private  HospitalRepository hospitalRepository;
    private QuartoRepository quartoRepository;
    private LeitoRepository leitoRepository;

    public AlaService (AlaRepository alaRepository,
                       HospitalRepository hospitalRepository,
                       QuartoRepository quartoRepository,
                       LeitoRepository leitoRepository){
        this.alaRepository = alaRepository;
        this.hospitalRepository = hospitalRepository;
        this.quartoRepository = quartoRepository;
        this.leitoRepository = leitoRepository;
    }

    @Transactional
    public Ala criarAlaComQuartosLeitos(AlaCreateDto dto) {
        Hospital hospital = hospitalRepository.findBy(dto.getHospitalId())
                .orElsewThrow(() -> new RuntimeException("Hospital não encontrado"));
    }




}
