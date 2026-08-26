package com.moit.meetup.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.moit.meetup.service.MeetupService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MeetupScheduler {
	
	private final MeetupService meetupService;
	
	@Scheduled(cron = "0 * * * * *")
    //@Scheduled(cron = "0 0 18 * * *")
    public void sendWeatherNotification() {
    	
    	meetupService.sendTomorrowWeatherNotification();
    }

}
