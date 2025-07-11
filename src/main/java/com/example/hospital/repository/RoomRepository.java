package com.example.hospital.repository;

import com.example.hospital.Enum.StatusLeito;
import com.example.hospital.projection.RoomAvailableProjection;
import com.example.hospital.model.Room;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends CrudRepository<Room, Long> {
    @Query(value = """
        SELECT q.* FROM quarto q
        JOIN leito l ON l.quarto_id = q.id
        JOIN paciente p ON p.id = l.paciente_id
        WHERE p.id = :pacienteId AND l.status = 'OCUPADO'
        """, nativeQuery = true)
    Room findQuartoByPacienteInternado(@Param("pacienteId") Long pacienteId);

    @Query("""
            SELECT DISTINCT
                r.ala.specialty as specialty,
                r.codigo as codigo
            FROM Room r
            JOIN r.leitos l
            WHERE l.status =:status
            """)
    List<RoomAvailableProjection>findRoomWithAvailableLeitos(StatusLeito status);

    @EntityGraph(attributePaths = {"ala", "leitos"})
    List<Room> findAll();

}
