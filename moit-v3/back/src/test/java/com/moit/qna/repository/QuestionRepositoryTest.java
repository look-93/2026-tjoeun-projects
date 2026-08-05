package com.moit.qna.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.moit.qna.entity.Question;

@DataJpaTest
@AutoConfigureTestDatabase( replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionRepositoryTest {

    @Autowired private QuestionRepository questionRepository;
    @Autowired private QuestionAnswerRepository questionAnswerRepository;
    @Autowired private QuestionNotificationRepository questionNotificationRepository;
    @Autowired private QuestionAiAnalysisRepository questionAiAnalysisRepository;

    @Test
    @DisplayName("QNA Repository 전체 Bean 생성 테스트")
    void repositoryBeanTest() {

        assertThat(questionRepository).isNotNull();
        assertThat(questionAnswerRepository).isNotNull();
        assertThat(questionNotificationRepository).isNotNull();
        assertThat(questionAiAnalysisRepository).isNotNull();
    }
    
    @Test
    @DisplayName("Question Repository 조회 테스트")
    void findTest(){
        List<Question> list = questionRepository.findAll();
        assertThat(list).isNotNull();
    }
}