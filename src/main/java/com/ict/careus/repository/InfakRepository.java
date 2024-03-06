package com.ict.careus.repository;

import com.ict.careus.model.ziswaf.Infak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfakRepository extends JpaRepository<Infak, Integer> {
    Infak findByInfakCode(String infakCode);
}
