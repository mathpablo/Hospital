package com.example.Hospital.Repository;

import com.example.Hospital.model.Room;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends CrudRepository<Room, Long> {
    @Query(value = """
        SELECT q.* FROM quarto q
        JOIN leito l ON l.quarto_id = q.id
        JOIN paciente p ON p.id = l.paciente_id
        WHERE p.id = :pacienteId AND l.status = 'OCUPADO'
        """, nativeQuery = true)
    Room findQuartoByPacienteInternado(@Param("pacienteId") Long pacienteId);
}
