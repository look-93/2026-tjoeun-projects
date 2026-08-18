package com.moit.reports.dto;

import com.moit.reports.enums.ReasonCode;
import com.moit.reports.enums.TargetType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AiReportsDto {
	
	private String keywords;		// 키워드
	private ReasonCode reasonCode;	// 사유
	private TargetType targetType;	// 타겟타입
}
