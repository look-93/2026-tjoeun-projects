package com.moit.qna.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.moit.qna.dao.QuestionMapper;
import com.moit.qna.dto.AnswerDto.AnswerRequestDto;
import com.moit.qna.dto.AnswerDto.AnswerResponseDto;
import com.moit.qna.event.AnswerCreatedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final QuestionMapper questionMapper;
    private final ApplicationEventPublisher publisher;

    // 답변 등록 + 문의 상태 업데이트
    public void register(AnswerRequestDto dto) {
        AnswerResponseDto oldAnswer = questionMapper.findByQuestionId(dto.getQuestionId());
        if (oldAnswer == null) {
            questionMapper.insertAnswer(dto);
        } else {
            dto.setAnswerId(oldAnswer.getAnswerId());
            questionMapper.restoreAnswer(dto);
        }
        questionMapper.updateStatusAnswered(dto.getQuestionId());
        publisher.publishEvent(new AnswerCreatedEvent(dto.getQuestionId()));
    }

    // 답변 수정
    public void update(AnswerRequestDto dto) {
        questionMapper.updateAnswer(dto);
    }

    // 답변 삭제
    public void delete(Long answerId, Long questionId) {
        questionMapper.deleteAnswer(answerId);
        questionMapper.updateStatusPending(questionId);
    }

    // 답변 조회
    public AnswerResponseDto getAnswer(Long questionId) {
        return questionMapper.findByQuestionId(questionId);
    }
}