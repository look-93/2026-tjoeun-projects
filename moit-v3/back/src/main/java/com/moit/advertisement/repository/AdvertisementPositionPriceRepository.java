package com.moit.advertisement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moit.advertisement.entity.AdvertisementPositionPrice;

public interface AdvertisementPositionPriceRepository
        extends JpaRepository<AdvertisementPositionPrice, Long> {

}