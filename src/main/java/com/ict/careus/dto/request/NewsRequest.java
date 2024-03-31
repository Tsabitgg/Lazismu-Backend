package com.ict.careus.dto.request;

import com.ict.careus.model.Topic;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class NewsRequest {
    private String title;
    private String content;
    private MultipartFile image;
    private Topic topic;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
}
