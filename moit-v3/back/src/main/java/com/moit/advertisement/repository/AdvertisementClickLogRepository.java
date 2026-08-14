package com.moit.advertisement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moit.advertisement.entity.AdvertisementClickLog;

public interface AdvertisementClickLogRepository extends JpaRepository<AdvertisementClickLog, Long> {
	
}