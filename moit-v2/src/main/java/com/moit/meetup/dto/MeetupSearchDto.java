package com.moit.meetup.dto;

import lombok.Data;

@Data
public class MeetupSearchDto {
	// 모집 상세조회 검색 조건 Dto
	private Integer memberId;
	private String searchType;
    private String searchText;
    private String status;
    private Integer start;
    private Integer end;
   	private Integer sidoId;
   	private String orderType;
   	private Integer categoryId;
   	private Integer pstartno;
   	
   	private String adminSearchText;
}
