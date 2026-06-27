package com.moit.dto;

import lombok.Data;

@Data
public class AdvertisementSearchDto {

	// 검색어 (제목 검색)
	private String searchText;

	// 상태
	private String status;       // OPEN, PENDING, CLOSED

	// 페이징 
    private int page;
    private int size;

    // 계산값
    private int offset;

	// 정렬
	//private String orderType;    // latest, priority
}