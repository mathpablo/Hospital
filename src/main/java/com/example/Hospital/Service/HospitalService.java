package com.example.Hospital.Service;

import com.example.Hospital.Dto.HospitalDto;
import com.example.Hospital.Repository.AlaRepository;
import com.example.Hospital.Repository.HospitalRepository;
import com.example.Hospital.model.Hospital;
import org.springframework.stereotype.Service;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final AlaRepository alaRepository;

    public HospitalService(HospitalRepository hospitalRepository, AlaRepository alaRepository) {
        this.hospitalRepository = hospitalRepository;
        this.alaRepository = alaRepository;
    }

    public HospitalDto criarHospital(HospitalDto dto) {
        Hospital hospital = new Hospital();
        hospital.setName(dto.getName());

        Hospital salvo = hospitalRepository.save(hospital);

        dto.setId(salvo.getId());
        return dto;
    }

    public void deletarHospital(Long hospitalId){
        if(!hospitalRepository.existsById(hospitalId)){
            throw new RuntimeException("Hospital não encontrado.");
        }

        if (alaRepository.existsByHospitalId(hospitalId)){
            throw new RuntimeException("Não é possível excluir o hospital. Existem alas associadas a esse hospital. ");
        }

        hospitalRepository.deleteById(hospitalId);
    }
}
