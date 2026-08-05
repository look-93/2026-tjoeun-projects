package com.moit.advertisement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moit.advertisement.entity.AdvertisementPayment;
import com.moit.advertisement.enums.PaymentHistoryStatus;

public interface AdvertisementPaymentRepository
        extends JpaRepository<AdvertisementPayment, Long> {
	
	List<AdvertisementPayment> findByAdvertisement_AdId(Long adId);
    List<AdvertisementPayment> findByPaymentStatus(PaymentHistoryStatus paymentStatus);
    Optional<AdvertisementPayment> findByOrderId(String orderId);

}