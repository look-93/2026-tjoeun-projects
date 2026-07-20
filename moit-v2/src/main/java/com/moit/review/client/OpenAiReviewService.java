package com.moit.review.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moit.review.dto.ReviewDto;

@Service
public class OpenAiReviewService {

	@Value("${openai.api.key}")
	private String apiKey;

	public String reviewAnalysis(List<ReviewDto> reviewList) {
		
		// 후기가 없을 경우 예외 처리
		if (reviewList == null || reviewList.isEmpty()) {
			return "등록된 모임 후기가 없어 AI 분석을 진행할 수 없습니다.";
		}
		
		// 후기 내용 합치기
		StringBuilder reviewText = new StringBuilder();
		for(ReviewDto review : reviewList) {
			reviewText.append("- ")
					  .append(review.getContent())
					  .append("\n");
		}

		// 요청하신 4번, 5번 항목만 집중적으로 반환하도록 프롬프트 구성
		String prompt = """
				다음은 한 모임에 대한 후기 목록입니다.

				%s

				위 후기들을 분석해서 오직 아래의 2가지 항목만 한국어로 요약 및 작성해주세요. 다른 정보나 서론은 일절 제외하세요.

				1. 운영자에게 추천하는 개선 방안
				2. 한 줄 총평

				답변은 보기 쉽게 작성해주세요.
				""".formatted(reviewText.toString());
		
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(apiKey);
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		Map<String, Object> body = new HashMap<>();
		body.put("model", "gpt-4o-mini"); 
		
		List<Map<String, String>> messages = new ArrayList<>();
		Map<String, String> userMessage = new HashMap<>();
		userMessage.put("role", "user");
		userMessage.put("content", prompt);
		messages.add(userMessage);
		
		body.put("messages", messages);
		
		HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
		
		try {
			// [수정] 빈 주입 예외(500 에러)를 방지하기 위해 RestTemplate 객체를 직접 생성합니다.
			RestTemplate restTemplate = new RestTemplate();
			
			ResponseEntity<String> response = restTemplate.postForEntity(
					"https://api.openai.com/v1/chat/completions",
					request,
					String.class
			);
			
			String json = response.getBody();
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(json);
			
			return root.path("choices").get(0).path("message").path("content").asText();
			
		} catch(Exception e) {
			// 자바 콘솔창에 진짜 에러 원인이 무엇인지 상세히 찍어주도록 설정
			System.err.println("=== OpenAI API 호출 중 예외 발생 ===");
			e.printStackTrace(); 
			return "AI 분석 중 오류가 발생했습니다. 원인: " + e.getMessage();
		}
	}
}