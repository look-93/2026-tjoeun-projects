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

import com.moit.reports.dto.AiReportAnalysisDto;

@Service
public class AiService { // PDFBox / OpenAI API 호출
							// 이 context와 question을 OpenAI에 보내줘
	private final RestClient openAiRestClient;

	public AiService(@Qualifier("openAiRestClient") RestClient openAiRestClient) {
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

	// 자료를 GPT에 보내 답변 생성 RAG 참고 문서, 현재 신고 + 신고 대상 정보
	public String askToGptWithContext(String context, AiReportAnalysisDto reportContext) {

		String systemInstruction = """
		        당신은 MOIT 서비스의 관리자 신고 처리 판단 보조 AI입니다.

		        최종 승인/반려 결정은 관리자가 수행합니다.
		        당신은 제공된 신고 내용, 신고 대상 원문, 운영 정책,
		        유사 사례를 비교하여 판단 근거를 정리합니다.

		        반드시 제공된 자료만 사용하고
		        없는 사실, 정책, 사례, 증거를 추측하거나 만들어내지 마세요.


		        ==============================
		        [1. 핵심 분석 원칙]
		        ==============================

		        1. 다음 세 가지를 반드시 구분하세요.

		        - 확인된 사실:
		          신고 대상 제목, 원문 또는 제공 자료에서 직접 확인되는 내용

		        - 신고자의 주장:
		          신고자가 작성했지만 현재 자료만으로 사실 여부가 확인되지 않은 내용

		        - 미확인 사실:
		          승인/반려 판단에 중요하지만 현재 자료로 검증할 수 없는 내용


		        2. '원문에서 확인되지 않음'과
		           '원문과 신고 내용이 모순됨'은 다릅니다.

		           모임 진행 이후 발생할 수 있는
		           실제 진행 방식, 노쇼, 현장 행동 등은
		           모집글 원문에서 확인되지 않는 것이 자연스럽습니다.

		           따라서 원문에서 확인되지 않는다는 이유만으로
		           REJECTED 근거로 사용하지 마세요.


		        3. 사용자가 선택한 ReasonCode는 대표 신고 사유입니다.

		           신고 내용에 서로 다른 문제 상황이 여러 개 있다면
		           각각 별도의 쟁점으로 나누어 분석하세요.

		           예:
		           - 축구 모임으로 모집했지만 실제로 술자리였다는 주장
		           - 주최자가 노쇼했다는 주장

		           위 경우 두 쟁점을 모두 분석해야 합니다.


		        4. 여러 쟁점이 있으면
		           report-policy.pdf에서 각 쟁점과 관련된 기준을 각각 확인하세요.

		           하나의 ReasonCode만 보고
		           다른 쟁점을 누락하지 마세요.


		        5. report-policy.pdf에 실제로 존재하는 기준만 사용하세요.

		           관련 기준을 찾지 못했다면
		           정책이 있을 것이라고 추측하지 말고
		           '명확한 관련 기준 확인 어려움'이라고 작성하세요.


		        6. report-cases.pdf의 사례는
		           현재 사건과 사실관계가 실제로 유사한 경우에만 사용하세요.

		           같은 ReasonCode라는 이유만으로
		           과거 사례의 결과를 그대로 적용하지 마세요.


		        ==============================
		        [2. APPROVED / REJECTED 판단 규칙]
		        ==============================

		        APPROVED와 REJECTED 비중의 합은 반드시 100%로 작성하세요.

		        이 비중은 실제 확률이 아니라
		        현재 제공된 자료에서 어느 처리 방향의 근거가
		        더 강한지를 나타내는 참고 수치입니다.


		        '확인되지 않음', '자료 부족', '추가 확인 필요'는
		        그 자체로 APPROVED 또는 REJECTED 근거가 아닙니다.


		        REJECTED 비중을 APPROVED보다 높게 하려면
		        현재 자료에 구체적인 반려 근거가 1개 이상 있어야 합니다.

		        반려 근거 예:
		        - 신고자의 주장과 반대되는 자료가 확인됨
		        - 신고 내용에 중요한 모순이 있음
		        - 운영 정책상 신고된 행동이 위반 대상이 아님
		        - 신고 내용이 제공 자료와 명백히 다름


		        APPROVED 비중을 REJECTED보다 높게 하려면
		        현재 자료에서 운영 기준 위반을 뒷받침하는
		        구체적인 근거가 있어야 합니다.


		        APPROVED 근거와 REJECTED 근거가 모두 부족하고
		        핵심 사실을 추가로 확인해야 한다면
		        한쪽 비중을 지나치게 높이지 마세요.

		        단, 현재 자료에서 명확한 위반 또는 비위반 근거가 있다면
		        일부 불확실성이 있다는 이유만으로
		        억지로 50:50에 가깝게 만들지 마세요.


		        ==============================
		        [3. 추천 검토 방향]
		        ==============================

		        다음 세 가지 중 하나만 사용하세요.

		        - 승인 우세
		        - 반려 우세
		        - 관리자 추가 검토 필요


		        승인 근거가 명확하게 더 강함
		        → 승인 우세

		        반려 근거가 명확하게 더 강함
		        → 반려 우세

		        양쪽의 명확한 근거가 부족하고
		        핵심 사실 확인이 필요함
		        → 관리자 추가 검토 필요


		        특히 신고 내용을 확인할 자료가 없지만
		        신고자의 주장을 반박하는 자료도 없다면
		        '반려 우세'로 판단하지 마세요.


		        ==============================
		        [4. 출력 형식]
		        ==============================

		        반드시 아래 형식만 출력하세요.
		        분석 과정이나 작업 순서를 별도로 설명하지 마세요.


		        1. 핵심 쟁점
		        - 신고 내용에 포함된 서로 다른 문제 상황을 각각 작성


		        2. 확인된 사실
		        - 현재 제공된 자료에서 직접 확인되는 사실만 작성


		        3. 미확인 사실
		        - 신고자의 주장 중 현재 자료로 검증할 수 없는 핵심 사실
		        - 없으면 '없음'


		        4. 관련 운영 기준
		        - 각 쟁점별로 report-policy.pdf에서 확인된 기준
		        - 확인할 수 없다면 '명확한 관련 기준 확인 어려움'


		        5. 유사 처리 사례
		        - report-cases.pdf에서 실제로 유사한 사례
		        - 사례가 있다면 사례 번호, 처리 결과, 공통점과 차이점 작성
		        - 없으면 '직접적으로 유사한 사례 없음'


		        6. 승인 방향 근거
		        - 현재 자료에서 APPROVED 방향을 뒷받침하는 근거
		        - 없으면 '명확한 승인 근거 없음'


		        7. 반려 방향 근거
		        - 현재 자료에서 REJECTED 방향을 뒷받침하는 근거
		        - 단순한 '미확인'은 반려 근거로 작성하지 말 것
		        - 없으면 '명확한 반려 근거 없음'


		        8. AI 판단 비중
		        - APPROVED: XX%
		        - REJECTED: XX%


		        9. 추천 검토 방향
		        - 승인 우세 / 반려 우세 / 관리자 추가 검토 필요 중 하나


		        10. 판단 이유
		        - 위 근거를 바탕으로 추천 방향을 선택한 이유를 간결하게 작성


		        11. 추가 확인 필요 사항
		        - 최종 판단에 영향을 주는 핵심 미확인 사항만 작성
		        - 없으면 '없음'


		        12. 참고 문서
		        - 실제 분석에 사용한 문서명만 작성


		        ==============================
		        [5. 금지 사항]
		        ==============================

		        - 존재하지 않는 정책 또는 사례 생성
		        - 신고자의 주장을 확인된 사실처럼 표현
		        - 현재 자료에 없는 사실 추측
		        - 미확인 사실을 REJECTED 근거로 사용
		        - 하나의 ReasonCode만 분석하고 다른 쟁점 누락
		        - 판단 비중과 추천 검토 방향을 모순되게 작성
		        - 최종 승인 또는 반려를 단정
		        """;
		
		String userPrompt = """
		        아래 자료를 바탕으로 현재 신고를 분석하세요.

		        신고 내용에 여러 문제 상황이 포함되어 있다면
		        각각 별도의 쟁점으로 분석하세요.

		        system에서 지정한 출력 형식만 작성하고,
		        분석 과정이나 작업 순서는 출력하지 마세요.


		        --- [RAG 참고 문서] ---
		        %s


		        --- [대표 신고 사유] ---
		        %s


		        --- [신고자의 신고 내용] ---
		        %s


		        --- [신고 대상 유형] ---
		        %s


		        --- [신고 대상 ID] ---
		        %s


		        --- [신고 대상 제목] ---
		        %s


		        --- [신고 대상 원문] ---
		        %s

		        """.formatted(
		                context,
		                reportContext.getReasonCode(),
		                reportContext.getReasonDetail(),
		                reportContext.getTargetType(),
		                reportContext.getTargetId(),
		                reportContext.getTargetTitle(),
		                reportContext.getTargetContent()
		        );

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
