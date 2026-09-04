package com.moit.advertisement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.moit.advertisement.entity.Advertisement;
import com.moit.advertisement.entity.AdvertisementDailyStatistics;
import com.moit.advertisement.entity.AdvertisementImage;
import com.moit.advertisement.entity.AdvertisementPayment;
import com.moit.advertisement.entity.AdvertisementPositionPrice;
import com.moit.advertisement.entity.AdvertisementPrice;
import com.moit.advertisement.entity.AdvertisementTargetRegion;
import com.moit.member.entity.Member;
import com.moit.member.repository.MemberRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableJpaRepositories(
    basePackageClasses = {
        AdvertisementRepository.class,
        AdvertisementImageRepository.class,
        AdvertisementTargetRegionRepository.class,
        AdvertisementPaymentRepository.class,
        AdvertisementPriceRepository.class,
        AdvertisementPositionPriceRepository.class,
        AdvertisementDailyStatisticsRepository.class,
        MemberRepository.class
    }
)
@ImportAutoConfiguration(exclude = {
    JpaRepositoriesAutoConfiguration.class
})
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
    
    @Autowired
    private MemberRepository memberRepository;


    // =========================================================
    // Repository Bean 생성 테스트
    // =========================================================

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


    // =========================================================
    // 실제 Oracle DB 조회 테스트
    // =========================================================
    @Test
    @DisplayName("광고 Repository - 실제 DB 조회")
    void advertisementSelectTest() {

        List<Advertisement> advertisements =
                advertisementRepository.findAll();

        assertThat(advertisements).isNotNull();
    }


    @Test
    @DisplayName("광고 이미지 Repository - 실제 DB 조회")
    void advertisementImageSelectTest() {

        List<AdvertisementImage> images =
                advertisementImageRepository.findAll();

        assertThat(images).isNotNull();
    }


    @Test
    @DisplayName("광고 지역 Repository - 실제 DB 조회")
    void advertisementTargetRegionSelectTest() {

        List<AdvertisementTargetRegion> regions =
                advertisementTargetRegionRepository.findAll();

        assertThat(regions).isNotNull();

    }


    @Test
    @DisplayName("광고 결제 Repository - 실제 DB 조회")
    void advertisementPaymentSelectTest() {

        List<AdvertisementPayment> payments =
                advertisementPaymentRepository.findAll();

        assertThat(payments).isNotNull();
    }


    @Test
    @DisplayName("광고 가격 Repository - 실제 DB 조회")
    void advertisementPriceSelectTest() {

        List<AdvertisementPrice> prices =
                advertisementPriceRepository.findAll();

        assertThat(prices).isNotNull();

    }


    @Test
    @DisplayName("광고 위치별 가격 Repository - 실제 DB 조회")
    void advertisementPositionPriceSelectTest() {

        List<AdvertisementPositionPrice> prices =
                advertisementPositionPriceRepository.findAll();

        assertThat(prices).isNotNull();
    }


    @Test
    @DisplayName("광고 일일통계 Repository - 실제 DB 조회")
    void advertisementDailyStatisticsSelectTest() {

        List<AdvertisementDailyStatistics> statistics =
                advertisementDailyStatisticsRepository.findAll();

        assertThat(statistics).isNotNull();
    }
}
