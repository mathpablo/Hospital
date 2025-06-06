package com.example.Hospital.Repository;

import com.example.Hospital.Projection.HistoricoInternmentProjection;
import com.example.Hospital.Projection.InternmentDetailProjection;
import com.example.Hospital.model.InternmentLog;
import com.example.Hospital.model.Leito;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InternmentRepository extends JpaRepository<InternmentLog, Long> {

    @Query(value = "SELECT p.name as patientName, " +
            "a.specialty as specialty, " +
            "i.data_internamento as admissionDate, " +
            "i.data_alta as dischargeDate, " +
            "h.name As hospitalName " +
            "FROM internment_log i " +
            "JOIN patient p ON i.patient_id = p.id " +
            "JOIN leito l ON i.leito_id = l.id " +
            "JOIN room q ON l.room_id = q.id " +
            "JOIN ala a ON q.ala_id = a.id " +
            "JOIN hospital h ON q.hospital_id = h.id " +
            "WHERE p.id = :patientId " +
            "ORDER BY i.data_internamento DESC",
            countQuery = "SELECT count(*) FROM internment_log i WHERE i.patient_id = :patientId",
            nativeQuery = true)
    Page<HistoricoInternmentProjection> findHistoryByPatientId(@Param("patientId") Long patientId, Pageable pageable);


    @Query("SELECT i FROM InternmentLog i WHERE i.dataAlta IS NULL")
    List<InternmentLog> findInternacoesAtivas();

    List<InternmentLog> findByPatientId(Long patientId);

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END " +
            "FROM InternmentLog i WHERE i.patient.id = :patientId AND i.dataAlta IS NULL")
    boolean existsByPatientIdAndInternacaoAtiva(@Param("patientId") Long patientId);

    Optional<InternmentLog> findByPatientIdAndDataAltaIsNull(Long patientId);

    @Modifying
    @Transactional
    @Query("update InternmentLog i set i.leito = null where i.leito = :leito")
    void clearLeitoFromLogs(@Param("leito") Leito leito);


    @Transactional
    void deleteByLeito(Leito leito);

    @Query("""
    SELECT
        h.name as hospitalName,
        l.specialty as specialty,
        r.codigo as codigo,
        p.name as patientName,
        il.dateInternamento as dataInternamento
    FROM InternmentLog il
    JOIN il.leito l
    JOIN l.room r
    JOIN r.ala a
    JOIN a.hospital h
    JOIN il.patient p
    WHERE p.id = :patientId
      AND il.dataAlta IS NULL
""")
    Optional<InternmentDetailProjection> findInternacaoAtivaDetalhesPorPaciente(@Param("patientId") Long patientId);

}

