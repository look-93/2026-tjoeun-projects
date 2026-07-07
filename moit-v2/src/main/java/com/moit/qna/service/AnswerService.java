package com.moit.qna.service;

import org.springframework.stereotype.Service;

import com.moit.qna.dao.AnswerMapper;
import com.moit.qna.dao.QuestionMapper;
import com.moit.qna.dto.AnswerDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerMapper answerMapper;
    private final QuestionMapper questionMapper;
    
 // 답변 등록 + 문의 상태 업데이트
    public void register(AnswerDto dto) {

        AnswerDto oldAnswer =
                answerMapper.findByQuestionIdAll(dto.getQuestionId());

        if(oldAnswer == null) {

            answerMapper.insertAnswer(dto);

        } else {

            dto.setAnswerId(oldAnswer.getAnswerId());

            answerMapper.restoreAnswer(dto);
        }

        questionMapper.updateStatusAnswered(dto.getQuestionId());
    }
    // 답변 수정
    public void update(AnswerDto dto) { answerMapper.updateAnswer(dto); }
    // 답변 삭제
    public void delete(int answerId, int questionId) {
        answerMapper.deleteAnswer(answerId);
        questionMapper.updateStatusPending(questionId);
    }
    
    // 답변 조회
    public AnswerDto getAnswer(int questionId) {
        return answerMapper.findByQuestionId(questionId);
    }
}
