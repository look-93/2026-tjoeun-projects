package com.moit.advertisement.service;

import java.util.List;

import com.moit.advertisement.dto.AdvertisementPriceDto;

public interface AdvertisementPriceService {

    List<AdvertisementPriceDto> findAll();

    AdvertisementPriceDto findOne(Long priceId);

    AdvertisementPriceDto save(AdvertisementPriceDto dto);

    AdvertisementPriceDto update(
            Long priceId,
            AdvertisementPriceDto dto
    );

    void delete(Long priceId);
}