package com.moit.review.dto;

import lombok.Data;

public class ReviewDto {

    @Data
    public static class Request {

        // PK 및 수정/삭제/조회에 필요한 ID
        private Long reviewId;

        private Long meetupId;

        private Long memberId;

        private String content;

        private Integer rating;

        private String isPublic;

        // 이미지 등록 시 필요한 필드
        private Long imageId;

        private String imagePath;
    }

    @Data
    public static class Response {

        private Long reviewId;

        private Long meetupId;

        private Long memberId;

        private String content;

        private Integer rating;

        private Integer likesCount;

        private Integer viewsCount;

        private String isPublic;

        private Integer deleteYn; 
        
        private Long imageId;    

        private String createdAt;

        private String updatedAt;
    }
}