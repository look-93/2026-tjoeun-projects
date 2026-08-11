package com.moit.qna.dao;

import org.apache.ibatis.annotations.Mapper;

import com.moit.qna.dto.AnswerDto.AnswerRequestDto;
import com.moit.qna.dto.AnswerDto.AnswerResponseDto;

@Mapper
public interface AnswerMapper {

    // 질문에 대한 답변 조회
    AnswerResponseDto findByQuestionId(Long questionId);

    // 답변 등록
    void insertAnswer(AnswerRequestDto dto);

    // 답변 수정
    void updateAnswer(AnswerRequestDto dto);

    // 답변 삭제
    void deleteAnswer(Long answerId);

    // 삭제된 답변 조회
    AnswerResponseDto findByQuestionIdAll(Long questionId);

    // 삭제된 답변 복구용
    void restoreAnswer(AnswerRequestDto dto);
}