package com.ict.careus.model;

import com.ict.careus.enumeration.CampaignCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    @Enumerated(EnumType.STRING)
    private CampaignCategory categoryName;

    private String categoryImage;
}
