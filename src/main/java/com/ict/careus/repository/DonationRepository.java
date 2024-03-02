package com.ict.careus.repository;

import com.ict.careus.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Integer> {

    @Procedure
    void update_campaign_current_amount(@Param("campaignCode") String campaignCode, @Param("amount") double amount);
}
