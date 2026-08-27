package com.moit.advertisement.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moit.advertisement.entity.AdvertisementClickLog;
import com.moit.advertisement.enums.AdPosition;

public interface AdvertisementClickLogRepository extends JpaRepository<AdvertisementClickLog, Long> {
	// 로그인 사용자
	boolean existsByAdvertisement_AdIdAndMember_IdAndPositionAndClickedAtAfter(
            Long adId,
            Long memberId,
            com.moit.advertisement.enums.AdPosition position,
            LocalDateTime after
    );
	
	// 비로그인 사용자
	boolean existsByAdvertisement_AdIdAndIpAddressAndPositionAndClickedAtAfter(
            Long adId,
            String ipAddress,
            AdPosition position,
            LocalDateTime after
    );
}