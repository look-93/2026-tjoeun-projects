package com.moit.member.service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PhoneVerificationService {

    private final StringRedisTemplate redisTemplate;
    private final SolapiSmsService solapiSmsService;

    private final SecureRandom random = new SecureRandom();

    // 인증번호 Redis Key
    private static final String PREFIX = "phone:verification:";

    // 인증 완료 Redis Key
    private static final String VERIFIED_PREFIX = "phone:verified:";

    // 인증번호 유효시간 : 2분
    private static final long EXPIRE_MINUTES = 2;

    // 인증 완료 상태 유효시간 : 10분
    private static final long VERIFIED_EXPIRE_MINUTES = 10;


    // =====================================================
    // 인증번호 발송
    // =====================================================
    public void sendVerificationCode(String mobile) {

        // 1. 인증번호 6자리 생성
        String code = generateCode();

        // 2. SMS 발송
        solapiSmsService.sendVerificationCode(mobile, code);

        // 3. SMS 발송 성공 후 Redis 저장
        String key = PREFIX + mobile;

        redisTemplate.opsForValue().set(
                key,
                code,
                EXPIRE_MINUTES,
                TimeUnit.MINUTES
        );

        // 기존 인증 완료 상태가 있다면 삭제
        String verifiedKey = VERIFIED_PREFIX + mobile;

        redisTemplate.delete(verifiedKey);

        System.out.println("===== 휴대폰 인증번호 Redis 저장 성공 =====");
        System.out.println("mobile: " + mobile);
        System.out.println("key: " + key);
        System.out.println("expire: " + EXPIRE_MINUTES + "분");
    }


    // =====================================================
    // 인증번호 확인
    // =====================================================
    public boolean verifyCode(
            String mobile,
            String code
    ) {

        String key = PREFIX + mobile;

        // Redis에서 인증번호 조회
        String savedCode = redisTemplate.opsForValue().get(key);

        System.out.println("===== 휴대폰 인증번호 확인 =====");
        System.out.println("mobile: " + mobile);
        System.out.println("입력 코드: " + code);
        System.out.println("Redis 코드: " + savedCode);

        // 인증번호가 없거나 만료됨
        if (savedCode == null) {
            System.out.println("인증번호 없음 또는 만료");
            return false;
        }

        // 인증번호 불일치
        if (!savedCode.equals(code)) {
            System.out.println("인증번호 불일치");
            return false;
        }

        // =================================================
        // 인증 성공
        // =================================================

        // 인증번호 삭제
        redisTemplate.delete(key);

        // 인증 완료 상태 저장
        String verifiedKey = VERIFIED_PREFIX + mobile;

        redisTemplate.opsForValue().set(
                verifiedKey,
                "true",
                VERIFIED_EXPIRE_MINUTES,
                TimeUnit.MINUTES
        );

        System.out.println("===== 휴대폰 인증 성공 =====");
        System.out.println("verifiedKey: " + verifiedKey);
        System.out.println("인증 완료 상태: true");

        return true;
    }


    // =====================================================
    // 휴대폰 인증 완료 여부
    // =====================================================
    public boolean isPhoneVerified(String mobile) {

        String verifiedKey = VERIFIED_PREFIX + mobile;

        String value = redisTemplate.opsForValue().get(verifiedKey);

        boolean verified = "true".equals(value);

        System.out.println("===== 휴대폰 인증 상태 확인 =====");
        System.out.println("mobile: " + mobile);
        System.out.println("verifiedKey: " + verifiedKey);
        System.out.println("Redis value: " + value);
        System.out.println("verified: " + verified);

        return verified;
    }


    // =====================================================
    // 휴대폰 인증 완료 상태 삭제
    // =====================================================
    public void removePhoneVerified(String mobile) {

        String verifiedKey = VERIFIED_PREFIX + mobile;

        redisTemplate.delete(verifiedKey);

        System.out.println("===== 휴대폰 인증 완료 상태 삭제 =====");
        System.out.println("mobile: " + mobile);
    }


    // =====================================================
    // 인증번호 생성
    // =====================================================
    private String generateCode() {

        int number = 100000 + random.nextInt(900000);

        return String.valueOf(number);
    }
}