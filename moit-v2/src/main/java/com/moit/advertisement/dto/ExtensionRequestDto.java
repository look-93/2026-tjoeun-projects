package com.moit.advertisement.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ExtensionRequestDto {

    private int adId;
    private int advertiserId;
    private LocalDateTime extensionRequestEndDatetime;

}