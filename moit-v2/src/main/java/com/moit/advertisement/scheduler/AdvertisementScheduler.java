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

 // 5분마다 실행
    @Scheduled(cron = "0 */5 * * * *")
    public void updateAdvertisementPriority() {


        int count =
            advertisementService.updatePriorityScore();


        System.out.println(
            "광고 우선도 갱신 : " + count
       );

    }
}