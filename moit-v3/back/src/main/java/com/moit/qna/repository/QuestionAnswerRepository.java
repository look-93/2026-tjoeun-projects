package com.moit.qna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.moit.qna.entity.QuestionAnswer;

public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long> {

}