package com.moit.advertisement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moit.advertisement.entity.AdvertisementPrice;
import com.moit.advertisement.enums.AdGrade;

public interface AdvertisementPriceRepository
        extends JpaRepository<AdvertisementPrice, Long> {
	
	Optional<AdvertisementPrice> findByAdGradeAndPeriodDays(
            AdGrade adGrade,
            Integer periodDays
    );

}