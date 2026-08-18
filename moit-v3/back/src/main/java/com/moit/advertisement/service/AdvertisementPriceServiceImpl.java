package com.moit.advertisement.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moit.advertisement.dto.AdvertisementPriceDto;
import com.moit.advertisement.entity.AdvertisementPrice;
import com.moit.advertisement.enums.AdGrade;
import com.moit.advertisement.enums.PaymentType;
import com.moit.advertisement.repository.AdvertisementPriceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdvertisementPriceServiceImpl
        implements AdvertisementPriceService {

    private final AdvertisementPriceRepository priceRepository;


    @Override
    public List<AdvertisementPriceDto> findAll() {

        return priceRepository
                .findAllByOrderByPaymentTypeAscAdGradeAscPeriodDaysAsc()
                .stream()
                .map(this::toDto)
                .toList();
    }


    @Override
    public AdvertisementPriceDto findOne(Long priceId) {

        AdvertisementPrice price =
                priceRepository.findById(priceId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "가격 정보를 찾을 수 없습니다."
                                )
                        );

        return toDto(price);
    }


    @Override
    @Transactional
    public AdvertisementPriceDto save(
            AdvertisementPriceDto dto) {

        validateDuplicate(
        		dto.getPaymentType(),
                dto.getAdGrade(),
                dto.getPeriodDays()
        );

        AdvertisementPrice price =
        		new AdvertisementPrice(
                        dto.getPaymentType(),
                        dto.getAdGrade(),
                        dto.getPeriodDays(),
                        dto.getBasePrice()
                );

        return toDto(
                priceRepository.save(price)
        );
    }


    @Override
    @Transactional
    public AdvertisementPriceDto update(
            Long priceId,
            AdvertisementPriceDto dto) {

        AdvertisementPrice price =
                priceRepository.findById(priceId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "가격 정보를 찾을 수 없습니다."
                                )
                        );

        if (!price.getPaymentType().equals(dto.getPaymentType())
                || !price.getAdGrade().equals(dto.getAdGrade())
                || !price.getPeriodDays().equals(dto.getPeriodDays())) {

            validateDuplicate(
                    dto.getPaymentType(),
                    dto.getAdGrade(),
                    dto.getPeriodDays()
            );
        }

        price.update(
                dto.getPaymentType(),
                dto.getAdGrade(),
                dto.getPeriodDays(),
                dto.getBasePrice()
        );

        return toDto(price);
    }


    @Override
    @Transactional
    public void delete(Long priceId) {

        if (!priceRepository.existsById(priceId)) {
            throw new IllegalArgumentException(
                    "가격 정보를 찾을 수 없습니다."
            );
        }

        priceRepository.deleteById(priceId);
    }


    private void validateDuplicate(
            PaymentType paymentType,
            AdGrade adGrade,
            Integer periodDays) {

        if (priceRepository
                .findByPaymentTypeAndAdGradeAndPeriodDays(
                        paymentType,
                        adGrade,
                        periodDays
                )
                .isPresent()) {

            throw new IllegalArgumentException(
                    "이미 등록된 결제유형, 광고등급, 기간의 가격입니다."
            );
        }
    }


    private AdvertisementPriceDto toDto(
            AdvertisementPrice price) {

        return AdvertisementPriceDto.builder()
                .priceId(price.getPriceId())
                .paymentType(price.getPaymentType())
                .adGrade(price.getAdGrade())
                .periodDays(price.getPeriodDays())
                .basePrice(price.getBasePrice())
                .build();
    }
}