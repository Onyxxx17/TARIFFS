package com.tariff.service;

import java.util.List;

import com.tariff.entity.Industry;

public interface IndustryService {
    List<Industry> listIndustry();
    Industry getIndustry(Long id);
    Industry addIndustry(Industry industry);
    Industry updateIndustry(Long id, Industry industry);
    void deleteIndustry(Long industry);
}
