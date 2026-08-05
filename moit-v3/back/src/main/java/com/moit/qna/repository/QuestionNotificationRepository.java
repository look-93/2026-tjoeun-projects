package com.moit.qna.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moit.member.entity.Member;
import com.moit.qna.entity.QuestionNotification;

public interface QuestionNotificationRepository 
        extends JpaRepository<QuestionNotification, Integer> {

    // 읽지 않은 알림 개수
    long countByMemberIdAndIsRead(Integer memberId, String isRead);

    // 읽지 않은 알림 조회
    List<QuestionNotification> findByMemberIdAndIsRead( Integer memberId, String isRead );

    // 전체 알림 조회
    List<QuestionNotification> findByMemberId( Integer memberId );

    // 알림 읽음 처리
    @Modifying
    @Query("""
            UPDATE QuestionNotification qn
            SET qn.isRead = 'Y'
            WHERE qn.notificationId = :notificationId
            """)
    void readNotification( @Param("notificationId") Integer notificationId );

}