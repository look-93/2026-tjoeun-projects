package com.moit.qna.entity;

import com.moit.member.entity.Member;
import com.moit.util.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private Long notificationId;
    
    @ManyToOne
    @JoinColumn(name = "QUESTION_ID", nullable = false)
    private Question question;
    
    //유저는 많은 질문을 가질 수 있다
	//    <Member>
	//    @OneToMany( mappedBy = "member" ,cascade = CascadeType.ALL, orphanRemoval = true )
	//    private List<QuestionNotification> questionNotifications = new ArrayList<>(); 
    
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    // enum
    @Column(name = "TYPE", nullable = false)
    private String type;

    @Column(name = "MESSAGE", nullable = false)
    private String message;

    //
    @Column(name = "IS_READ")
    private String isRead;
}