package com.moit.advertisement.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moit.advertisement.dto.DashboardAiDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiSummaryServiceImpl implements AiSummaryService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String createSummary(DashboardAiDto dto) {

    	try {
    		// 1. 프롬프트 생성
            String prompt = """
                    너는 광고 운영 전문가이다.

                    아래 데이터를 보고
                    5줄 정도로 운영 분석을 작성해라.

                    총 광고 : %d
                    총 노출 : %d
                    총 클릭 : %d
                    평균 CTR : %.2f%%
                    연장률 : %.1f%%
                    교체 권장 광고 : %d개
                    최고 CTR 위치 : %s
                    최저 CTR 위치 : %s
                    가장 많은 광고 등급 : %s

                    아래 형식만 출력한다.

                    📈 AI 운영 분석

                    • ...
                    • ...
                    • ...
                    • ...
                    • ...
                    """
                    .formatted(
                            dto.getTotalAd(),
                            dto.getTotalImp(),
                            dto.getTotalClick(),
                            dto.getAvgCtr(),
                            dto.getExtensionRate(),
                            dto.getFatigueWarningCount(),
                            dto.getBestPosition(),
                            dto.getWorstPosition(),
                            dto.getTopGrade()
                    );

            HttpHeaders headers = new HttpHeaders();

            // 2. OpenAI API 호출
            headers.setBearerAuth(apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 3. 응답 파싱
            Map<String, Object> body = new HashMap<>();

            body.put("model", "gpt-5");

            body.put("input", prompt);

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(body, headers);

            // 4. AI 요약 String 반환
            ResponseEntity<String> response =
                    restTemplate.exchange(
                            "https://api.openai.com/v1/responses",
                            HttpMethod.POST,
                            entity,
                            String.class
                    );

            JsonNode root = objectMapper.readTree(response.getBody());

            return root.get("output_text").asText();

        } catch (Exception e) {

            e.printStackTrace();

    	
            return "AI 요약 생성 실패";
    }
  }

}
