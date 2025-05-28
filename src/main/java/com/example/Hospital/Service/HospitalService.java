package com.example.Hospital.Service;

import com.example.Hospital.Dto.HospitalDto;
import com.example.Hospital.Repository.HospitalRepository;
import com.example.Hospital.model.Hospital;
import org.springframework.stereotype.Service;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public HospitalDto criarHospital(HospitalDto dto) {
        Hospital hospital = new Hospital();
        hospital.setName(dto.getName());

        Hospital salvo = hospitalRepository.save(hospital);

        dto.setId(salvo.getId());
        return dto;
    }
}
