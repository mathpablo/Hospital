package com.example.Hospital.Controller;

import com.example.Hospital.Enum.Specialty;
import com.example.Hospital.Service.InternationallService;
import com.example.Hospital.Service.PatientService;
import com.example.Hospital.model.InternationalLog;
import com.example.Hospital.model.Patient;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internacoes")
public class InternationallController {

    private InternationallService internationallService;
    private PatientService patientService;

    @Autowired
    public InternationallController(InternationallService internationallService, PatientService patientService) {
        this.internationallService = internationallService;
        this.patientService = patientService;
    }

    @PostMapping("/internar")
    public ResponseEntity<InternationalLog> internarPaciente(
            @RequestParam @NotNull Long patientId,
            @RequestParam @NotNull Specialty specialty) {

        Patient patient = patientService.buscarPorId(patientId)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com id " + patientId));

        InternationalLog internationalLog = internationallService.internarPaciente(patient, specialty);

        return ResponseEntity.status(HttpStatus.CREATED).body(internationalLog);
    }
}
