package com.moit.advertisement.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "ADVERTISEMENT_TARGET_REGION")
public class AdvertisementTargetRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "advertisement_target_region_seq")
    @SequenceGenerator(
        name = "advertisement_target_region_seq",
        sequenceName = "ADVERTISEMENT_TARGET_REGION_SEQ",
        allocationSize = 1
    )
    @Column(name = "TARGET_REGION_ID")
    private Integer targetRegionId;

    @ManyToOne
    @JoinColumn(name = "AD_ID", nullable = false)
    private Advertisement advertisement;

    @Column(name = "REGION_CODE", length = 50, nullable = false)
    private String regionCode;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}