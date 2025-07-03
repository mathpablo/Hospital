package com.example.Hospital.Repository;

import com.example.Hospital.Projection.AlaProjection;
import com.example.Hospital.model.Ala;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface AlaRepository extends JpaRepository<Ala, Long> {
    List<AlaProjection> findByHospitalId(Long hospitalId);
    boolean existsByHospitalId(Long hospitalId);

}
