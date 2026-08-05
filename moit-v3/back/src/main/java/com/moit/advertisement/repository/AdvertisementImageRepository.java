package com.moit.advertisement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moit.advertisement.entity.AdvertisementImage;

public interface AdvertisementImageRepository
        extends JpaRepository<AdvertisementImage, Long> {
	
	List<AdvertisementImage> findByAdvertisement_AdId(Long adId);

}