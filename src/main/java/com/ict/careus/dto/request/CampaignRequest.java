package com.ict.careus.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class CampaignRequest {

    private int campaignId;
    private int categoryId;
    private String campaignName;
    private String campaignCode;
    private MultipartFile campaignImage;
    private String description;
    private String location;
    private double targetAmount;
    private double currentAmount;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private String vaNumber;
    private boolean active;
}
