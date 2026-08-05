package com.moit.qna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.moit.qna.entity.QuestionNotification;

public interface QuestionNotificationRepository extends JpaRepository<QuestionNotification, Long> {

}