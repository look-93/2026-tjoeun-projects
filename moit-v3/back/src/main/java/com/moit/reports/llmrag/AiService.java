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
//	public String extractTextFromPdf(InputStream inputStream) throws IOException {
//		try (PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
//			PDFTextStripper stripper = new PDFTextStripper();
//			return stripper.getText(document);
//		}
//	}
//	
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

	// 자료를 GPT에 보내 답변 생성
	public String askToGptWithContext(String context, String question) {
		String systemInstruction = "당신은 업로드된 문서내용을 기반으로 답변하는 전문 비서입니다."; // ##
		String userPrompt = "--- [문서 내용] ---\n%s\n --- [질문] ---\n%s".formatted(context, question); // ##

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
