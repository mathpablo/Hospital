package com.example.Hospital.Service;

import com.example.Hospital.Enum.StatusLeito;
import com.example.Hospital.Projection.RoomAvailableProjection;
import com.example.Hospital.Repository.HospitalRepository;
import com.example.Hospital.Repository.RoomRepository;
import com.example.Hospital.model.Hospital;
import com.example.Hospital.model.Room;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final HospitalRepository hospitalRepository;

    public RoomService(RoomRepository roomRepository, HospitalRepository hospitalRepository) {
        this.roomRepository = roomRepository;
        this.hospitalRepository = hospitalRepository;
    }

    public Room criarRoomComHospital(Long hospitalId, String codigoRoom) {

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new RuntimeException("Hospital não encontrado"));

        Room room = new Room();
        room.setCodigo(codigoRoom);
        room.setHospital(hospital);

        return roomRepository.save(room);
    }

    public List<RoomAvailableProjection>listarQuartosComLeitosDisponiveis(){
        return roomRepository.findRoomWithAvailableLeitos(StatusLeito.LIVRE);
    }

    public void deletarRoom(Long id){
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado."));

        boolean temLeitoOcupado = room.getLeitos().stream()
                .anyMatch(leito -> leito.getStatus() ==StatusLeito.OCUPADO);

        if(temLeitoOcupado){
            throw new RuntimeException("Não é possível excluir os quartos, pois tem leitos ocupados");
        }

        roomRepository.delete(room);
    }
}
