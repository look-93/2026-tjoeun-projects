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
@Table(name = "QUESTIONS")
@Getter @Setter
public class Question extends BaseEntity{

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "question_seq_generator")
    @SequenceGenerator(name = "question_seq_generator", sequenceName = "QUESTION_SEQ", allocationSize = 1)
    @Column(name = "QUESTION_ID")
    private Integer questionId;

    @Column(name = "PARENT_ID", nullable = false)
    private Integer parentId;

    @Column(name = "MEMBER_ID", nullable = false)
    private Integer memberId;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Lob
    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "IS_PUBLIC")
    private String isPublic;

}