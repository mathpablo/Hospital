package com.example.Hospital.Service;

import com.example.Hospital.Dto.AlaCreateDto;
import com.example.Hospital.Enum.StatusLeito;
import com.example.Hospital.Repository.*;
import com.example.Hospital.model.Ala;
import com.example.Hospital.model.Hospital;
import com.example.Hospital.model.Leito;
import com.example.Hospital.model.Room;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlaService {

    private InternmentRepository internmentRepository;
    private  AlaRepository alaRepository;
    private  HospitalRepository hospitalRepository;
    private RoomRepository roomRepository;
    private LeitoRepository leitoRepository;

    public AlaService (AlaRepository alaRepository,
                       HospitalRepository hospitalRepository,
                       RoomRepository roomRepository,
                       LeitoRepository leitoRepository,
                       InternmentRepository internmentRepository){
        this.alaRepository = alaRepository;
        this.hospitalRepository = hospitalRepository;
        this.roomRepository = roomRepository;
        this.leitoRepository = leitoRepository;
        this.internmentRepository = internmentRepository;
    }

    @Transactional
    public Ala criarAlaComQuartosLeitos(AlaCreateDto dto) {

        Hospital hospital = hospitalRepository.findById(dto.getHospitalId())
                .orElseThrow(() -> new RuntimeException("Hospital não encontrado"));

        Ala ala = new Ala();
        ala.setHospital(hospital);
        ala.setSpecialty(dto.getSpecialty());
        ala = alaRepository.save(ala);

        if(dto.getSpecialty() ==null){
            throw new IllegalArgumentException("Especialidade não pode ser nula");
        }

        String prefixoCoidgo = dto.getSpecialty().name().substring(0, 3).toUpperCase();


        for (int i = 1; i <= dto.getQuantidadeQuartos(); i++) {
            Room room = new Room();
            room.setAla(ala);
            room.setHospital(hospital);
            room.setStatus(StatusLeito.LIVRE);
            room.setCodigo(prefixoCoidgo + i);
            room = roomRepository.save(room);


            for (int j = 1; j <= dto.getQuantidadeLeitosPorQuartos(); j++) {
                Leito leito = new Leito();
                leito.setRoom(room);
                leito.setStatus(StatusLeito.LIVRE);
                leito.setCodigo(prefixoCoidgo + i + "-" + j);
                leito.setSpecialty(ala.getSpecialty());
                leitoRepository.save(leito);
            }
        }
        return ala;
    }

    @Transactional
    public void deletarAla(Long alaId){
        Ala ala = alaRepository.findById(alaId)
                .orElseThrow(() -> new RuntimeException("Ala não encontrada"));

        List<Room> rooms = ala.getRooms();

        for(Room room : rooms){
           List<Leito> leitos = room.getLeitos();

           for(Leito leito : leitos){
               internmentRepository.deleteByLeito(leito);
           }
        }

        alaRepository.delete(ala);
    }
}




