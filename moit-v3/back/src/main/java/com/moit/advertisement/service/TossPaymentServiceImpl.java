package com.moit.advertisement.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.moit.advertisement.dto.PaymentConfirmRequestDto;
import com.moit.advertisement.entity.Advertisement;
import com.moit.advertisement.entity.AdvertisementPayment;
import com.moit.advertisement.enums.AdStatus;
// import com.moit.advertisement.enums.PaymentHistoryStatus; // 본인 패키지에 맞게 주석 해제
// import com.moit.advertisement.enums.PaymentStatus; // 본인 패키지에 맞게 주석 해제
import com.moit.advertisement.repository.AdvertisementPaymentRepository;
import com.moit.advertisement.repository.AdvertisementRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TossPaymentServiceImpl implements TossPaymentService {

    private final AdvertisementPaymentRepository paymentRepository;
    private final AdvertisementRepository advertisementRepository;

    
    @Value("${toss.secret-key}")
    private String tossSecretKey;

    @Override
    @Transactional
    public void confirmPayment(PaymentConfirmRequestDto requestDto) {
    	System.out.println("🔥 프론트에서 넘어온 orderId: [" + requestDto.getOrderId() + "]");
        System.out.println("🔥 프론트에서 넘어온 amount: [" + requestDto.getAmount() + "]");
    	System.out.println("🔥 주입된 토스 시크릿키: [" + tossSecretKey + "]");
        
        // 1. 주문번호로 결제 내역 조회
        AdvertisementPayment payment = paymentRepository.findByOrderId(requestDto.getOrderId())
                .orElseThrow(() -> {
                    System.out.println("❌ DB에 이 orderId가 없음!!");
                    return new IllegalArgumentException("존재하지 않는 주문번호입니다.");
                });

        // 2. 금액 검증 (위변조 방지)
        if (payment.getAmount().compareTo(requestDto.getAmount()) != 0) {
            throw new IllegalArgumentException("결제 금액이 일치하지 않습니다.");
        }

        // 3. 토스 승인 API 호출 세팅
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        
        // 시크릿 키 Base64 인코딩 (Toss 요구사항: 시크릿키 뒤에 콜론(:)을 붙여 인코딩)
        String authHeader = "Basic " + Base64.getEncoder()
                .encodeToString((tossSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        
        headers.set("Authorization", authHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("paymentKey", requestDto.getPaymentKey());
        body.put("orderId", requestDto.getOrderId());
        body.put("amount", requestDto.getAmount());

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            // 4. API 통신
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.tosspayments.com/v1/payments/confirm",
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
            	// 1. 결제 이력(History) 성공 처리
                payment.updatePaymentSuccess(requestDto.getPaymentKey()); 
                
                // 2. 광고 본체 가져오기
                Advertisement advertisement = payment.getAdvertisement();
                
                // 3. 광고 결제 완료 상태로 변경 (엔티티에 이미 만들어져 있는 메서드 호출!)
                advertisement.completeInitialPayment();
                
                // 4. 운영 상태를 OPEN (진행중)으로 변경
                advertisement.changeStatus(AdStatus.OPEN);
            } else {
                throw new RuntimeException("토스 결제 승인 실패");
            }
        } catch (Exception e) {
            // 통신 실패나 잔액 부족 등
            // payment.updatePaymentFailed("승인 실패: " + e.getMessage());
            throw new RuntimeException("결제 승인 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}