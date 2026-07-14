package com.moit.meetup.dto.openapi;

import java.util.List;

import lombok.Data;

@Data
public class AddressSearchResponse {
	private int totalCount;
	List<AddressSearchResponse.AddressSearchDto> list;
	
	@Data
    public static class AddressSearchDto{
        private String address;
        private String road;
        private String jibun;
        private String zipNo;
        private Double latitude;
        private Double longitude;
        private Integer nx;         // 기상청 X
        private Integer ny;         // 기상청 Y
        private String sido;
        private String sigungu;
		
        
    }

}