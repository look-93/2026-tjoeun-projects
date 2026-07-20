package com.moit.reports.api;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ApiOpenAi {
	
	@Value("${openai.api.key}")
	private String apiKey;
	@Value("${openai.model}")
	private String model;
	
	
	private static final String API_URL="https://api.openai.com/v1/chat/completions";
	private final ObjectMapper objectMapper = new ObjectMapper(); // json -> java
	private RestClient restClient; // 외부 api에 http 요청
	
	public ApiOpenAi(RestClient.Builder restClientBuilder) {
		this.restClient = restClientBuilder.baseUrl(API_URL).build();
	}
	
	public String getAIResponse(String keywords) {
		Map<String, Object> body = Map.of(
			"model", model,
			"messages", List.of( 
				Map.of(
					"role", "developer",
					"content",
					"\n사용자가 입력한 키워드를 기반으로 신고 내용을 작성하세요."
					+ "\n사실을 임의로 추가하거나 과장하지 마세요."
					+ "\n정중하고 객관적인 문장으로 작성하세요."
					+ "\n결과는 신고 내용만 출력하세요."
					+ "\n150자 이내로 작성하세요."
				),
				Map.of(
					"role", "user",
					"content", keywords
				)
			)
		);
		// user ->
		// content ->
		
		try {
			// RestClient 스타일세팅 값 받아오기
			String responseBody = restClient.post()
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + apiKey)
					.body(body)
					.retrieve()
					.body(String.class);
			
			// json 파싱
			JsonNode root = objectMapper.readTree(responseBody);
			
			return root.path("choices")
					.get(0)
					.path("message")
					.path("content")
					.asText();
			
		} catch(Exception e) {
			throw new RuntimeException( "신고 내용 작성 ai 응답 실패", e );
		}
		
	}
	
	
}
