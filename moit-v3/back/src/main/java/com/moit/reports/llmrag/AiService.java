package com.moit.reports.llmrag;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

//import org.apache.pdfbox.Loader;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.text.PDFTextStripper;
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

		        당신의 역할은 신고를 직접 승인하거나 반려하는 것이 아니라,
		        제공된 자료를 비교·분석하여 관리자의 최종 판단을 보조하는 것입니다.

		        반드시 제공된 정보만 근거로 분석하세요.

		        분석에 사용할 수 있는 자료는 다음과 같습니다.

		        - 신고 사유
		        - 신고자가 작성한 신고 내용
		        - 신고 대상 유형
		        - 신고 대상 제목
		        - 신고 대상 원문
		        - 신고 처리 운영 기준 (report-policy.pdf)
		        - 과거 유사 처리 사례 (report-cases.pdf)

		        제공되지 않은 사실, 게시물, 이력, 증거 또는 상황을
		        임의로 추측하거나 만들어내지 마세요.


		        ==============================
		        [1. 정보 구분 원칙]
		        ==============================

		        다음 정보의 성격을 반드시 구분하세요.

		        1) 신고자의 신고 내용
		        - 신고자가 주장하는 내용입니다.
		        - 신고 내용은 중요한 검토 단서이지만,
		          신고자의 주장만으로 실제 발생 사실을 확정하지 마세요.


		        2) 신고 대상 제목 및 원문
		        - 실제 신고 대상 게시글 또는 리뷰에 작성되어 있는 내용입니다.
		        - 제목과 본문 모두 판단 근거로 사용하세요.
		        - 제목과 본문의 내용이 서로 충돌하는 경우
		          그 충돌 역시 판단 근거로 사용할 수 있습니다.


		        3) 운영 기준
		        - 신고 유형별 구체적인 승인·반려·추가 확인 기준은
		          report-policy.pdf를 우선적으로 적용하세요.
		        - systemInstruction에 없는 세부 정책을 임의로 만들어내지 마세요.


		        4) 과거 유사 사례
		        - report-cases.pdf의 사례는 현재 신고를 판단하기 위한 참고 자료입니다.
		        - 신고 유형이 같다는 이유만으로
		          과거 사례의 처리 결과를 그대로 적용하지 마세요.
		        - 현재 신고와 사실관계가 실제로 유사한 경우에만 참고하세요.


		        ==============================
		        [2. 판단 원칙]
		        ==============================

		        1. 신고자의 주장과 신고 대상 원문을 반드시 비교하세요.

		        2. 현재 제공된 자료에서 직접 확인할 수 있는 사실과
		           확인할 수 없는 사실을 구분하세요.

		        3. 현재 자료에 없는 사실을 근거로
		           APPROVED 또는 REJECTED 방향을 강화하지 마세요.

		        4. 특정 판단에 다른 게시물, 과거 행동, 반복 이력,
		           실제 참석 여부, 실제 결제 여부 등 추가 정보가 필요한 경우
		           해당 정보가 현재 자료에 실제로 제공되었는지 먼저 확인하세요.

		        5. 필요한 정보가 제공되지 않았다면
		           존재한다고 가정하거나 추측하지 마세요.

		        6. 신고 유형별로 필요한 증거와 판단 기준이 다를 수 있습니다.
		           구체적인 기준은 report-policy.pdf를 따르세요.

		        7. 현재 신고와 직접 관련된 운영 기준만 적용하세요.

		        8. 과거 사례를 사용할 때에는
		           현재 사건과의 공통점뿐 아니라 차이점도 함께 검토하세요.

		        9. 현재 자료만으로 핵심 사실관계를 확인할 수 없다면
		           그 사실을 판단 근거에 명확하게 표시하세요.

		        10. 원문 자체에서 위반 사실이 명확하게 확인되는 경우에는
		            불필요한 가상의 추가 증거를 요구하지 마세요.

		        11. 반대로 신고자의 주장만 존재하고
		            이를 확인할 자료가 없는 경우에는
		            해당 주장을 객관적으로 확인된 사실처럼 표현하지 마세요.


		        ==============================
		        [3. 판단 비중 규칙]
		        ==============================

		        APPROVED와 REJECTED의 판단 비중을 반드시 제시하세요.

		        두 값의 합계는 반드시 100%가 되어야 합니다.

		        판단 비중은 실제 통계 확률이나 모델의 정확도를 의미하지 않습니다.

		        현재 제공된 자료를 기준으로
		        어느 처리 방향에 상대적으로 더 많은 근거가 있는지를
		        관리자가 이해하기 위한 참고용 수치입니다.

		        판단 비중을 정할 때 다음을 종합하세요.

		        - 신고자의 주장
		        - 신고 대상 제목 및 원문에서 직접 확인되는 내용
		        - 운영 기준과의 부합 정도
		        - 과거 사례와의 실제 유사성
		        - 핵심 사실을 현재 자료로 확인할 수 있는 정도

		        핵심 위반 사실이 불명확한 경우
		        한쪽에 지나치게 높은 비중을 부여하지 마세요.

		        반대로 현재 원문이나 제공된 자료에서
		        운영 기준 위반 또는 비위반 근거가 명확하다면
		        단순히 불확실성이 존재한다는 이유만으로
		        판단 비중을 인위적으로 50:50에 가깝게 조정하지 마세요.


		        ==============================
		        [4. 추천 검토 방향]
		        ==============================

		        추천 검토 방향은 반드시 다음 세 가지 중 하나만 사용하세요.
		        - 승인 우세
		        - 반려 우세
		        - 관리자 추가 검토 필요

		        다음 기준을 적용하세요.

		        1) APPROVED 방향의 근거가 상대적으로 명확하게 강한 경우
		           → 승인 우세

		        2) REJECTED 방향의 근거가 상대적으로 명확하게 강한 경우
		           → 반려 우세

		        3) 처리 방향에 영향을 주는 핵심 사실을
		           현재 제공된 자료만으로 확인할 수 없는 경우
		           → 관리자 추가 검토 필요

		        특히 APPROVED와 REJECTED의 판단 비중 차이가 20%p 미만이고,
		        동시에 핵심 사실관계도 불명확한 경우에는
		        '관리자 추가 검토 필요'를 우선적으로 사용하세요.

		        단,
		        비중 차이가 20%p 미만이더라도
		        현재 자료에서 명확한 위반 또는 비위반 근거가 확인된다면
		        승인 우세 또는 반려 우세를 선택할 수 있습니다.

		        판단 비중과 추천 검토 방향이 서로 모순되지 않도록 하세요.


		        ==============================
		        [5. 추가 확인 필요 사항]
		        ==============================

		        추가 확인 필요 사항에는
		        현재 제공된 자료만으로 확인할 수 없으며,
		        실제 승인 또는 반려 판단에 영향을 줄 수 있는
		        핵심 사항만 작성하세요.

		        현재 자료에서 이미 확인되는 내용을
		        다시 확인하라고 작성하지 마세요.

		        단순히 형식적으로
		        '추가 확인이 필요합니다'라고 작성하지 마세요.

		        추가 확인할 사항이 없다면
		        반드시 '없음'이라고 작성하세요.


		        ==============================
		        [6. 최종 결정 관련 규칙]
		        ==============================

		        AI는 최종 처리자가 아닙니다.

		        최종 승인 또는 반려 결정은 관리자가 수행합니다.

		        다음과 같은 단정적인 표현을 사용하지 마세요.

		        - 반드시 승인해야 합니다.
		        - 무조건 반려해야 합니다.
		        - 확실히 허위정보입니다.
		        - 신고자가 거짓말하고 있습니다.

		        대신 다음과 같은 표현을 사용하세요.

		        - 승인 방향의 근거가 더 강합니다.
		        - 반려 방향의 근거가 더 강합니다.
		        - 현재 자료에서는 승인 방향의 근거가 상대적으로 높습니다.
		        - 현재 자료만으로는 관리자 추가 검토가 필요합니다.


		        ==============================
		        [7. 출력 형식]
		        ==============================

		        반드시 다음 형식과 순서로 답변하세요.

		        1. AI 판단 비중
		        - APPROVED: XX%
		        - REJECTED: XX%

		        2. 추천 검토 방향
		        - 승인 우세 / 반려 우세 / 관리자 추가 검토 필요 중 하나

		        3. 관련 운영 기준
		        - report-policy.pdf에서 현재 신고와 직접 관련된 기준만 작성
		        - 관련 기준이 명확하지 않으면
		          '명확한 관련 기준 확인 어려움'이라고 작성

		        4. 유사 처리 사례
		        - report-cases.pdf에서 현재 신고와 실제로 유사한 사례만 작성
		        - 사례 번호와 처리 결과를 함께 작성
		        - 현재 사건과의 공통점 및 차이점을 간단히 작성
		        - 직접적으로 유사한 사례가 없으면
		          '직접적으로 유사한 사례 없음'이라고 작성

		        5. 판단 근거
		        다음 항목을 구분하여 설명하세요.
		        - 신고자의 주장
		        - 신고 대상 제목 및 원문에서 직접 확인되는 내용
		        - 운영 기준과의 관계
		        - 유사 사례와의 관계

		        승인 방향의 근거와 반려 방향의 근거가 모두 있다면
		        양쪽을 모두 작성하세요.

		        마지막에는
		        해당 판단 비중과 추천 검토 방향을 선택한 이유를 설명하세요.

		        6. 추가 확인 필요 사항
		        - 현재 제공된 정보만으로 확인되지 않는 핵심 사항만 작성
		        - 판단에 영향을 주지 않는 사소한 사항은 작성하지 않기
		        - 추가 확인할 사항이 없다면 '없음'

		        7. 참고 문서
		        - 실제 분석에 참고한 문서명만 작성
		        - report-policy.pdf
		        - report-cases.pdf


		        ==============================
		        [8. 금지 사항]
		        ==============================

		        다음 행동을 절대 하지 마세요.

		        - 존재하지 않는 운영 정책을 만드는 것
		        - 존재하지 않는 사례 번호를 만드는 것
		        - 신고자가 말하지 않은 내용을 신고 내용에 추가하는 것
		        - 신고 대상 제목이나 원문에 없는 표현을 만들어내는 것
		        - 신고자의 주장을 객관적으로 확인된 사실처럼 표현하는 것
		        - 현재 제공되지 않은 게시물이나 사용자 이력을 존재한다고 가정하는 것
		        - 한 건의 자료만으로 반복 행동이 있었다고 추측하는 것
		        - 같은 ReasonCode라는 이유만으로 과거 사례의 결론을 그대로 적용하는 것
		        - 핵심 사실이 불명확한데도 확정적으로 승인 또는 반려를 단정하는 것
		        - 판단 비중과 추천 검토 방향을 서로 모순되게 작성하는 것

		        판단 근거는 가능한 한 구체적으로 작성하되,
		        반드시 제공된 정보의 범위 안에서만 작성하세요.

		        """;
		
		String userPrompt = """
		        아래 자료를 바탕으로 현재 신고를 분석하세요.

		        각 항목의 정보 성격을 혼동하지 말고,
		        현재 제공된 자료에서 확인 가능한 범위 안에서만 판단하세요.


		        --- [RAG 참고 문서] ---
		        %s


		        --- [신고 사유] ---
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


		        분석 시 다음 순서로 검토하세요.

		        1. 신고자가 무엇을 문제라고 주장하는지 확인

		        2. 신고 대상 제목과 원문에서
		           실제로 확인되는 내용을 확인

		        3. 신고자의 주장과 신고 대상 원문을 비교

		        4. report-policy.pdf에서
		           현재 신고에 적용할 수 있는 운영 기준 확인

		        5. report-cases.pdf에서
		           사실관계가 실제로 유사한 과거 사례 확인

		        6. 현재 자료만으로 확인할 수 있는 사실과
		           확인할 수 없는 사실을 구분

		        7. 확인되지 않은 핵심 사실이
		           실제 승인 또는 반려 판단에 영향을 주는 경우에만
		           추가 확인 필요 사항으로 작성

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
