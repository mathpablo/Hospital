package com.example.hospital.service;

import com.example.hospital.dto.AlaCreateDto;
import com.example.hospital.dto.AlaDto;
import com.example.hospital.Enum.StatusLeito;
import com.example.hospital.dto.AtualizarAlaDto;
import com.example.hospital.repository.*;
import com.example.hospital.model.Ala;
import com.example.hospital.model.Hospital;
import com.example.hospital.model.Leito;
import com.example.hospital.model.Room;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlaService {

    private final InternmentRepository internmentRepository;
    private final AlaRepository alaRepository;
    private final HospitalService hospitalService;
    private final RoomRepository roomRepository;
    private final LeitoRepository leitoRepository;

    public AlaService(AlaRepository alaRepository,
                      HospitalService hospitalService,
                      RoomRepository roomRepository,
                      LeitoRepository leitoRepository,
                      InternmentRepository internmentRepository) {
        this.alaRepository = alaRepository;
        this.hospitalService = hospitalService;
        this.roomRepository = roomRepository;
        this.leitoRepository = leitoRepository;
        this.internmentRepository = internmentRepository;
    }

    @Transactional
    public Ala criarAlaComQuartosLeitos(AlaCreateDto dto) {
        Hospital hospital = hospitalService.buscarHospitalPorId(dto.getHospitalId());

        if (dto.getSpecialty() == null) {
            throw new IllegalArgumentException("Especialidade não pode ser nula");
        }

        Ala ala = new Ala();
        ala.setHospital(hospital);
        ala.setSpecialty(dto.getSpecialty());
        ala.setQuantidadeQuartos(dto.getQuantidadeQuartos());
        ala.setQuantidadeLeitosPorQuartos(dto.getQuantidadeLeitosPorQuartos());

        ala = alaRepository.save(ala);

        String prefixoCodigo = dto.getSpecialty().name().substring(0, 3).toUpperCase();
        criarQuartosELeitos(ala, hospital, prefixoCodigo, dto, 1); // começa do 1

        return ala;
    }

    private void criarQuartosELeitos(Ala ala, Hospital hospital, String prefixoCodigo, AlaCreateDto dto, int inicio) {
        for (int i = 0; i < dto.getQuantidadeQuartos(); i++) {
            int numeroQuarto = inicio + i;

            Room room = new Room();
            room.setAla(ala);
            room.setHospital(hospital);
            room.setStatus(StatusLeito.LIVRE);
            room.setCodigo(prefixoCodigo + numeroQuarto);
            room = roomRepository.save(room);

            for (int j = 1; j <= dto.getQuantidadeLeitosPorQuartos(); j++) {
                Leito leito = new Leito();
                leito.setRoom(room);
                leito.setStatus(StatusLeito.LIVRE);
                leito.setCodigo(prefixoCodigo + numeroQuarto + "-" + j);
                leito.setSpecialty(ala.getSpecialty());
                leitoRepository.save(leito);
            }
        }
    }

    public List<AlaDto> listarAlas() {
        return alaRepository.findAll().stream()
                .map(AlaDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletarAla(Long alaId) {
        Ala ala = alaRepository.findById(alaId)
                .orElseThrow(() -> new RuntimeException("Ala não encontrada"));

        List<Room> rooms = ala.getRooms();
        List<Leito> todosLeitos = new ArrayList<>();

        for (Room room : rooms) {
            todosLeitos.addAll(room.getLeitos());
        }

        if (!todosLeitos.isEmpty() && internmentRepository.existsByAnyLeito(todosLeitos)) {
            throw new IllegalStateException("Não é possível excluir a ala: existem pacientes internados.");
        }

        alaRepository.delete(ala);
    }

    @Transactional
    public void atualizarEstrutura(Long alaId, AtualizarAlaDto dto){
        Ala ala = alaRepository.findById(alaId)
                .orElseThrow(() -> new RuntimeException("Ala não encontrada"));

        int antigaQtdQuartos = ala.getQuantidadeQuartos();
        int novaQtdQuartos = dto.getQuantidadeQuartos();
        int antigaQtdLeitos = ala.getQuantidadeLeitosPorQuartos();
        int novaQtdLeitos = dto.getQuantidadeLeitosPorQuartos();

        ala.setQuantidadeQuartos(novaQtdQuartos);
        ala.setQuantidadeLeitosPorQuartos(novaQtdLeitos);
        alaRepository.save(ala);

        if (novaQtdQuartos > antigaQtdQuartos) {
            AlaCreateDto dtoFake = new AlaCreateDto();
            dtoFake.setHospitalId(ala.getHospital().getId());
            dtoFake.setSpecialty(ala.getSpecialty());
            dtoFake.setQuantidadeQuartos(novaQtdQuartos - antigaQtdQuartos);
            dtoFake.setQuantidadeLeitosPorQuartos(novaQtdLeitos);

            criarQuartosELeitos(ala, ala.getHospital(),
                    ala.getSpecialty().name().substring(0, 3).toUpperCase(),
                    dtoFake,
                    antigaQtdQuartos + 1);
        }

        if (novaQtdLeitos > antigaQtdLeitos) {
            adicionarLeitosNosQuartosExistentes(ala, antigaQtdLeitos + 1, novaQtdLeitos);
        }
    }

    private void adicionarLeitosNosQuartosExistentes(Ala ala, int de, int ate) {
        String prefixo = ala.getSpecialty().name().substring(0, 3).toUpperCase();

        for (Room room : ala.getRooms()) {
            int numeroDoQuarto = Integer.parseInt(room.getCodigo().replaceAll("\\D", ""));

            for (int i = de; i <= ate; i++) {
                Leito leito = new Leito();
                leito.setRoom(room);
                leito.setStatus(StatusLeito.LIVRE);
                leito.setCodigo(prefixo + numeroDoQuarto + "-" + i);
                leito.setSpecialty(ala.getSpecialty());
                leitoRepository.save(leito);
            }
        }
    }


}





