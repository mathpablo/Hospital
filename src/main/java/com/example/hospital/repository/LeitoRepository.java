package com.example.hospital.repository;

import com.example.hospital.projection.QuantidadeLeitoLivreProjection;
import com.example.hospital.model.Leito;
import com.example.hospital.Enum.Specialty;
import com.example.hospital.Enum.StatusLeito;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LeitoRepository extends CrudRepository<Leito, Long> {


    Optional<Leito> findFirstByRoom_Ala_SpecialtyAndStatus(Specialty specialty, StatusLeito status);

    @Query("SELECT a.specialty AS specialty, COUNT(l) AS quantidadeLeitosLivres " +
            "FROM Leito l " +
            "JOIN Room r ON l.room = r " +
            "JOIN Ala a ON r.ala = a " +
            "WHERE l.status = 'LIVRE' GROUP BY a.specialty")
    List<QuantidadeLeitoLivreProjection> findLeitosLivresPorEspecialidade();

    Optional<Leito>findByCodigo(String codigo);

}
