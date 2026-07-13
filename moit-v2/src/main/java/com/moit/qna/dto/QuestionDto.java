package com.moit.qna.dto;

import lombok.Data;

@Data
public class QuestionDto {

    private int questionId;
    private Integer parentId;
    private int memberId;

    private String category;   // MEETUP, ADMIN
    private String title;
    private String content;

    private String status;     // PENDING, ANSWERED
    private String isPublic;
    private String deleteYn;

    private java.sql.Timestamp createdAt;
    private java.sql.Timestamp updatedAt;

    // JOIN
    private String nickname;
    private AnswerDto answer;
    
    // AI 분석 결과
    private String analysisStatus;
    private int aggressionScore;
}