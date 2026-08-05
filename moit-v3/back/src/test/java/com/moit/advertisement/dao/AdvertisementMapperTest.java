package com.moit.advertisement.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.moit.advertisement.dto.AdvertisementChartDto;
import com.moit.advertisement.dto.AdvertisementDto;
import com.moit.advertisement.dto.AdvertisementImageDto;

@SpringBootTest
class AdvertisementMapperTest {

    @Autowired
    private AdvertisementMapper advertisementMapper;


    // =========================================================
    // Mapper Bean 생성 확인
    // =========================================================

    @Test
    @DisplayName("광고 Mapper Bean 생성 테스트")
    void mapperBeanTest() {

        assertThat(advertisementMapper)
                .isNotNull();
    }


    // =========================================================
    // 광고 조회
    // =========================================================

    @Test
    @DisplayName("광고 Mapper - 광고 1건 조회")
    void selectAdvertisementTest() {

        AdvertisementDto result =
                advertisementMapper.selectAdvertisementOne(1L);

        System.out.println("===== 광고 조회 =====");
        System.out.println("result = " + result);

        assertThat(result)
                .isNotNull();
    }


    // =========================================================
    // 광고 목록
    // =========================================================

    @Test
    @DisplayName("광고 Mapper - 광고 이미지 조회")
    void selectAdvertisementImageTest() {

        List<AdvertisementImageDto> result =
                advertisementMapper
                        .selectAdvertisementImageList(1L);

        System.out.println("===== 광고 이미지 =====");
        System.out.println("이미지 개수 = " + result.size());

        result.forEach(image ->
                System.out.println(
                        "imageId = " + image.getImageId()
                        + ", adId = " + image.getAdId()
                        + ", type = " + image.getImageType()
                        + ", url = " + image.getImageUrl()
                )
        );

        assertThat(result)
                .isNotNull();
    }


    // =========================================================
    // 광고 통계
    // =========================================================

    @Test
    @DisplayName("광고 Mapper - 전체 통계 조회")
    void selectSummaryTest() {

        AdvertisementChartDto result =
                advertisementMapper.selectSummary();

        System.out.println("===== 광고 전체 통계 =====");
        System.out.println("result = " + result);

        assertThat(result)
                .isNotNull();
    }


    // =========================================================
    // 광고 상태별 개수
    // =========================================================

    @Test
    @DisplayName("광고 Mapper - 상태별 광고 개수 조회")
    void selectAdvertisementCountTest() {

        int total =
                advertisementMapper.selectTotalAdvertisementCnt();

        int open =
                advertisementMapper.selectOpenAdvertisementCnt();

        int pending =
                advertisementMapper.selectPendingAdvertisementCnt();

        int closed =
                advertisementMapper.selectClosedAdvertisementCnt();

        System.out.println("===== 광고 상태별 개수 =====");
        System.out.println("전체 = " + total);
        System.out.println("OPEN = " + open);
        System.out.println("PENDING = " + pending);
        System.out.println("CLOSED = " + closed);

        assertThat(total)
                .isGreaterThanOrEqualTo(0);

        assertThat(open)
                .isGreaterThanOrEqualTo(0);

        assertThat(pending)
                .isGreaterThanOrEqualTo(0);

        assertThat(closed)
                .isGreaterThanOrEqualTo(0);
    }


    // =========================================================
    // 광고 통계 차트
    // =========================================================

    @Test
    @DisplayName("광고 Mapper - 일일 통계 조회")
    void selectDailyChartTest() {

        List<AdvertisementChartDto> result =
                advertisementMapper.selectDailyChart();

        System.out.println("===== 일일 통계 =====");
        System.out.println("데이터 개수 = " + result.size());

        result.forEach(stat ->
                System.out.println(
                        "date = " + stat.getStatDate()
                        + ", impressions = " + stat.getImpressions()
                        + ", clicks = " + stat.getClicks()
                )
        );

        assertThat(result)
                .isNotNull();
    }


    @Test
    @DisplayName("광고 Mapper - CTR TOP5 조회")
    void selectTopCtrChartTest() {

        List<AdvertisementChartDto> result =
                advertisementMapper.selectTopCtrChart();

        System.out.println("===== CTR TOP5 =====");

        result.forEach(stat ->
                System.out.println(
                        "title = " + stat.getTitle()
                        + ", ctr = " + stat.getCtr()
                )
        );

        assertThat(result)
                .isNotNull();
    }


    @Test
    @DisplayName("광고 Mapper - 위치별 노출 조회")
    void selectPositionChartTest() {

        List<AdvertisementChartDto> result =
                advertisementMapper.selectPositionChart();

        System.out.println("===== 위치별 노출 =====");

        result.forEach(stat ->
                System.out.println(
                        "position = " + stat.getPosition()
                        + ", impressions = "
                        + stat.getImpressions()
                )
        );

        assertThat(result)
                .isNotNull();
    }


    @Test
    @DisplayName("광고 Mapper - 위치별 CTR 조회")
    void selectPositionCtrChartTest() {

        List<AdvertisementChartDto> result =
                advertisementMapper.selectPositionCtrChart();

        System.out.println("===== 위치별 CTR =====");

        result.forEach(stat ->
                System.out.println(
                        "position = " + stat.getPosition()
                        + ", ctr = " + stat.getCtr()
                )
        );

        assertThat(result)
                .isNotNull();
    }
}