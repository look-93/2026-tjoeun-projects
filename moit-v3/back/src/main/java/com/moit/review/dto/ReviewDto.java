package com.moit.review.dto;

import lombok.Data;

public class ReviewDto {


    @Data
    public static class Request {

        private Long meetupId;

        private Long memberId;

        private String content;

        private Integer rating;

        private String isPublic;

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

        private String createdAt;

        private String updatedAt;

    }

}