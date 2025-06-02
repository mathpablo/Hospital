package com.example.Hospital.Repository;

import com.example.Hospital.model.InternmentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface InternmentRepository extends JpaRepository<InternmentLog, Long> {
    List<InternmentLog> findByPatientId(Long patientId);

    @Query("SELECT i FROM InternmentLog i WHERE i.dataAlta IS NULL")
    List<InternmentLog> findInternacoesAtivas();

}
