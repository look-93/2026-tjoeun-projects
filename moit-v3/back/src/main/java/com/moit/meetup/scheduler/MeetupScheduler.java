package com.moit.meetup.scheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.moit.meetup.client.OpenApiService;
import com.moit.meetup.dto.MeetupDto;
import com.moit.meetup.dto.MeetupWeatherNotificationDto;
import com.moit.meetup.dto.openapi.WeatherInfoRequest;
import com.moit.meetup.dto.openapi.WeatherInfoResponse;
import com.moit.meetup.service.MeetupService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MeetupScheduler {
	
	private final OpenApiService openApiService;
	private final MeetupService meetupService;
	
    // 5분마다 광고 우선도 갱신 실행 
	@Scheduled(cron = "0 0 * * * *")
    public void sendKakaoWeatherInfo() {
		List<MeetupDto> list =  meetupService.selectMeetupsBeforeTwoHours();
		
		for(MeetupDto dto : list) {
			WeatherInfoRequest request  = new WeatherInfoRequest();
			DateTimeFormatter inputFormatter =
			        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			LocalDateTime dateTime = LocalDateTime.parse(dto.getMeetupAt(), inputFormatter);
			request.setMeetupDate(dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
			request.setMeetupTime(dateTime.getHour());
			request.setNx(request.getNx());
			request.setNy(request.getNy());
			WeatherInfoResponse response = openApiService.getWeathreInfo(request);
			MeetupWeatherNotificationDto notiDto = new MeetupWeatherNotificationDto();
			notiDto.setMeetupId(dto.getMeetupId());
			notiDto.setMemberId(dto.getMemberId());
			notiDto.setMobile(dto.getMobile());
			notiDto.setSendStatus("SENT");
			meetupService.insertNotification(notiDto);
		}
		
		
		
		

    }
}
