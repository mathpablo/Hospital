package com.example.Hospital.Service;

import com.example.Hospital.Repository.HospitalRepository;
import com.example.Hospital.Repository.RoomRepository;
import com.example.Hospital.model.Hospital;
import com.example.Hospital.model.Room;
import org.springframework.stereotype.Service;

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
}
