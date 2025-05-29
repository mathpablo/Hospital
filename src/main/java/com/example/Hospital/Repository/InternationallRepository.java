package com.example.Hospital.Repository;

import com.example.Hospital.model.InternationalLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternationallRepository extends JpaRepository<InternationalLog, Long> {
}
