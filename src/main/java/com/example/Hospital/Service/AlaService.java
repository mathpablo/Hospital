package com.example.Hospital.Service;

import com.example.Hospital.Dto.AlaCreateDto;
import com.example.Hospital.Enum.StatusLeito;
import com.example.Hospital.Repository.AlaRepository;
import com.example.Hospital.Repository.HospitalRepository;
import com.example.Hospital.Repository.LeitoRepository;
import com.example.Hospital.Repository.RoomRepository;
import com.example.Hospital.model.Ala;
import com.example.Hospital.model.Hospital;
import com.example.Hospital.model.Leito;
import com.example.Hospital.model.Room;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AlaService {

    private  AlaRepository alaRepository;
    private  HospitalRepository hospitalRepository;
    private RoomRepository roomRepository;
    private LeitoRepository leitoRepository;

    public AlaService (AlaRepository alaRepository,
                       HospitalRepository hospitalRepository,
                       RoomRepository roomRepository,
                       LeitoRepository leitoRepository){
        this.alaRepository = alaRepository;
        this.hospitalRepository = hospitalRepository;
        this.roomRepository = roomRepository;
        this.leitoRepository = leitoRepository;
    }

    @Transactional
    public Ala criarAlaComQuartosLeitos(AlaCreateDto dto) {
        Hospital hospital = hospitalRepository.findById(dto.getHospitalId())
                .orElseThrow(() -> new RuntimeException("Hospital não encontrado"));

        Ala ala = new Ala();
        ala.setHospital(hospital);
        ala.setSpecialty(dto.getSpecialty());
        ala = alaRepository.save(ala);

        String prefixoCoidgo = dto.getSpecialty().name().substring(0, 3).toUpperCase();

        for (int i = 1; i <= dto.getQuantidadeQuartos(); i++) {
            Room room = new Room();
            room.setAla(ala);
            room.setStatus(StatusLeito.LIVRE);
            room.setCodigo(prefixoCoidgo + i);
            room = roomRepository.save(room);


            for (int j = 1; j <= dto.getQuantidadeLeitosPorQuartos(); j++) {
                Leito leito = new Leito();
                leito.setRoom(room);
                leito.setStatus(StatusLeito.LIVRE);
                leito.setCodigo(prefixoCoidgo + i + "-" + j);
                leitoRepository.save(leito);
            }
        }
        return ala;
    }

    }




