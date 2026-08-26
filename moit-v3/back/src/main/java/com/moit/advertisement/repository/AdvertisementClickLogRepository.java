package com.moit.advertisement.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moit.advertisement.entity.AdvertisementClickLog;
import com.moit.advertisement.enums.AdPosition;

public interface AdvertisementClickLogRepository extends JpaRepository<AdvertisementClickLog, Long> {
	
	boolean existsByAdvertisement_AdIdAndIpAddressAndPositionAndClickedAtAfter(
            Long adId,
            String ipAddress,
            AdPosition position,
            LocalDateTime after
    );
}