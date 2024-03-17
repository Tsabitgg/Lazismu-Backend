package com.ict.careus.controller;

import com.ict.careus.dto.response.CategoryResponse;
import com.ict.careus.enumeration.CampaignCategory;
import com.ict.careus.enumeration.InfakCategory;
import com.ict.careus.enumeration.WakafCategory;
import com.ict.careus.enumeration.ZakatCategory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoriesController {

    @GetMapping("/campaign/category")
    public List<CategoryResponse> getAllCampaignCategory() {
        CampaignCategory[] campaignCategory = CampaignCategory.values();
        List<CategoryResponse> campaignCategoryResponse = new ArrayList<>();

        for (int i = 0; i < campaignCategory.length; i++) {
            CategoryResponse campaignCategoryDTO = new CategoryResponse();
            campaignCategoryDTO.setId(i + 1);
            campaignCategoryDTO.setCategoryName(campaignCategory[i].toString());
            campaignCategoryResponse.add(campaignCategoryDTO);
        }
        return campaignCategoryResponse;
    }

    @GetMapping("/zakat/category")
    public List<CategoryResponse> getAllZakatCategory() {
        ZakatCategory[] zakatCategory = ZakatCategory.values();
        List<CategoryResponse> zakatCategoryResponse = new ArrayList<>();

        for (int i = 0; i < zakatCategory.length; i++) {
            CategoryResponse zakatCategoryDTO = new CategoryResponse();
            zakatCategoryDTO.setId(i + 1);
            zakatCategoryDTO.setCategoryName(zakatCategory[i].toString());
            zakatCategoryResponse.add(zakatCategoryDTO);
        }
        return zakatCategoryResponse;
    }

    @GetMapping("/infak/category")
    public List<CategoryResponse> getAllInfakCategory() {
        InfakCategory[] infakCategory = InfakCategory.values();
        List<CategoryResponse> infakCategoryResponse = new ArrayList<>();

        for (int i = 0; i < infakCategory.length; i++) {
            CategoryResponse infakCategoryDTO = new CategoryResponse();
            infakCategoryDTO.setId(i + 1);
            infakCategoryDTO.setCategoryName(infakCategory[i].toString());
            infakCategoryResponse.add(infakCategoryDTO);
        }
        return infakCategoryResponse;
    }

    @GetMapping("/wakaf/category")
    public List<CategoryResponse> getAllWakafCategory() {
        WakafCategory[] wakafCategory = WakafCategory.values();
        List<CategoryResponse> wakafCategoryResponse = new ArrayList<>();

        for (int i = 0; i < wakafCategory.length; i++) {
            CategoryResponse wakafCategoryDTO = new CategoryResponse();
            wakafCategoryDTO.setId(i + 1);
            wakafCategoryDTO.setCategoryName(wakafCategory[i].toString());
            wakafCategoryResponse.add(wakafCategoryDTO);
        }
        return wakafCategoryResponse;
    }
}
