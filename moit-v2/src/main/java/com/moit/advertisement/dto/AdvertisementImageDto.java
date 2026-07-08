package com.moit.advertisement.dto;

import lombok.Data;

@Data
public class AdvertisementImageDto {

    // 이미지 PK
    private Integer imageId;

    // 광고 PK
    private Integer adId;

    // 이미지 종류
    private String imageType;

    // 이미지 경로
    private String imageUrl;

}