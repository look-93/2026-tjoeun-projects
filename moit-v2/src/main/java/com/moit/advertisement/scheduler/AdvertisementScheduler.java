package com.moit.advertisement.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.moit.advertisement.service.AdvertisementService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdvertisementScheduler {

    private final AdvertisementService advertisementService;


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
    // 매일 새벽 1시  일일통계 저장(미완)
//    @Scheduled(cron = "0 * * * * *")
//    public void createDailyStatistics(){
//
//        advertisementService.insertDailyStatistics();
//
//    }
    
    // 매일 오전 9시 광고기간 만료 14/7일자 발송
    @Scheduled(cron = "0 0 9 * * *")
    //@Scheduled(cron = "0 */1 * * * *")
    public void advertisementReminder() {

        advertisementService.sendReminderMail();

    }
}