package com.moit.qna.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "QUESTION_AI_ANALYSIS")
@Getter @Setter
public class QuestionAiAnalysis {

    @Id
    @Column(name = "QUESTION_ID")
    private Long questionId;

    @Column(name = "ANALYSIS_STATUS")
    private String analysisStatus;

    @Column(name = "AGGRESSION_SCORE")
    private Double aggressionScore;
}