package com.moit.qna.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.moit.qna.entity.QuestionAnswer;

public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long> {

    // 질문당 답변은 1개
    Optional<QuestionAnswer> findByQuestion_Id(Long questionId);

}