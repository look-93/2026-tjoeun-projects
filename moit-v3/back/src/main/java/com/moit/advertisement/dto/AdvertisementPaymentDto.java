package com.moit.advertisement.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.moit.advertisement.enums.AdGrade;
import com.moit.advertisement.enums.AdPosition;
import com.moit.advertisement.enums.PaymentHistoryStatus;
import com.moit.advertisement.enums.PaymentType;

import lombok.Data;

@Data
public class AdvertisementPaymentDto {

    private Long paymentId;

    private Long adId;
    private String adTitle;

    private Long advertiserId;
    
    private String advertiserNickname;
    private AdGrade adGrade;

    private PaymentType paymentType;

    private String orderId;
    private String paymentKey;

    private BigDecimal baseAmount;
    private BigDecimal positionAmount;
    private BigDecimal amount;

    private AdPosition position;

    private PaymentHistoryStatus paymentStatus;

    private String paymentMethod;

    private LocalDateTime requestedAt;
    private LocalDateTime paidAt;
    private LocalDateTime cancelledAt;

    private String cancelReason;

    private Integer periodDays;

    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;

    private LocalDateTime createdAt;
}