package com.moit.meetup.dto.openapi;

import lombok.Data;

@Data
public class RecommendMeetupResponseDto {
    private String title;
    private String category;
    private Integer categoryId;
    private String content;
}
