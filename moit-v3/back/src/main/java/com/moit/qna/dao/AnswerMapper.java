package com.moit.qna.dao;

import org.apache.ibatis.annotations.Mapper;

import com.moit.qna.dto.AnswerDto;

@Mapper
public interface AnswerMapper {

	// 질문에 대한 답변 조회
    AnswerDto findByQuestionId(int questionId);

    // 답변 등록
    void insertAnswer(AnswerDto dto);
    
    // 답변 수정
    void updateAnswer(AnswerDto dto);

    // 답변 삭제
    void deleteAnswer(int answerId);
    
    // 삭제된 답변 조회
    AnswerDto findByQuestionIdAll(int questionId);
    
    // 삭제된 답변 복구용
    void restoreAnswer(AnswerDto dto);
}