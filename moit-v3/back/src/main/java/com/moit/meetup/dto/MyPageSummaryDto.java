package com.moit.meetup.dto;

import lombok.Data;

@Data
public class MyPageSummaryDto {
	// 마이페이지 집계 데이터
	private Integer meetupCount; //내모집수
	private Integer reviewCount; //후기수
	private Integer likeCount;   //좋아요수
	private Integer applicationCount; //신청수 
}
