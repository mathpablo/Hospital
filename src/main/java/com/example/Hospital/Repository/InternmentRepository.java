package com.example.Hospital.Repository;

import com.example.Hospital.model.InternmentLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternmentRepository extends JpaRepository<InternmentLog, Long> {
}
