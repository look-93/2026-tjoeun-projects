package com.moit.advertisement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moit.advertisement.entity.Advertisement;

public interface AdvertisementRepository
        extends JpaRepository<Advertisement, Long> {

}