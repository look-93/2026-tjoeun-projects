package com.moit.advertisement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moit.advertisement.entity.AdvertisementImage;

public interface AdvertisementImageRepository
        extends JpaRepository<AdvertisementImage, Long> {

}