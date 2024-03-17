package com.ict.careus.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class CampaignRequest {

    private long campaignId;
    private long categoryId;
    private String campaignName;
    private String campaignCode;
    private MultipartFile campaignImage;
    private String description;
    private String location;
    private double targetAmount;
    private double currentAmount;
    private String vaNumber;
    private String creator;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private boolean active;
}
