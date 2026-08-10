package com.moit.advertisement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moit.advertisement.entity.AdvertisementImpressionLog;

public interface AdvertisementImpressionLogRepository extends JpaRepository<AdvertisementImpressionLog, Long> {
}
