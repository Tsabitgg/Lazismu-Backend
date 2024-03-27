package com.ict.careus.repository;

import com.ict.careus.enumeration.CampaignCategory;
import com.ict.careus.model.campaign.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    @Query("SELECT c FROM Campaign c WHERE c.category.categoryName = :categoryName")
    Page<Campaign> findByCategoryName(@Param("categoryName") CampaignCategory categoryName, Pageable pageable);

    List<Campaign> findCampaignByActive(boolean isActive);

    Campaign findByCampaignCode(String campaignCode);

    Campaign findById(long campaignId);

    @Query("SELECT c FROM Campaign c WHERE LOWER(c.campaignName) LIKE LOWER(CONCAT('%', :campaignName, '%'))")
    Page<Campaign> findByCampaignName(String campaignName, Pageable pageable);

    @Query("SELECT c FROM Campaign c WHERE YEAR(c.startDate) = :year")
    Page<Campaign> findByYear(@Param("year") int year, Pageable pageable);
}
