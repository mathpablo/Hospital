package com.example.Hospital.Repository;

import com.example.Hospital.Projection.HistoricoInternmentProjection;
import com.example.Hospital.model.InternmentLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InternmentRepository extends JpaRepository<InternmentLog, Long> {

    @Query(value = "SELECT p.nome as patientName, a.especialidade as specialty, i.data_internamento as admissionDate, i.data_alta as dischargeDate " +
            "FROM internment_log i " +
            "JOIN patient p ON i.patient_id = p.id " +
            "JOIN leito l ON i.leito_id = l.id " +
            "JOIN room q ON l.room_id = q.id " +
            "JOIN ala a ON q.ala_id = a.id " +
            "WHERE p.id = :patientId",
            countQuery = "SELECT count(*) FROM internment_log i WHERE i.patient_id = :patientId",
            nativeQuery = true)
    Page<HistoricoInternmentProjection> findHistoryByPatientId(@Param("patientId") Long patientId, Pageable pageable);

    @Query("SELECT i FROM InternmentLog i WHERE i.dataAlta IS NULL")
    List<InternmentLog> findInternacoesAtivas();

    List<InternmentLog> findByPatientId(Long patientId);

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END " +
            "FROM InternmentLog i WHERE i.patient.id = :patientId AND i.dataAlta IS NULL")
    boolean existsByPatientIdAndInternacaoAtiva(@Param("patientId") Long patientId);

}
