package com.moit.reports.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.moit.reports.dto.EmailRequestDto;
import com.moit.reports.service.FailedEmailQueueService;
import com.moit.reports.service.ReportsService;
import com.moit.reports.service.SendEmailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApiScheduledTask {
	
	@Autowired private ReportsService service;
	
	@Autowired private SendEmailService sendEmailService;
	@Autowired private FailedEmailQueueService failedEmailQueueService;

	// 신고 처리 후 3일 뒤 만족도 메일 발송
	@Scheduled(cron = "0 0 3 * * *")
	public void threeSendEmail() { 
		try {
			service.sendThreeDaysAgoReportEmails();
		} catch (Exception e) {
			log.error("3일 후 신고 처리 결과 메일 발송 중 오류 발생", e);
		}
	}
	
	// 관리자 신고 이력 3년 경과 시 자동 삭제
	@Scheduled(cron = "0 0 3 * * *")
	public void threeYearsAgoDeleteAuditLogs() { 
		try {
			long deletedCount = service.deleteAuditLogs();
			log.info("삭제된 관리자 처리 이력: {}건", deletedCount);
			
		} catch (Exception e) {
			log.error("3년 경과 관리자 처리 이력 삭제 중 오류 발생", e);
		}
	}

	// 실패 이메일 재전송
	@Scheduled(fixedDelay = 60000)
	public void retrySendEmail() { 
		try {
			// 실패 이메일 꺼내기
			EmailRequestDto emailDto = failedEmailQueueService.pop();
			
			// 실패 이메일 없으면 종료
			if (emailDto == null) { return; }
			
			// 실패 이메일 재전송
			sendEmailService.sendEmail(emailDto);
			
		} catch (Exception e) {
			log.error("실패 이메일 재전송 중 오류 발생", e);
		}
	}
}