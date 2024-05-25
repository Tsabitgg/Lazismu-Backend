package com.ict.careus.service;

import com.ict.careus.dto.response.*;

import java.util.List;
import java.util.Optional;

public interface SummaryService {
    //summary for dashboard
    SummaryResponse getSummary(Integer year);

    //amil for campaign
    List<AmilCampaignResponse> getAmilCampaign();

    //amil for zakat
    List<AmilZakatResponse> getAmilZakat();

    //amil for infak
    List<AmilInfakResponse> getAmilInfak();

    //amil for wakaf
    List<AmilWakafResponse> getAmilWakaf();

    //summary for campaign
    Optional<SummaryCampaignResponse> getSummaryCampaign();
}
