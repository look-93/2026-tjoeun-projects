package com.moit.advertisement.dto;

import lombok.Data;

@Data
public class AdvertisementSearchDto {

    // 검색 조건
    private String searchText;   // 제목 검색

    private String status;        // OPEN / PENDING / CLOSED
    private String approvalStatus; // WAITING / APPROVED / REJECTED
    
    // 광고주
    private Integer advertiserId;

    // 페이징
    private int page = 1;   // 기본 1페이지
    private int size = 10;  // 기본 10개

    // 정렬 (선택)
    private String orderType;  
    // latest / priority / popular
    
    // 자동 계산용
    public int getOffset() {
        return (page - 1) * size;
    }
}