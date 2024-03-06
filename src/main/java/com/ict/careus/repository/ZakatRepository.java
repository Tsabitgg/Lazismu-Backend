package com.ict.careus.repository;

import com.ict.careus.model.ziswaf.Zakat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZakatRepository extends JpaRepository<Zakat, Integer> {
    Zakat findByZakatCode(String zakatCode);
}
