package com.moit.advertisement.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.moit.advertisement.enums.AdPosition;

@Entity
@Table(name = "ADVERTISEMENT_IMAGES")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdvertisementImage {

    // 광고 이미지 PK
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "advertisement_image_seq"
    )
    @SequenceGenerator(
        name = "advertisement_image_seq",
        sequenceName = "ADVERTISEMENT_IMAGE_SEQ",
        allocationSize = 1
    )
    @Column(name = "IMAGE_ID")
    private Long imageId;


    // 이미지가 등록된 광고
    @ManyToOne
    @JoinColumn(name = "AD_ID", nullable = false)
    private Advertisement advertisement;


    // 이미지 노출 위치
    // MAIN / MEETUP_LIST_BANNER /
    // MEETUP_LIST_SIDEBAR / MEETUP_DETAIL_SIDEBAR
    @Enumerated(EnumType.STRING)
    @Column(name = "IMAGE_TYPE", length = 30, nullable = false)
    private AdPosition imageType;


    // 저장된 이미지 URL
    @Column(name = "IMAGE_URL", length = 500, nullable = false)
    private String imageUrl;


    // 이미지 등록일시
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;


    // Entity 최초 저장 시 등록일시 자동 설정
    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}