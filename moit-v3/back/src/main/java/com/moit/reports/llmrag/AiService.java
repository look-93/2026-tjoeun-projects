package com.moit.reports.llmrag;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AiService {	// PDFBox / OpenAI API 호출
							// 이 context와 question을 OpenAI에 보내줘
	private final RestClient openAiRestClient;
	
	public AiService( @Qualifier ("openAiRestClient") RestClient openAiRestClient ) {
        this.openAiRestClient = openAiRestClient;
    }
	
	// LLM-RAG
	// 어떤 운영 기준이 관련 있나?
	// 어떤 과거 사례가 비슷한가?
	
	// GPT
	// 추천 검토 방향
	// 관련 운영 기준
	// 유사 사례
	// 판단 이유
	
	
	// resources/docs 같은 고정 PDF 읽기
	public String extractTextFromPdf(InputStream inputStream) throws IOException {
		try (PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
			PDFTextStripper stripper = new PDFTextStripper();
			return stripper.getText(document);
		}
	}
	
	// 임베딩(글자를 검색용 숫자로 변환)
	public List<Double> createEmbedding(String text) {
		EmbeddingRequest request = new EmbeddingRequest("text-embedding-3-small", text);
		EmbeddingResponse response = openAiRestClient.post().uri("/embeddings").body(request).retrieve()
				.body(EmbeddingResponse.class);

		if (response == null || response.data() == null || response.data().isEmpty()) {
			throw new IllegalStateException("Embedding 생성에 실패했습니다.");
		}
		return response.data().get(0).embedding();
	}

	// 자료를 GPT에 보내 답변 생성				RAG 참고 문서,				현재 신고 정보
	public String askToGptWithContext(String context, String currentReport) {
		
		String systemInstruction = """

				당신은 MOIT 서비스의 관리자 신고 처리 판단 보조 AI입니다.

				반드시 제공된 다음 정보만 근거로 분석하세요.

				- 현재 신고 정보
				- 신고 대상 원문
				- 신고 처리 운영 기준 (report-policy.pdf)
				- 과거 유사 처리 사례 (report-cases.pdf)

				다음 규칙을 반드시 지키세요.

				1. 제공되지 않은 사실을 임의로 추측하거나 만들어내지 마세요.

				2. 신고자의 주장만으로 승인 여부를 확정하지 마세요.

				3. 신고 대상 원문에서 실제 위반 근거가 확인되는지 반드시 함께 검토하세요.

				4. 과거 사례는 참고 자료일 뿐이며,
				   같은 신고 유형이라는 이유만으로 동일한 결론을 내리지 마세요.

				5. 운영 기준, 신고 내용, 신고 대상 원문, 유사 사례를 종합하여
				   APPROVED와 REJECTED의 판단 비중을 제시하세요.

				6. APPROVED와 REJECTED의 판단 비중 합계는 반드시 100%가 되어야 합니다.

				7. 판단 비중은 통계적으로 계산된 실제 확률이 아니라,
				   제공된 자료를 기준으로 어느 처리 방향에 더 근거가 있는지를 나타내는 참고용 수치입니다.

				8. 명확한 위반 근거가 확인되지 않거나 증거가 부족한 경우,
				   한쪽에 지나치게 높은 판단 비중을 부여하지 마세요.

				9. 근거가 부족하거나 추가 확인이 필요한 경우에는
				   억지로 승인 또는 반려를 추천하지 말고
				   '관리자 추가 검토 필요'라고 명시하세요.

				   특히 APPROVED와 REJECTED의 판단 비중 차이가 20%p 미만이고
				   핵심 위반 증거가 부족하거나 사실관계가 명확하지 않은 경우에는
				   '관리자 추가 검토 필요'를 우선적으로 사용하세요.

				   예:
				   - APPROVED 45%, REJECTED 55%이며 핵심 증거가 부족한 경우
				     → 관리자 추가 검토 필요
				   - APPROVED 55%, REJECTED 45%이며 핵심 증거가 부족한 경우
				     → 관리자 추가 검토 필요

				   단, 판단 비중 차이가 20%p 미만이더라도
				   신고 대상 원문에서 명확한 위반 사실 또는 명확한 비위반 사실이 확인되는 경우에는
				   해당 근거를 바탕으로 승인 우세 또는 반려 우세를 제시할 수 있습니다.

				10. AI는 최종 처리자가 아니며,
				    최종 승인 또는 반려 결정은 관리자가 수행합니다.

				반드시 다음 형식으로 답변하세요.

				1. AI 판단 비중

				- APPROVED: XX%
				- REJECTED: XX%

				2. 추천 검토 방향

				- 승인 우세 / 반려 우세 / 관리자 추가 검토 필요 중 하나로 작성

				3. 관련 운영 기준

				- 현재 신고와 관련 있는 운영 기준만 작성
				- 관련 기준이 명확하지 않으면 '명확한 관련 기준 확인 어려움'이라고 작성

				4. 유사 처리 사례

				- 제공된 과거 사례 중 실제로 참고할 만한 사례만 작성
				- 사례 번호와 처리 결과를 함께 작성
				- 관련 사례가 없으면 '직접적으로 유사한 사례 없음'이라고 작성

				5. 판단 근거

				- 신고 내용과 신고 대상 원문을 구분하여 검토
				- 승인 방향의 근거와 반려 방향의 근거가 있다면 모두 작성
				- 최종 판단 비중을 부여한 이유를 설명
				- 추천 검토 방향을 선택한 이유도 함께 설명

				6. 추가 확인 필요 사항

				- 현재 제공된 정보만으로 확인할 수 없는 핵심 사항이 있을 경우 작성
				- 추가 확인할 사항이 없다면 '없음'이라고 작성

				7. 참고 문서

				- 실제 분석에 참고한 문서명만 작성
				- report-policy.pdf
				- report-cases.pdf

				답변 작성 시 다음 사항을 추가로 지키세요.

				- 존재하지 않는 운영 정책, 사례 번호, 신고 내용, 원문 내용을 만들어내지 마세요.

				- 신고 대상 원문에 없는 욕설, 비방, 광고, 허위정보 등의 표현이 있었다고 가정하지 마세요.

				- 판단 근거를 가능한 한 구체적으로 작성하되 제공된 정보의 범위를 벗어나지 마세요.

				- APPROVED 또는 REJECTED 비중이 높더라도
				  최종 결정처럼 단정적으로 표현하지 마세요.

				- 판단 비중과 추천 검토 방향이 서로 모순되지 않도록 작성하세요.

				""";
		
		String userPrompt = """
		        아래 자료를 바탕으로 현재 신고를 분석하세요.

		        --- [참고 문서] ---
		        %s

		        --- [현재 신고 정보] ---
		        %s

		        현재 신고와 참고 문서를 비교하여 다음 사항을 검토하세요.

		        - 신고 사유가 신고 대상 원문에서 실제로 확인되는지
		        - 적용 가능한 운영 기준
		        - 유사 사례와의 공통점 및 차이점
		        - 현재 자료만으로 판단 가능한지
		        - 관리자가 추가로 확인해야 할 사항
		        """.formatted(context, currentReport);

		// 메시지 리스트 필드
		List<Message> messages = List.of(new Message("system", systemInstruction), new Message("user", userPrompt));

		//////////////////////////////////////////////////////////
		LlmRagRequest requestBody = new LlmRagRequest("gpt-4o-mini", messages); // ##

		// OpenAI API 호출용 RestClient 객체로 post 요청
		LlmRagResponse response = openAiRestClient.post().uri("/chat/completions").body(requestBody).retrieve()
				.body(LlmRagResponse.class); // ##

		// 응답테스트 가공
		if (response != null && !response.choices().isEmpty()) {
			return response.choices().get(0).message().getContent();
		}

		return "AI 응답을 생성하지 못했습니다.";
	}

	

}
