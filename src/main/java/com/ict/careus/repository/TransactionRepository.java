package com.ict.careus.repository;

import com.ict.careus.model.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Procedure
    void update_campaign_current_amount(@Param("campaignCode") String campaignCode, @Param("TransactionAmount") double transactionAmount);

    @Procedure
    void update_zakat_amount(@Param("zakatCode") String zakatCode, @Param("TransactionAmount") double transactionAmount);

    @Procedure
    void update_infak_amount(@Param("infakCode") String zainfakCode, @Param("TransactionAmount") double transactionAmount);

    @Procedure
    void update_wakaf_amount(@Param("wakafCode") String wakafCode, @Param("TransactionAmount") double transactionAmount);
}
