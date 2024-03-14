package com.ict.careus.repository;

import com.ict.careus.enumeration.CampaignCategory;
import com.ict.careus.model.campaign.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    @Query("SELECT c FROM Campaign c WHERE c.category.categoryName = :categoryName")
    List<Campaign> findByCategoryName(@Param("categoryName") CampaignCategory categoryName);

    List<Campaign> findCampaignByActive(boolean isActive);
    Campaign findByCampaignCode(String CampaignCode);
}
