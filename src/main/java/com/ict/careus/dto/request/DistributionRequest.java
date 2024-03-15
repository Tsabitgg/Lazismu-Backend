package com.ict.careus.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class DistributionRequest {
    private double distributionAmount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date distributionDate;

    private MultipartFile image;

    private String description;

    private boolean success;
}