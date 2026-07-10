package com.moit.qna.listener;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.moit.qna.dao.NotificationMapper;
import com.moit.qna.dao.QuestionMapper;
import com.moit.qna.dto.NotificationDto;
import com.moit.qna.dto.QuestionDto;
import com.moit.qna.event.AnswerCreatedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationMapper notificationMapper;
    private final QuestionMapper questionMapper;

    @Async
    @EventListener
    public void handle(AnswerCreatedEvent event) {
        QuestionDto question = questionMapper.findById(event.getQuestionId());
        NotificationDto dto = new NotificationDto();

        dto.setQuestionId(question.getQuestionId());
        dto.setMemberId(question.getMemberId());

        dto.setType("ANSWER_CREATED");
        dto.setMessage("문의에 답변이 등록되었습니다.");
        notificationMapper.insert(dto);
    }
}