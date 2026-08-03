package com.moit.advertisement.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "advertisement_target_region")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdvertisementTargetRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "target_region_id")
    private Integer targetRegionId;

    @Column(name = "ad_id", nullable = false)
    private Integer adId;

    @Column(name = "region_code", length = 50, nullable = false)
    private String regionCode;
}