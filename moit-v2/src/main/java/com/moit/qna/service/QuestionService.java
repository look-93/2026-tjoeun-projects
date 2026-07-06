package com.moit.qna.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.moit.qna.dao.AnswerMapper;
import com.moit.qna.dao.QuestionMapper;
import com.moit.qna.dto.AnswerDto;
import com.moit.qna.dto.QuestionDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionMapper questionMapper;
    private final AnswerMapper answerMapper;

    // 전체 문의 목록 조회 (페이징)
    public List<QuestionDto> getList(
            int start,
            int end) {

        Map<String, Integer> map = new HashMap<>();

        map.put("start", start);
        map.put("end", end);

        return questionMapper.findAll(map);
    }

 // 문의 상세 조회 + 답변 정보 조회
    public QuestionDto getDetail(int id) {
    	// 문의 정보 조회
        QuestionDto question = questionMapper.findById(id);
        // 해당 문의의 답변 조회
        AnswerDto answer = answerMapper.findByQuestionId(id);
        
        question.setAnswer(answer);
        return question;
    }
    
    // 문의 등록
    public void register(QuestionDto dto) {
        questionMapper.insertQuestion(dto);
    }
    
    // 문의 수정
    public void updateQuestion(QuestionDto dto) {
        questionMapper.updateQuestion(dto);
    }

    // 문의 삭제
    public void deleteQuestion(int questionId) {
        questionMapper.deleteQuestion(questionId);
    }
    
    // 전체 문의 수 조회
    public int getAllCnt() {
        return questionMapper.findAllCnt();
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
    
    // 사용자 문의 목록 페이징
    public List<QuestionDto> getMyQuestions(
            int memberId,
            int start,
            int end) {

        Map<String, Object> map = new HashMap<>();

        map.put("memberId", memberId);
        map.put("start", start);
        map.put("end", end);

        return questionMapper.findMyQuestions(map);
    }
    
    // 내 문의 총 개수 조회
    public int getMyQuestionCnt(int memberId) {
        return questionMapper.findMyQuestionCnt(memberId);
    }
    
    //
    public List<QuestionDto> selectByParentId(int parentId){
        return questionMapper.selectByParentId(parentId);
    }
}