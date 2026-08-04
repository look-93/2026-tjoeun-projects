package com.moit.advertisement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdvertisementRepositoryTest {

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private AdvertisementImageRepository advertisementImageRepository;

    @Autowired
    private AdvertisementTargetRegionRepository advertisementTargetRegionRepository;

    @Autowired
    private AdvertisementPaymentRepository advertisementPaymentRepository;

    @Autowired
    private AdvertisementPriceRepository advertisementPriceRepository;

    @Autowired
    private AdvertisementPositionPriceRepository advertisementPositionPriceRepository;

    @Autowired
    private AdvertisementDailyStatisticsRepository advertisementDailyStatisticsRepository;

    @Test
    @DisplayName("광고 Repository 전체 Bean 생성 테스트")
    void repositoryBeanTest() {

        assertThat(advertisementRepository).isNotNull();
        assertThat(advertisementImageRepository).isNotNull();
        assertThat(advertisementTargetRegionRepository).isNotNull();
        assertThat(advertisementPaymentRepository).isNotNull();
        assertThat(advertisementPriceRepository).isNotNull();
        assertThat(advertisementPositionPriceRepository).isNotNull();
        assertThat(advertisementDailyStatisticsRepository).isNotNull();
    }
}