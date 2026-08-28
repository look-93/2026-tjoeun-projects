package com.moit.reports.api;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moit.reports.dto.AiReportsDto;
import com.moit.reports.enums.ReasonCode;
import com.moit.reports.enums.TargetType;

@Service
public class ApiOpenAi {	// 사용자 신고 작성 openAI
	
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
	
	public String getAIResponse(AiReportsDto dto) {
		String keywords = dto.getKeywords();
		ReasonCode reasonCode = dto.getReasonCode();
		TargetType targetType = dto.getTargetType();
		
		// targetTypeText
		String targetTypeText = "";
		switch (targetType) {
			case MEETUP: targetTypeText = "모임"; break;
			case REVIEW: targetTypeText = "후기"; break;
		};
		
		// reasonCodeText	(ABUSE, SPAM, FAKE_INFO, AD, NOSHOW, ETC)
		String reasonCodeText = "";
		switch (reasonCode) {
		    case ABUSE: reasonCodeText = "욕설 및 비방"; break;
		    case SPAM: reasonCodeText = "도배 및 스팸"; break;
		    case FAKE_INFO: reasonCodeText = "허위 정보"; break;
		    case AD: reasonCodeText = "광고성 게시물"; break;
		    case NOSHOW: reasonCodeText = "노쇼"; break;
		    case ETC: reasonCodeText = "기타"; break;
		}
		
		Map<String, Object> body = Map.of(
			"model", model,
			"messages", List.of( 
				Map.of("role", "developer", "content", """
						당신은 MOIT 서비스의 사용자 신고 작성 보조 AI입니다.

						사용자가 입력한 키워드와 신고 사유를 바탕으로
						관리자가 이해하기 쉬운 신고 내용을 작성하세요.

						다음 규칙을 반드시 지키세요.

						1. 사용자가 입력하지 않은 사실을 추가하지 마세요.

						2. 사용자가 입력한 내용을 과장하거나
						   더 심각한 표현으로 바꾸지 마세요.

						3. 사용자의 추측이나 감정을
						   확인된 사실처럼 표현하지 마세요.

						4. 특정 행동이 실제로 발생했다고
						   사용자가 명확히 입력하지 않았다면
						   발생한 사실처럼 작성하지 마세요.

						5. 신고 대상 종류인
						   '모임', '후기'라는 단어를
						   불필요하게 문장에 직접 넣지 마세요.

						6. 신고 사유가 문장 안에서 자연스럽게 드러나도록 작성하세요.

						7. 신고 사유 코드나 영문 코드
						   (ABUSE, SPAM, FAKE_INFO, AD, NOSHOW 등)를
						   그대로 출력하지 마세요.

						8. 욕설이나 공격적인 표현이 포함되어 있더라도
						   가능한 한 객관적이고 정중하게 표현하세요.
						   단, 신고 판단에 필요한 의미를 임의로 삭제하지 마세요.

						9. 사용자의 감정이나 평가보다
						   실제 신고하려는 행동이나 상황이 중심이 되도록 작성하세요.

						10. 날짜, 장소, 금액, 횟수, 인물, 증거,
						    대화 내용 등을 임의로 만들어내지 마세요.

						11. 신고 승인 여부나 실제 위반 여부를 판단하지 마세요.

						12. 단정적인 제재 표현을 사용하지 마세요.

						13. 설명, 제목, 번호, 따옴표 없이
						    신고 내용만 출력하세요.

						14. 가능하면 1~3문장,
						    전체 길이는 150자 이내로 작성하세요.
						"""),

				Map.of("role", "user", "content", """
						아래 입력 정보를 바탕으로 신고 내용을 작성하세요.

						[신고 대상 유형]
						%s

						[신고 사유]
						%s

						[사용자가 입력한 내용]
						%s
						""".formatted(targetTypeText, reasonCodeText, keywords)
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
