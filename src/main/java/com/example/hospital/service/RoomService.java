package com.example.hospital.service;

import com.example.hospital.dto.LeitoDto;
import com.example.hospital.dto.RoomDto;
import com.example.hospital.repository.HospitalRepository;
import com.example.hospital.repository.InternmentRepository;
import com.example.hospital.repository.RoomRepository;
import com.example.hospital.model.Hospital;
import com.example.hospital.model.Room;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final HospitalRepository hospitalRepository;
    private final InternmentRepository internmentRepository;

    public RoomService(RoomRepository roomRepository, HospitalRepository hospitalRepository, InternmentRepository internmentRepository) {
        this.roomRepository = roomRepository;
        this.hospitalRepository = hospitalRepository;
        this.internmentRepository = internmentRepository;
    }

    public Room criarRoomComHospital(Long hospitalId, String codigoRoom) {

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new RuntimeException("Hospital não encontrado"));

        Room room = new Room();
        room.setCodigo(codigoRoom);
        room.setHospital(hospital);

        return roomRepository.save(room);
    }

    public List<RoomDto> listarQuartosComLeitosDisponiveis() {
        List<Room> rooms = roomRepository.findAll();

        return rooms.stream().map(room -> {
            RoomDto dto = new RoomDto();
            dto.setId(room.getId());
            dto.setCodigo(room.getCodigo());
            dto.setAlaId(room.getAla() != null ? room.getAla().getId() : null);
            dto.setStatus(room.getStatus() != null ? room.getStatus().name() : null);
            dto.setSpecialty(room.getAla() != null ? room.getAla().getSpecialty().name() : "Sem ala");

            List<LeitoDto> leitoDtos = room.getLeitos().stream().map(leito -> {
                LeitoDto ldto = new LeitoDto();
                ldto.setId(leito.getId());
                ldto.setCodigo(leito.getCodigo());
                ldto.setStatus(leito.getStatus() != null ? leito.getStatus().name() : null);
                ldto.setSpecialty(leito.getSpecialty() != null ? leito.getSpecialty().name() : null);
                return ldto;
            }).collect(Collectors.toList());

            dto.setLeitos(leitoDtos);

            return dto;
        }).collect(Collectors.toList());
    }




    public void deletarRoom(Long id){
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado."));

        boolean temLeitoComInternacao = room.getLeitos().stream()
                .anyMatch(leito -> !internmentRepository.findByLeitoId(leito.getId()).isEmpty());

        if(temLeitoComInternacao){
            throw new IllegalStateException("Não é possível excluir o quarto porque há leitos com internação registrada.");
        }

        roomRepository.delete(room);
    }


}
