package com.moit.advertisement.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moit.advertisement.entity.AdvertisementPositionPrice;
import com.moit.advertisement.entity.AdvertisementPrice;
import com.moit.advertisement.enums.AdGrade;
import com.moit.advertisement.enums.AdPosition;
import com.moit.advertisement.enums.PaymentType;
import com.moit.advertisement.repository.AdvertisementPositionPriceRepository;
import com.moit.advertisement.repository.AdvertisementPriceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdvertisementCalculationServiceImpl implements AdvertisementCalculationService {

    private final AdvertisementPriceRepository priceRepository;
    private final AdvertisementPositionPriceRepository positionPriceRepository;

    @Override
    public BigDecimal calculateTotalAmount(
            LocalDateTime startDatetime, 
            LocalDateTime endDatetime, 
            AdGrade adGrade, 
            PaymentType paymentType, 
            List<AdPosition> positions) {

        if (startDatetime == null || endDatetime == null) {
            return BigDecimal.ZERO;
        }

        // 1. 기간(일수) 계산 (올림 처리)
        long diffHours = Duration.between(startDatetime, endDatetime).toHours();
        int totalDays = (int) Math.ceil((double) diffHours / 24.0);

        if (totalDays <= 0) {
            return BigDecimal.ZERO;
        }

        // 2. 기본 광고비 계산
        BigDecimal basePrice = calculateBasePrice(totalDays, adGrade, paymentType);

        // 3. 위치 추가금 합산
        BigDecimal positionPrice = calculatePositionExtra(positions);

        // 4. 최종 금액 반환 (기본요금 + 위치요금)
        return basePrice.add(positionPrice);
    }

    /**
     * 기본 광고비 계산 로직 (일수가 큰 것부터 차감)
     */
    private BigDecimal calculateBasePrice(int totalDays, AdGrade adGrade, PaymentType paymentType) {
        BigDecimal totalBase = BigDecimal.ZERO;
        int remainingDays = totalDays;

        // DB에서 내림차순(예: 90 -> 60 -> 30)으로 가격표를 가져옴
        List<AdvertisementPrice> priceList = priceRepository
                .findByPaymentTypeAndAdGradeOrderByPeriodDaysDesc(paymentType, adGrade);

        for (AdvertisementPrice price : priceList) {
            int currentPeriod = price.getPeriodDays();
            int count = remainingDays / currentPeriod;

            if (count > 0) {
                BigDecimal countBd = BigDecimal.valueOf(count);
                BigDecimal addedPrice = price.getBasePrice().multiply(countBd);
                
                totalBase = totalBase.add(addedPrice);
                remainingDays -= (currentPeriod * count);
            }
        }

        return totalBase;
    }

    /**
     * 위치별 추가 요금 계산 로직
     */
    private BigDecimal calculatePositionExtra(List<AdPosition> positions) {
        if (positions == null || positions.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalExtra = BigDecimal.ZERO;

        // IN 쿼리로 선택된 위치들의 가격을 한 번에 가져옴
        List<AdvertisementPositionPrice> positionPrices = 
                positionPriceRepository.findByPositionIn(positions);

        for (AdvertisementPositionPrice pp : positionPrices) {
            totalExtra = totalExtra.add(pp.getAdditionalPrice());
        }

        return totalExtra;
    }
}