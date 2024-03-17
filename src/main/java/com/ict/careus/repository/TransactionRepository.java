package com.ict.careus.repository;

import com.ict.careus.model.campaign.Campaign;
import com.ict.careus.model.transaction.Transaction;
import com.ict.careus.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    List<Transaction> findByUser(User user);

    List<Transaction> findByCampaign(Campaign campaign);

}
