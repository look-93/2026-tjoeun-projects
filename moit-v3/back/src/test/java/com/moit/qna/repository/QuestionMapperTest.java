package com.moit.qna.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.moit.qna.dao.AnswerMapper;
import com.moit.qna.dao.NotificationMapper;
import com.moit.qna.dao.QuestionAiAnalysisMapper;
import com.moit.qna.dao.QuestionMapper;
import com.moit.qna.dto.AnswerDto;
import com.moit.qna.dto.NotificationDto;
import com.moit.qna.dto.QuestionDto;

@SpringBootTest
public class QuestionMapperTest {

    @Autowired private QuestionMapper questionMapper;
    @Autowired private AnswerMapper answerMapper;
    @Autowired private NotificationMapper notificationMapper;
    @Autowired private QuestionAiAnalysisMapper questionAiAnalysisMapper;

    @Test
    @DisplayName("QNA Mapper 전체 Bean 생성 테스트")
    void mapperBeanTest() {
        assertThat(questionMapper).isNotNull();
        assertThat(answerMapper).isNotNull();
        assertThat(notificationMapper).isNotNull();
        assertThat(questionAiAnalysisMapper).isNotNull();
    }

//    @Test
//    @DisplayName("Question Mapper 조회 테스트")
//    void questionMapperTest() {
//        List<QuestionDto> list = questionMapper.findAll(new HashMap<>());
//        assertThat(list).isNotNull();
//    }
//
//    @Test
//    @DisplayName("Answer Mapper 조회 테스트")
//    void answerMapperTest() {
//        AnswerDto dto = answerMapper.findByQuestionId(1);
//        assertThat(dto).isNotNull();
//    }
//
//    @Test
//    @DisplayName("Notification Mapper 조회 테스트")
//    void notificationMapperTest() {
//        List<NotificationDto> list = notificationMapper.selectAll(1);
//        assertThat(list).isNotNull();
//    }
}