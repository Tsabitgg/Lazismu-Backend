package com.ict.careus.service;

import com.ict.careus.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SummaryService {
    //summary for dashboard
    SummaryResponse getSummary(Integer year);

    //amil for campaign
    Page<AmilCampaignResponse> getAmilCampaign(Pageable pageable);

    //amil for zakat
    Page<AmilZakatResponse> getAmilZakat(Pageable pageable);

    //amil for infak
    Page<AmilInfakResponse> getAmilInfak(Pageable pageable);

    //amil for wakaf
    Page<AmilWakafResponse> getAmilWakaf(Pageable pageable);

    //summary for campaign
    Optional<SummaryCampaignResponse> getSummaryCampaign();
}
