package com.moit.qna.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moit.qna.ai.OpenAiService;
import com.moit.qna.ai.ProfanityFilter;
import com.moit.qna.ai.dto.AiAnalysisResult;
import com.moit.qna.dao.QuestionAiAnalysisMapper;
import com.moit.qna.dto.QuestionAiAnalysisDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionAiAnalysisService {

    private final OpenAiService openAiService;
    private final QuestionAiAnalysisMapper questionAiAnalysisMapper;
    private final ProfanityFilter profanityFilter;

    @Transactional
    public void analyzeAndSave(int questionId, String text) {
        QuestionAiAnalysisDto dto = new QuestionAiAnalysisDto();
        dto.setQuestionId(questionId);

        // 1차 방어
        if (profanityFilter.containsBadWord(text)) {
            dto.setAnalysisStatus("PENDING_REVIEW");
            dto.setAggressionScore(99);
            questionAiAnalysisMapper.insert(dto);
            return;
        }

        // 2차 방어
        try {
            AiAnalysisResult result = openAiService.analyze(text);
            dto.setAnalysisStatus(result.getAnalysis());
            dto.setAggressionScore(result.getScore());
        } catch (Exception e) {
            dto.setAnalysisStatus("PENDING_REVIEW");
            dto.setAggressionScore(0);
        }
        questionAiAnalysisMapper.insert(dto);
    }
    //검토 상태변경
    public void changeToNormal(List<Integer> ids){
        for(Integer id : ids){
            questionAiAnalysisMapper.changeToNormal(id);
        }
    }
}