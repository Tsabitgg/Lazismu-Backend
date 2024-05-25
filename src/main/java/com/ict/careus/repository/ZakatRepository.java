package com.ict.careus.repository;

import com.ict.careus.dto.response.AmilZakatResponse;
import com.ict.careus.model.ziswaf.Zakat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ZakatRepository extends JpaRepository<Zakat, Long> {
    Zakat findByZakatCode(String zakatCode);

    @Query("SELECT\n" +
            " z.zakatId,\n" +
            " z.zakatCategory,\n" +
            " z.zakatCode,\n" +
            " z.amount,\n" +
            " z.amount * 0.15 AS amil\n" +
            "FROM\n" +
            " Zakat z\n" +
            "GROUP BY z.zakatId, z.zakatCategory")
    List<Object []> getAmilZakat();

}
