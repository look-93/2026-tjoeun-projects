package com.moit.reports.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.moit.reports.dto.ReportsDto;
import com.moit.reports.service.ReportsService;

@Component
public class ApiScheduledTask {
	
	@Autowired
	private ReportsService service;
	@Autowired
	private ApiEmail apiEmail;
	
///////////////////////////////////////////////////////////
//	신고처리하고 3일뒤에 신고처리결과가 맘에 드시나요?   메일보내기 자동으로 
//				cron = 초 분 시 일 월 요일

//	@Scheduled(fixedDelay = 10000) test
	@Scheduled(cron = "0 0 3 * * *")
	public void threeSendEmail() { 

		System.out.println("...스케줄러 실행");
		
		try {
			service.selectThreeDaysAgo();
		} catch (Exception e) { e.printStackTrace(); }
		
		System.out.println("...스케줄러 종료");
	}

//	새벽 배치용
	@Scheduled(cron = "0 0 1 * * *")
	public void yesterdayMember( ) {
		
		try {
			service.selectTargetMembersYesterday();
		} catch (Exception e) { e.printStackTrace(); }
	}
}
