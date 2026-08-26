package com.moit.advertisement.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moit.advertisement.entity.AdvertisementImpressionLog;
import com.moit.advertisement.enums.AdPosition;

public interface AdvertisementImpressionLogRepository 
				extends JpaRepository<AdvertisementImpressionLog, Long> {
	
	boolean existsByAdvertisement_AdIdAndIpAddressAndPositionAndViewedAtAfter(
            Long adId,
            String ipAddress,
            AdPosition position,
            LocalDateTime after
    );
}
