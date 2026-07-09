package com.moit.meetup.dto;

import lombok.Data;

@Data
public class TrustScoreDto {

	private String memberId;
    private Integer finalTrustScore;
    
    // 점수
    private Integer noshowScore;
    private Integer cancelScore;
    private Integer reportScore;
    private Integer reviewScore;

    // 건수
    private Integer noshowCount;
    private Integer cancelCount;
    private Integer reportCount;
    private Integer reviewCount;
}