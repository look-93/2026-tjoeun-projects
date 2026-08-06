package com.moit.advertisement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.moit.advertisement.dao.AdvertisementMapper;
import com.moit.advertisement.dto.AdvertisementDto;

@SpringBootTest
class AdvertisementMapperTest {

    @Autowired
    private AdvertisementMapper advertisementMapper;

    @Test
    @DisplayName("광고 Mapper - 실제 DB 조회")
    void selectAdvertisementTest() {

        AdvertisementDto result = advertisementMapper.selectAdvertisementOne(1L);
        System.out.println("result = " + result);
        assertThat(result).isNotNull();
    }
}