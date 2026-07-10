package com.moit.qna.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moit.qna.ai.OpenAiService;
import com.moit.qna.ai.dto.AiAnalysisResult;
import com.moit.qna.dto.QuestionAiAnalysisDto;
import com.moit.qna.dao.QuestionAiAnalysisMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionAiAnalysisService {

    private final OpenAiService openAiService;
    private final QuestionAiAnalysisMapper questionAiAnalysisMapper;

    @Transactional
    public void analyzeAndSave(int questionId, String text) {
        QuestionAiAnalysisDto dto = new QuestionAiAnalysisDto();
        dto.setQuestionId(questionId);

        try {
            AiAnalysisResult result = openAiService.analyze(text);
            dto.setAnalysisStatus(result.getAnalysis());
            dto.setAggressionScore(result.getScore());
        } catch (Exception e) {
            // OpenAI 호출 실패 시 관리자 검토 대상으로 저장
            dto.setAnalysisStatus("PENDING_REVIEW");
            dto.setAggressionScore(0);
        }
        questionAiAnalysisMapper.insert(dto);
    }

}