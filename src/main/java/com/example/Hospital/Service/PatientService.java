package com.example.Hospital.Service;

import com.example.Hospital.Dto.PatientDto;
import com.example.Hospital.Repository.InternmentRepository;
import com.example.Hospital.Repository.PatientRepository;
import com.example.Hospital.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final InternmentRepository internmentRepository;
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository, InternmentRepository internmentRepository) {
        this.patientRepository = patientRepository;
        this.internmentRepository = internmentRepository;
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
        dto.setId(saved.getId());
        return dto;

    }

    public List<PatientDto> listarTodos(){
        List<Patient> pacientes = patientRepository.findAll();

        return pacientes.stream().map(patient -> {
            PatientDto dto = new PatientDto();
            dto.setId(patient.getId());
            dto.setName(patient.getName());
            dto.setDataNascimento(patient.getDataNascimento());
            return dto;
        }).collect(Collectors.toList());
    }

    public void deletarPaciente(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado.");
        }

        boolean internado = internmentRepository.existsByPatientIdAndDataAltaIsNull(id);
        if (internado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Paciente está internado e não pode ser deletado.");
        }

        patientRepository.deleteById(id);
    }
}

