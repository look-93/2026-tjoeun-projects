package com.moit.advertisement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.moit.advertisement.entity.AdvertisementPayment;
import com.moit.advertisement.enums.PaymentHistoryStatus;

public interface AdvertisementPaymentRepository
        extends JpaRepository<AdvertisementPayment, Long> {
	
	List<AdvertisementPayment> findByAdvertisement_AdId(Long adId);
    List<AdvertisementPayment> findByPaymentStatus(PaymentHistoryStatus paymentStatus);
    Optional<AdvertisementPayment> findByOrderId(String orderId);

    // 광고 ID와 결제 상태(REQUESTED 등)로 orderId 가져오기
    Optional<AdvertisementPayment> findByAdvertisement_AdIdAndPaymentStatus(Long adId, PaymentHistoryStatus paymentStatus);
    
    // 관리자 결제 내역
    Page<AdvertisementPayment> findAllByOrderByCreatedAtDesc( Pageable pageable );
    
    Optional<AdvertisementPayment> findTopByAdvertisement_AdIdOrderByCreatedAtDesc(Long adId);
}