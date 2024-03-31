package com.ict.careus.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ict.careus.model.Topic;
import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;

@Data
public class NewsRequest {
    private String title;
    private String content;
    private MultipartFile image;
    private Topic topic;

    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
