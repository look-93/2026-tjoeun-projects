package com.moit;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moit.meetup.client.OpenApiService;
import com.moit.meetup.dto.openapi.AddressSearchResponse;
import com.moit.meetup.dto.openapi.WeatherInfoRequest;
import com.moit.meetup.dto.openapi.WeatherInfoResponse;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SampleController {
	// tmptAdmin 복사해서 content안에 본인 html 붙여넣으면됩니다
	
	private final OpenApiService openApiService;
	
	// 탬플릿 샘플
	@GetMapping("/tmptAdmin")
	public String tmptAdmin() {return "/admin/temp/tmptAdmin"; } //prefix(/templates) + tmpt + suffix(.html)
	
	@GetMapping("/tmptMain")
	public String tmptMain() {return "/user/temp/tmptMain"; }

	@GetMapping("/tmptMeetupList")
	public String tmptMeetupList() {return "/user/temp/tmptMeetupList"; }
	
	@GetMapping("/tmptMeetupDetail")
	public String tmptMeetupDetail() {return "/user/temp/tmptMeetupDetail"; }	
	
	@GetMapping("/tmptMypage")
	public String tmptMypage() {return "/user/temp/tmptMypage"; }

	// admin 페이지 샘플 
	@GetMapping("/sample/admin")
	public String admin() {return "/sample/admin"; }

	// main 페이지 샘플 
	@GetMapping("/sample/main")
	public String main() {return "/sample/main"; }

	// meetupList 페이지 샘플 
	@GetMapping("/sample/meetupList")
	public String meetupList() {return "/sample/meetupList"; }
	
	// meetupDetail 페이지 샘플 
	@GetMapping("/sample/meetupDetail")
	public String meetupDetail() {return "/sample/meetupDetail"; }	
	
	// mypage 페이지 샘플 
	@GetMapping("/sample/mypage")
	public String mypage() {return "/sample/mypage"; }		
	
	// mypage 페이지 샘플 
	@GetMapping("/test/weather-info")
	@ResponseBody
	public WeatherInfoResponse weathreInfo() {
		WeatherInfoRequest request  = new WeatherInfoRequest();
		request.setMeetupDate("20260710");
		request.setMeetupTime(23);
		//System.out.println(java.time.LocalTime.now());
		//화면에서 보낼예정
		request.setNx(55);
		request.setNy(127);
		WeatherInfoResponse response = openApiService.getWeathreInfo(request);
		return response;
	}		
	
	@GetMapping("/test/address-search")
	@ResponseBody
	public AddressSearchResponse addressSearch() {
		AddressSearchResponse res = openApiService.addressSearch("인천시 부평구",1);
		return res;
	}	
}
