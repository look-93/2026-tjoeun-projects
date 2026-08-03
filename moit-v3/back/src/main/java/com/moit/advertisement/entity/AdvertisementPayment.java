package com.moit.advertisement.entity;

import com.moit.advertisement.enums.PaymentStatus;
import com.moit.advertisement.enums.PaymentType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "advertisement_payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdvertisementPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;

    @Column(name = "ad_id", nullable = false)
    private Integer adId;

    @Column(name = "advertiser_id", nullable = false)
    private Integer advertiserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", length = 20, nullable = false)
    private PaymentType paymentType;

    @Column(name = "order_id", length = 100, nullable = false, unique = true)
    private String orderId;

    @Column(name = "payment_key", length = 200)
    private String paymentKey;

    @Column(name = "base_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal baseAmount;

    @Column(name = "position_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal positionAmount;

    @Column(name = "amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 20, nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "payment_method", length = 30)
    private String paymentMethod;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancel_reason", length = 500)
    private String cancelReason;

    @Column(name = "period_days")
    private Integer periodDays;

    @Column(name = "start_datetime")
    private LocalDateTime startDatetime;

    @Column(name = "end_datetime")
    private LocalDateTime endDatetime;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}