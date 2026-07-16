package com.moit.advertisement.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.moit.advertisement.dto.DashboardAiDto;
import com.moit.advertisement.service.AdvertisementService;
import com.moit.advertisement.service.AiSummaryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdvertisementScheduler {

    private final AdvertisementService advertisementService;
    private final AiSummaryService aiSummaryService;


    // 1분마다 광고 상태 체크
    @Scheduled(cron = "0 * * * * *")
    public void updateAdvertisementStatus() {
    	
    	System.out.println("광고 상태 체크 실행");

        advertisementService.updateAdvertisementStatus();

    }

    // 5분마다 광고 우선도 갱신 실행 
    @Scheduled(cron = "0 */5 * * * *")
    public void updateAdvertisementPriority() {


        int count =
            advertisementService.updatePriorityScore();

        System.out.println(
            "광고 우선도 갱신 : " + count
       );

    }
    // 매일 새벽 1시  일일통계 저장
    @Scheduled(cron = "0 0 1 * * *")
    //@Scheduled(cron = "0 * * * * *")
    public void createDailyStatistics(){
    	System.out.println("===== 광고 일일 통계 생성 시작 =====");
        advertisementService.insertDailyStatistics();
    	System.out.println("===== 광고 일일 통계 생성 완료 =====");
    }
    
    // 매일 오전 9시 광고기간 만료 30/14일자 발송
    @Scheduled(cron = "0 0 9 * * *")
//    @Scheduled(cron = "0 */1 * * * *")
    public void advertisementReminder() {
    	System.out.println("스케줄러 실행");
        advertisementService.sendReminderMail();

    }
//    // 3시간마다 통계 ai 요약 저장
//    @Scheduled(cron = "0 0 */3 * * *")
//    //@Scheduled(cron = "0 */1 * * * *")
//    public void generateAiSummary() {
//    	System.out.println("ai 요약 시작");
//    	// 1. 통계 데이터 조회
//        DashboardAiDto dto = advertisementService.getDashboardAiData();
//        // 2. GPT 분석 생성
//        String summary = aiSummaryService.createSummary(dto);
//        // 3. DB 저장	
//        advertisementService.saveAiSummary(summary);
//        System.out.println("ai 요약 종료");
//    }
}