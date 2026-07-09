package com.moit.qna.ai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moit.qna.ai.dto.AiAnalysisResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OpenAiService {

	private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.url}")
    private String url;

    public AiAnalysisResult analyze(String text){

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String,Object> body = new HashMap<>();

        body.put("model", model);

        body.put("input", List.of(
                Map.of(
                        "role","user",
                        "content",buildPrompt(text)
                )
        ));

        HttpEntity<Map<String,Object>> entity = new HttpEntity<>(body, headers);
        String response = restTemplate.postForObject(url, entity, String.class);

        try {
            JsonNode root = objectMapper.readTree(response);
            String textResult =
                    root.path("output")
                        .get(0)
                        .path("content")
                        .get(0)
                        .path("text")
                        .asText();
            return objectMapper.readValue(textResult, AiAnalysisResult.class);
            
        } catch (Exception e) {
            throw new RuntimeException("GPT 응답 파싱 실패", e);
        }

    }
    
    private String buildPrompt(String text) {

        return """
            당신은 문의글의 공격성을 분석하는 AI이다.
            반드시 JSON 객체만 반환한다.
	        설명, 코드블록(```), 추가 문장은 절대 출력하지 않는다.
	
	        출력 형식
            {
              "analysis":"NORMAL",
              "score":0
            }
            규칙
            score는 0~100
            analysis 값은 반드시 둘 중 하나

            NORMAL
            PENDING_REVIEW

            욕설, 협박, 성희롱, 인신공격, 악성 민원은
            PENDING_REVIEW

            일반 문의는
            NORMAL

            문의 내용

            %s
            """.formatted(text);
    }

}