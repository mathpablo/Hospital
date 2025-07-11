package com.example.hospital.repository;

import com.example.hospital.projection.AlaProjection;
import com.example.hospital.model.Ala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface AlaRepository extends JpaRepository<Ala, Long> {
    List<AlaProjection> findByHospitalId(Long hospitalId);
    boolean existsByHospitalId(Long hospitalId);

}
