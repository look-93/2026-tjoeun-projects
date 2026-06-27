package com.moit.dto;

import lombok.Data;

@Data
public class MeetupSearchDto {
	// 모집 상세조회 검색 조건 Dto
	private int memberId;
	private String searchType;
    private String searchText;
    private String status;
    private int start;
    private int end;
   	private int sidoId;
   	private String orderType;
   	private int categoryId;

   	
}
