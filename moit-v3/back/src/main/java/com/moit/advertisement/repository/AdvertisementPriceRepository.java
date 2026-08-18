package com.moit.advertisement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moit.advertisement.entity.AdvertisementPrice;
import com.moit.advertisement.enums.AdGrade;
import com.moit.advertisement.enums.PaymentType;

public interface AdvertisementPriceRepository
        extends JpaRepository<AdvertisementPrice, Long> {
	
	List<AdvertisementPrice> findAllByOrderByPaymentTypeAscAdGradeAscPeriodDaysAsc();
	
	Optional<AdvertisementPrice> findByPaymentTypeAndAdGradeAndPeriodDays(
			PaymentType paymentType,
            AdGrade adGrade,
            Integer periodDays
    );

}