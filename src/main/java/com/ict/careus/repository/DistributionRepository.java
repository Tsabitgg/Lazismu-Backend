package com.ict.careus.repository;

import com.ict.careus.model.transaction.Distribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributionRepository extends JpaRepository<Distribution, Long> {

    @Procedure
    void update_campaign_distribution(@Param("campaignCode") String campaigCode, @Param("distributionAmount") double distributionAmount);

    @Procedure
    void update_zakat_distribution(@Param("zakatCode") String zakatCode, @Param("distributionAmount") double distributionAmount);

    @Procedure
    void update_infak_distribution(@Param("infakCode") String infakCode, @Param("distributionAmount") double distributionAmount);

    @Procedure
    void update_wakaf_distribution(@Param("wakafCode") String wakafCode, @Param("distributionAmount") double distributionAmount);
}