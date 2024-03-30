package com.ict.careus.service;

import com.ict.careus.dto.response.SummaryResponse;

public interface SummaryService {
    //summary for dashboard
    SummaryResponse getSummary(Integer year);
}
