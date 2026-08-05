package com.moit.qna.entity;

import com.moit.util.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ANSWERS")
@Getter @Setter
public class QuestionAnswer extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "answer_seq_generator")
    @SequenceGenerator(name = "answer_seq_generator",sequenceName = "ANSWER_SEQ",allocationSize = 1)
    
    @Column(name = "ANSWER_ID")
    private Integer answerId;

    @Column(name = "QUESTION_ID", nullable = false, unique = true)
    private Integer questionId;

    @Column(name = "MEMBER_ID", nullable = false)
    private Integer memberId;

    @Lob
    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "IS_PUBLIC")
    private String isPublic;
}