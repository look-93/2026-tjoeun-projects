package com.moit.qna.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class QuestionDto {

    // 질문 등록/수정 요청 DTO
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class QuestionRequestDto {
        private Long questionId;
        private Long parentId;
        private Long memberId;

        private String category;   // MEETUP, ADMIN
        private String title;
        private String content;
        private String isPublic;
    }

    // 질문 조회 응답 DTO
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class QuestionResponseDto {
        private Long questionId;
        private Long parentId;
        private Long memberId;

        private String category;
        private String title;
        private String content;

        private String status;	 // PENDING, ANSWERED
        private String isPublic;
        private String deleteYn;

        private Timestamp createdAt;
        private Timestamp updatedAt;

        // JOIN
        private String nickname;
        private AnswerDto.AnswerResponseDto answer;

        // AI 분석 결과
        private String analysisStatus;
        private int aggressionScore;
    }
}