package com.moit.advertisement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moit.advertisement.entity.AdvertisementDailyStatistics;

public interface AdvertisementDailyStatisticsRepository
        extends JpaRepository<AdvertisementDailyStatistics, Long> {
 
}