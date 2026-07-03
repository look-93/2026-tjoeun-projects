package com.moit.meetup.dto;

import lombok.Data;

@Data
public class QuestionDto {

    private int questionId;
    private int parentId;
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
    private String memberName;
    private AnswerDto answer;
    
}