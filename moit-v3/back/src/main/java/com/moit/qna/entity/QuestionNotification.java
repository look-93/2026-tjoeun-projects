package com.moit.qna.entity;

import com.moit.util.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "NOTIFICATIONS")
@Getter @Setter
public class QuestionNotification extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "notification_seq_generator")
    @SequenceGenerator(name = "notification_seq_generator",sequenceName = "NOTIFICATION_SEQ",allocationSize = 1)
    
    @Column(name = "NOTIFICATION_ID")
    private Integer notificationId;

    @Column(name = "QUESTION_ID", nullable = false)
    private Integer questionId;

    @Column(name = "MEMBER_ID", nullable = false)
    private Integer memberId;

    @Column(name = "TYPE", nullable = false)
    private String type;

    @Column(name = "MESSAGE", nullable = false)
    private String message;

    @Column(name = "IS_READ")
    private String isRead;
}