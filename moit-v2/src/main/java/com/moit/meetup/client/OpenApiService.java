package com.moit.meetup.client;

import java.util.List;

import com.moit.meetup.dto.openapi.AddressSearchResponse;
import com.moit.meetup.dto.openapi.WeatherInfoRequest;
import com.moit.meetup.dto.openapi.WeatherInfoResponse;

public interface OpenApiService {
	public WeatherInfoResponse getWeathreInfo(WeatherInfoRequest request);
	public AddressSearchResponse addressSearch(String keyword,Integer pstartno);
}
