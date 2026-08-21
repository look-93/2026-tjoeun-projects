package com.moit.config;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.moit.advertisement.entity.AdvertisementPositionPrice;
import com.moit.advertisement.entity.AdvertisementPrice;
import com.moit.advertisement.enums.AdGrade;
import com.moit.advertisement.enums.AdPosition;
import com.moit.advertisement.enums.PaymentType;
import com.moit.advertisement.repository.AdvertisementPositionPriceRepository;
import com.moit.advertisement.repository.AdvertisementPriceRepository;
import com.moit.member.entity.MemberStatus;
import com.moit.member.entity.MemberType;
import com.moit.member.enums.MemberStatusEnum;
import com.moit.member.enums.MemberTypeEnum;
import com.moit.member.repository.MemberStatusRepository;
import com.moit.member.repository.MemberTypeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer {
	
	private final MemberTypeRepository memberTypeRepository;
	private final MemberStatusRepository memberStatusRepository;
	
	private final AdvertisementPriceRepository priceRepository;
    private final AdvertisementPositionPriceRepository positionPriceRepository;
	
	@Bean
	public CommandLineRunner initData() {
		return args -> {
            
			// ==========================================
			// 1. 회원유형 초기 데이터
			// ==========================================
			for(MemberTypeEnum type : MemberTypeEnum.values()) {
				if(!memberTypeRepository.existsById(type.getId())) {
					MemberType memberType = new MemberType();
					memberType.setMemberTypeId(type.getId());
					memberType.setTypeName(type.name());
					memberTypeRepository.save(memberType);					
				}
			}
			
			// ==========================================
			// 2. 회원상태 초기 데이터
			// ==========================================
			for(MemberStatusEnum status : MemberStatusEnum.values()) {
				if(!memberStatusRepository.existsById(status.getId())) {
					MemberStatus memberStatus = new MemberStatus();
					memberStatus.setStatusId(status.getId());
					memberStatus.setStatusName(status.name());
					memberStatusRepository.save(memberStatus);
				}
			}

            // ==========================================
			// 기간별 광고 가격 세팅
			// ==========================================
            if (priceRepository.count() == 0) {
                log.info("🔥 [DataInit] 기간별 광고 가격 데이터가 없어 기본값을 생성합니다.");

                // 순서 주의: AdGrade, periodDays, PaymentType, basePrice
                priceRepository.saveAll(Arrays.asList(
                    // --- 신규(INITIAL) 일반 ---
                    AdvertisementPrice.create(AdGrade.GENERAL, 1, PaymentType.INITIAL, new BigDecimal("10000")),
                    AdvertisementPrice.create(AdGrade.GENERAL, 7, PaymentType.INITIAL, new BigDecimal("65000")),
                    AdvertisementPrice.create(AdGrade.GENERAL, 14, PaymentType.INITIAL, new BigDecimal("125000")),
                    AdvertisementPrice.create(AdGrade.GENERAL, 30, PaymentType.INITIAL, new BigDecimal("250000")),
                    AdvertisementPrice.create(AdGrade.GENERAL, 60, PaymentType.INITIAL, new BigDecimal("480000")),
                    AdvertisementPrice.create(AdGrade.GENERAL, 90, PaymentType.INITIAL, new BigDecimal("670000")),

                    // --- 신규(INITIAL) 프리미엄 ---
                    AdvertisementPrice.create(AdGrade.PREMIUM, 1, PaymentType.INITIAL, new BigDecimal("20000")),
                    AdvertisementPrice.create(AdGrade.PREMIUM, 7, PaymentType.INITIAL, new BigDecimal("130000")),
                    AdvertisementPrice.create(AdGrade.PREMIUM, 14, PaymentType.INITIAL, new BigDecimal("250000")),
                    AdvertisementPrice.create(AdGrade.PREMIUM, 30, PaymentType.INITIAL, new BigDecimal("500000")),
                    AdvertisementPrice.create(AdGrade.PREMIUM, 60, PaymentType.INITIAL, new BigDecimal("960000")),
                    AdvertisementPrice.create(AdGrade.PREMIUM, 90, PaymentType.INITIAL, new BigDecimal("1350000")),

                    // --- 연장(EXTENSION) 일반 ---
                    AdvertisementPrice.create(AdGrade.GENERAL, 7, PaymentType.EXTENSION, new BigDecimal("60000")),
                    AdvertisementPrice.create(AdGrade.GENERAL, 14, PaymentType.EXTENSION, new BigDecimal("110000")),
                    AdvertisementPrice.create(AdGrade.GENERAL, 30, PaymentType.EXTENSION, new BigDecimal("220000")),
                    AdvertisementPrice.create(AdGrade.GENERAL, 60, PaymentType.EXTENSION, new BigDecimal("420000")),
                    AdvertisementPrice.create(AdGrade.GENERAL, 90, PaymentType.EXTENSION, new BigDecimal("600000")),

                    // --- 연장(EXTENSION) 프리미엄 ---
                    AdvertisementPrice.create(AdGrade.PREMIUM, 7, PaymentType.EXTENSION, new BigDecimal("120000")),
                    AdvertisementPrice.create(AdGrade.PREMIUM, 14, PaymentType.EXTENSION, new BigDecimal("220000")),
                    AdvertisementPrice.create(AdGrade.PREMIUM, 30, PaymentType.EXTENSION, new BigDecimal("450000")),
                    AdvertisementPrice.create(AdGrade.PREMIUM, 60, PaymentType.EXTENSION, new BigDecimal("850000")),
                    AdvertisementPrice.create(AdGrade.PREMIUM, 90, PaymentType.EXTENSION, new BigDecimal("1200000"))
                ));
            }

            // ==========================================
			// 위치별 추가금 세팅 (MAIN 제거)
			// ==========================================
            if (positionPriceRepository.count() == 0) {
                log.info("🔥 [DataInit] 위치별 추가금 데이터가 없어 기본값을 생성합니다.");

                positionPriceRepository.saveAll(Arrays.asList(
                    createPositionPrice(AdPosition.MEETUP_LIST_BANNER, new BigDecimal("30000")),
                    createPositionPrice(AdPosition.MEETUP_LIST_SIDEBAR, new BigDecimal("15000")),
                    createPositionPrice(AdPosition.MEETUP_DETAIL_SIDEBAR, new BigDecimal("10000"))
                ));
            }
		};
	}

    private AdvertisementPositionPrice createPositionPrice(AdPosition position, BigDecimal price) {
        AdvertisementPositionPrice positionPrice = new AdvertisementPositionPrice();

        positionPrice.setPosition(position);
        positionPrice.setAdditionalPrice(price);
        
        return positionPrice;
    }
}