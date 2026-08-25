package com.moit.reports.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.moit.reports.service.ReportsService;

@Component
public class ApiScheduledTask {
	
	@Autowired private ReportsService service;
	
//	신고처리하고 3일뒤에 신고처리결과가 맘에 드시나요?   메일보내기 자동으로 
//				cron = 초 분 시 일 월 요일

//	test
//	@Scheduled(fixedDelay = 10000)
	@Scheduled(cron = "0 0 3 * * *")
	public void threeSendEmail() { 

		System.out.println("...신고처리 3일 후 sendEmail 스케줄러 실행");
		
		try {
			service.sendThreeDaysAgoReportEmails();
			
		} catch (Exception e) { e.printStackTrace(); }
		
		System.out.println("...신고처리 3일 후 sendEmail 스케줄러 종료");
	}
}