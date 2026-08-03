package com.moit.advertisement.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdvertisementExtensionRequestDto {

    @NotNull
    private Long adId;

    @NotNull
    private Integer periodDays;
}

// 연장 승인대기없어져서 없어질에정같음