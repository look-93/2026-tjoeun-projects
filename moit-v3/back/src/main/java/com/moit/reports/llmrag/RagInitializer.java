package com.moit.reports.llmrag;

import java.io.InputStream;
import java.util.List;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RagInitializer { // 서버 시작할 때 PDF 파일 읽어보는 초기 실행 코드

    @Bean
    public ApplicationRunner initializePdfData(
    		ResourceLoader resourceLoader, AiService aiService, RagService ragService) {
    	
        return args -> { 
        	
        	List<String> pdfPaths = List.of(
					"classpath:docs/report-policy.pdf",
					"classpath:docs/report-cases.pdf"
        	);
        	
        	for (String path : pdfPaths) {
        		var pdfResource = resourceLoader.getResource(path);
        		
        		if (pdfResource.exists()) { 
        			try (InputStream is = pdfResource.getInputStream()) {
        				// PDF 글자 읽기
        				String text = aiService.extractTextFromPdf(is); 
        				// PDF를 의미있는 문서 조각으로 나누고 저장
        				ragService.splitAndAddDocument(pdfResource.getFilename(), text);

        			} catch (Exception e) {
        				log.error( "[RAG] 기본 PDF 파싱 중 오류 발생 - 파일: {}", pdfResource.getFilename(), e );
        			}
        			
        		} else {
        			log.warn( "[RAG] PDF 파일을 찾을 수 없습니다. 경로를 확인해주세요. path={}", path );
        		}
        	}
        };
    }
}