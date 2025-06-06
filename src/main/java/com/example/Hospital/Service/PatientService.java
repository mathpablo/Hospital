package com.example.Hospital.Service;

import com.example.Hospital.Dto.PatientDto;
import com.example.Hospital.Repository.PatientRepository;
import com.example.Hospital.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient save(Patient patient){
        return patientRepository.save(patient);
    }

    public Optional<Patient> buscarPorId(Long id) {
        return patientRepository.findById(id);
    }

    public PatientDto create(PatientDto dto){
        Patient patient = new Patient();
        patient.setName(dto.getName());
        patient.setDataNascimento(dto.getDataNascimento());

        Patient saved = patientRepository.save(patient);

        PatientDto response = new PatientDto();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setDataNascimento(saved.getDataNascimento());

        return response;

    }
}

