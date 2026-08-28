package com.moit.advertisement.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.moit.advertisement.dto.PaymentConfirmRequestDto;
import com.moit.advertisement.entity.Advertisement;
import com.moit.advertisement.entity.AdvertisementPayment;
import com.moit.advertisement.enums.AdStatus;
import com.moit.advertisement.enums.PaymentType;
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
                .encodeToString((tossSecretKey+ ":").getBytes(StandardCharsets.UTF_8));
        
        headers.set("Authorization", authHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("paymentKey", requestDto.getPaymentKey());
        body.put("orderId", requestDto.getOrderId());
        body.put("amount", requestDto.getAmount());

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            // 4. API 통신
        	ResponseEntity<JsonNode> response = restTemplate.postForEntity(
        	        "https://api.tosspayments.com/v1/payments/confirm",
        	        requestEntity,
        	        JsonNode.class
        	);

        	if (response.getStatusCode().is2xxSuccessful()) {

        	    JsonNode rootNode = response.getBody();

        	    String tossMethod = rootNode.path("method").asText();

        	    System.out.println("🔥 Toss 결제수단: [" + tossMethod + "]");

        	    String paymentMethod;

        	    switch (tossMethod) {

        	        case "카드":
        	            paymentMethod = "CARD";
        	            break;

        	        case "간편결제":
        	            paymentMethod = "EASY_PAY";
        	            break;

        	        case "가상계좌":
        	            paymentMethod = "VIRTUAL_ACCOUNT";
        	            break;

        	        case "계좌이체":
        	            paymentMethod = "TRANSFER";
        	            break;

        	        case "휴대폰":
        	            paymentMethod = "MOBILE";
        	            break;

        	        default:
        	            paymentMethod = "OTHER";
        	            break;
        	    }

        	    System.out.println("🔥 DB 저장용 결제수단: [" + paymentMethod + "]");
        	    
            	// 1. 결제 이력(History) 성공 처리
                payment.updatePaymentSuccess(requestDto.getPaymentKey(), paymentMethod); 
                
                // 2. 광고 본체 가져오기
                Advertisement advertisement = payment.getAdvertisement();
                
             // 결제 유형에 따른 광고 상태 처리
                if (payment.getPaymentType() == PaymentType.INITIAL) {

                    // 최초 결제
                    advertisement.completeInitialPayment();

                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime startDateTime =
                            advertisement.getStartDatetime();

                    if (startDateTime != null && now.isBefore(startDateTime)) {
                        advertisement.changeStatus( AdStatus.PENDING );
                    } else {
                        advertisement.changeStatus( AdStatus.OPEN );
                    }

                } else if (payment.getPaymentType() == PaymentType.EXTENSION) {

                    // 연장 결제
                    advertisement.completeExtensionPayment();

                    // 연장 일수 가져오기
                    Integer periodDays =
                            payment.getPeriodDays();

                    if (periodDays == null) {
                        throw new IllegalArgumentException(
                                "연장 기간 정보가 없습니다."
                        );
                    }

                    advertisement.extendEndDatetime( periodDays );
                } else {
                    throw new IllegalArgumentException( "지원하지 않는 결제 유형입니다." );
                }
            } else {
                throw new RuntimeException("토스 결제 승인 실패");
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new RuntimeException("토스 결제 승인 실패: " + e.getMessage(), e);
        }
    }
}