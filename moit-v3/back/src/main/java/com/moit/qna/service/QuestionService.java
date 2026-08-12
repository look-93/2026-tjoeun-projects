package com.moit.qna.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.moit.qna.dao.QuestionMapper;
import com.moit.qna.dto.AnswerDto.AnswerResponseDto;
import com.moit.qna.dto.QuestionDto.QuestionRequestDto;
import com.moit.qna.dto.QuestionDto.QuestionResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionMapper questionMapper;
    private final QuestionAiAnalysisService questionAiAnalysisService;

    // 전체 문의 목록 조회 (페이징)
    public List<QuestionResponseDto> getList(
            int start,
            int end,
            String type,
            String keyword,
            String status,
            String startDate,
            String endDate) {

        Map<String, Object> map = new HashMap<>();

        map.put("start", start);
        map.put("end", end);

        map.put("type", type);
        map.put("keyword", keyword);
        map.put("status", status);
        map.put("startDate", startDate);
        map.put("endDate", endDate);

        List<QuestionResponseDto> list = questionMapper.findAll(map);

        // 답변 정보 추가
        for (QuestionResponseDto q : list) {
        	AnswerResponseDto answer = questionMapper.findByQuestionId(q.getQuestionId());
            q.setAnswer(answer);
        }
        return list;
    }

    // 문의 상세 조회 + 답변 정보 조회
    public QuestionResponseDto getDetail(Long id) {
        // 문의 정보 조회
        QuestionResponseDto question = questionMapper.findById(id);
        // 해당 문의의 답변 조회
        AnswerResponseDto answer = questionMapper.findByQuestionId(id);

        question.setAnswer(answer);
        return question;
    }

    // 문의 등록
    public void register(QuestionRequestDto dto) {
        // questions 테이블 저장
        questionMapper.insertQuestion(dto);
        // AI 분석
        String text = dto.getTitle() + "\n" + dto.getContent();

        questionAiAnalysisService.analyzeAndSave( dto.getQuestionId(), text );
    }

    // 문의 수정
    public void updateQuestion(QuestionRequestDto dto) {
        questionMapper.updateQuestion(dto);
    }

    // 문의 삭제
    public void deleteQuestion(Long questionId) {
        questionMapper.deleteQuestion(questionId);
    }

    // 전체 문의 수 조회
    public int getAllCnt() {
        return questionMapper.findAllCnt();
    }

    // 검색 문의 수 조회
    public int getSearchCnt(
            String type,
            String keyword,
            String status,
            String startDate,
            String endDate) {

        Map<String,Object> map=new HashMap<>();

        map.put("type", type);
        map.put("keyword", keyword);
        map.put("status", status);
        map.put("startDate", startDate);
        map.put("endDate", endDate);

        return questionMapper.findSearchCnt(map);
    }

    // 답변 대기 문의 수 조회
    public int getPendingCnt() {
        return questionMapper.findPendingCnt();
    }

    // 답변 완료 문의 수 조회
    public int getAnsweredCnt() {
        return questionMapper.findAnsweredCnt();
    }

    // 오늘 등록된 문의 수 조회
    public int getTodayCnt() {
        return questionMapper.findTodayCnt();
    }

    // 사용자 문의 목록 페이징 조회
    public List<QuestionResponseDto> getMyQuestions(
            Long memberId,
            int start,
            int end,
            String type,
            String keyword) {

        Map<String, Object> map = new HashMap<>();

        map.put("memberId", memberId);
        map.put("start", start);
        map.put("end", end);
        map.put("type", type);
        map.put("keyword", keyword);

        List<QuestionResponseDto> list = questionMapper.findMyQuestions(map);

        // 답변 정보 추가
        for (QuestionResponseDto q : list) {
        	AnswerResponseDto answer = questionMapper.findByQuestionId(q.getQuestionId());
            q.setAnswer(answer);
        }

        return list;
    }

    // 내 문의 총 개수 조회
    public int getMyQuestionCnt(Long memberId, String type, String keyword) {
        Map<String, Object> map = new HashMap<>();

        map.put("memberId", memberId);
        map.put("type", type);
        map.put("keyword", keyword);

        return questionMapper.findMyQuestionCnt(map);
    }

    //관리자용 선택 삭제
    public void deleteSelected(List<Long> ids){
        questionMapper.deleteSelected(ids);
    }

    //해당 모임의 문의 목록
    public List<QuestionResponseDto> selectByParentId(Long parentId){
        return questionMapper.selectByParentId(parentId);
    }
}