package com.example.Hospital.Service;

import com.example.Hospital.Dto.LeitoDto;
import com.example.Hospital.Dto.RoomDto;
import com.example.Hospital.Enum.StatusLeito;
import com.example.Hospital.Projection.RoomAvailableProjection;
import com.example.Hospital.Repository.HospitalRepository;
import com.example.Hospital.Repository.InternmentRepository;
import com.example.Hospital.Repository.RoomRepository;
import com.example.Hospital.model.Hospital;
import com.example.Hospital.model.Room;
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
            dto.setSpecialty(room.getAla() != null ? room.getAla().getSpecialty().toString() : "Sem ala");

            List<LeitoDto> leitoDtos = room.getLeitos().stream().map(leito -> {
                LeitoDto ldto = new LeitoDto();
                ldto.setId(leito.getId());
                ldto.setStatus(leito.getStatus());
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
