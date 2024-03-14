package com.ict.careus.repository;

import com.ict.careus.model.ziswaf.Wakaf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WakafRepository extends JpaRepository<Wakaf, Long> {
    Wakaf findByWakafCode(String wakafCode);
}
