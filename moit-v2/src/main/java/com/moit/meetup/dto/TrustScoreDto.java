package com.moit.meetup.dto;

import lombok.Data;

@Data
public class TrustScoreDto {

	private Integer memberId;
	private String aiSummary;
	private Integer finalTrustScore;
	
    // 점수
    private Integer noshowScore;
    private Integer cancelScore;
    private Integer reportScore;
    private Integer reviewRatingScore;

    // 건수
    private Integer noshowCount;
    private Integer cancelCount;
    private Integer reportCount;
    private Integer reviewRatingCount;
}