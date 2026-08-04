package com.moit.advertisement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moit.advertisement.entity.AdvertisementPayment;

public interface AdvertisementPaymentRepository
        extends JpaRepository<AdvertisementPayment, Long> {

}