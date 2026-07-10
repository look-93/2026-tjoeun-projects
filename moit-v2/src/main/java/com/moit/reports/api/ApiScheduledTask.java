package com.moit.reports.api;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ApiScheduledTask {
	
//	신고처리하고 3일뒤에 신고처리결과가 맘에 드시나요?   메일보내기 자동으로 
//	@Scheduled(fixedRate = 259200)
	public void runTest() {
		System.out.println("...스케줄러 실행중 : " + System.currentTimeMillis());
	}

}
