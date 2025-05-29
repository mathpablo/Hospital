package com.example.Hospital.Repository;

import com.example.Hospital.Projection.LeitoLivreProjection;
import com.example.Hospital.model.Leito;
import com.example.Hospital.Enum.Specialty;
import com.example.Hospital.Enum.StatusLeito;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LeitoRepository extends CrudRepository<Leito, Long> {


    Optional<Leito> findFirstByRoom_Ala_SpecialtyAndStatus(Specialty specialty, StatusLeito status);

    @Query("SELECT l.room.ala.specialty as specialty, COUNT(l) as quantidadeLeitosLivres " +
            "FROM Leito l WHERE l.status = 'LIVRE' GROUP BY l.room.ala.specialty")
    List<LeitoLivreProjection> contarLeitosLivresPorEspecialidade();
}
