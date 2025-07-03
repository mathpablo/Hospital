package com.example.Hospital.Service;

import com.example.Hospital.Dto.HospitalDto;
import com.example.Hospital.Repository.AlaRepository;
import com.example.Hospital.Repository.HospitalRepository;
import com.example.Hospital.model.Hospital;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final AlaRepository alaRepository;

    public HospitalService(HospitalRepository hospitalRepository, AlaRepository alaRepository) {
        this.hospitalRepository = hospitalRepository;
        this.alaRepository = alaRepository;
    }

    public Hospital buscarHospitalPorId(Long id){
        return hospitalRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hospital não encontrado com o id:" + id));
    }

    public Hospital criarHospital(Hospital hspital) {
        Hospital hospital = new Hospital();
        hospital.setName(hospital.getName());
        return this.hospitalRepository.save(hospital);
    }

    public ResponseEntity<String> deletarHospital(Long id) {
        try {
            Hospital hospital = hospitalRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Hospital não encontrado."));

            hospitalRepository.delete(hospital);
            return ResponseEntity.ok("Hospital excluído com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    public List<HospitalDto> listarHospitais() {
        List<Hospital> hospitais = hospitalRepository.findAll();

        List<HospitalDto> dtos = hospitais.stream().map(hospital -> {
            HospitalDto dto = new HospitalDto();
            dto.setId(hospital.getId());
            dto.setName(hospital.getName());

            return dto;
        }).collect(Collectors.toList());

        return dtos;
    }

}
