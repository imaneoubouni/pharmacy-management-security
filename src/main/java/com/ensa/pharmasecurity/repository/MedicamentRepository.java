package com.ensa.pharmasecurity.repository;

import com.ensa.pharmasecurity.model.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentRepository
        extends JpaRepository<Medicament, Long> {
}