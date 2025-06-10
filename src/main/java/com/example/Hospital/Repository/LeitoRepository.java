package com.example.Hospital.Repository;

import com.example.Hospital.Projection.QuantidadeLeitoLivreProjection;
import com.example.Hospital.model.Leito;
import com.example.Hospital.Enum.Specialty;
import com.example.Hospital.Enum.StatusLeito;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LeitoRepository extends CrudRepository<Leito, Long> {


    Optional<Leito> findFirstByRoom_Ala_SpecialtyAndStatus(Specialty specialty, StatusLeito status);

    @Query("SELECT a.specialty AS specialty, COUNT(l) AS quantidadeLeitosLivres " +
            "FROM Leito l JOIN l.room r JOIN r.ala a " +
            "WHERE l.status = 'LIVRE' GROUP BY a.specialty")
    List<QuantidadeLeitoLivreProjection> findLeitosLivresPorEspecialidade();

    @Query("SELECT l FROM Leito l JOIN FETCH l.room WHERE l.id =: id")
    Leito findLeitoComRoom(@Param("id") Long id);



}
