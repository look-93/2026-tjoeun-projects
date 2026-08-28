package com.moit.advertisement.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moit.advertisement.dto.AdvertisementCalculationResultDto;
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

        return calculate(
                startDatetime,
                endDatetime,
                adGrade,
                paymentType,
                positions
        ).getTotalAmount();
    }

    @Override
    public AdvertisementCalculationResultDto calculate(
            LocalDateTime startDatetime,
            LocalDateTime endDatetime,
            AdGrade adGrade,
            PaymentType paymentType,
            List<AdPosition> positions) {

        if (startDatetime == null || endDatetime == null) {
            return AdvertisementCalculationResultDto.builder()
                    .totalDays(0)
                    .basePrice(BigDecimal.ZERO)
                    .positionPrice(BigDecimal.ZERO)
                    .totalAmount(BigDecimal.ZERO)
                    .build();
        }

        // 기간 계산
        long diffMinutes = Duration.between(startDatetime, endDatetime).toMinutes();

        int totalDays = (int) Math.ceil((double) diffMinutes / (24 * 60.0));

        if (totalDays <= 0) {
            return AdvertisementCalculationResultDto.builder()
                    .totalDays(0)
                    .basePrice(BigDecimal.ZERO)
                    .positionPrice(BigDecimal.ZERO)
                    .totalAmount(BigDecimal.ZERO)
                    .build();
        }

        // 기본 광고비
        BigDecimal basePrice =
                calculateBasePrice(
                        totalDays,
                        adGrade,
                        paymentType
                );

        // 위치 추가금
        BigDecimal positionPrice =
                calculatePositionExtra(positions);

        // 최종 금액
        BigDecimal totalAmount =
                basePrice.add(positionPrice);

        return AdvertisementCalculationResultDto.builder()
                .totalDays(totalDays)
                .basePrice(basePrice)
                .positionPrice(positionPrice)
                .totalAmount(totalAmount)
                .build();
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
    
    @Override
    public int calculateTotalDays(
            LocalDateTime startDatetime,
            LocalDateTime endDatetime) {

        if (startDatetime == null || endDatetime == null) {
            return 0;
        }

        long diffMinutes = Duration.between(startDatetime, endDatetime).toMinutes();

        return (int) Math.ceil( (double) diffMinutes / (24 * 60.0) );
    }
}