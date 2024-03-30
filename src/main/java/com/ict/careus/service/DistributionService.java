package com.ict.careus.service;

import com.ict.careus.dto.request.DistributionRequest;
import com.ict.careus.model.transaction.Distribution;
import org.apache.coyote.BadRequestException;

public interface DistributionService {
    Distribution createDistribution(String type, String code, DistributionRequest distributionRequest) throws BadRequestException;
}
