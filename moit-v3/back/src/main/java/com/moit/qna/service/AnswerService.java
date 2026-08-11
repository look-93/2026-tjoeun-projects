package com.moit.qna.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.moit.qna.dao.AnswerMapper;
import com.moit.qna.dao.QuestionMapper;
import com.moit.qna.dto.AnswerDto.AnswerRequestDto;
import com.moit.qna.dto.AnswerDto.AnswerResponseDto;
import com.moit.qna.event.AnswerCreatedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerMapper answerMapper;
    private final QuestionMapper questionMapper;
    private final ApplicationEventPublisher publisher;

    // 답변 등록 + 문의 상태 업데이트
    public void register(AnswerRequestDto dto) {
        AnswerResponseDto oldAnswer = answerMapper.findByQuestionId(dto.getQuestionId());

        if(oldAnswer == null) {
            answerMapper.insertAnswer(dto);
        } else {
            dto.setAnswerId(oldAnswer.getAnswerId());
            answerMapper.restoreAnswer(dto);
        }
        questionMapper.updateStatusAnswered(dto.getQuestionId());
        publisher.publishEvent( new AnswerCreatedEvent(dto.getQuestionId()) );
    }

    // 답변 수정
    public void update(AnswerRequestDto dto) {
        answerMapper.updateAnswer(dto);
    }

    // 답변 삭제
    public void delete(Long answerId, Long questionId) {
        answerMapper.deleteAnswer(answerId);
        questionMapper.updateStatusPending(questionId);
    }

    // 답변 조회
    public AnswerResponseDto getAnswer(Long questionId) {
        return answerMapper.findByQuestionId(questionId);
    }
}