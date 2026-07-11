package com.moit.meetup.client;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.moit.meetup.dto.openapi.AddressSearchResponse;
import com.moit.meetup.dto.openapi.WeatherInfoRequest;
import com.moit.meetup.dto.openapi.WeatherInfoResponse;
import com.moit.util.GridUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OpenApiServiceImpl implements OpenApiService{
	

	@Value("${kma.api.key}") 
	private String kmaApiKey;	
    @Value("${vworld.api.key}")
    private String apiKey;
    @Qualifier("vworldRestClient")
    private final RestClient vworldRestClient;
    @Qualifier("weatherRestClient")
    private final RestClient weatherRestClient; 
    private final ObjectMapper objectMapper = new ObjectMapper();
    
	@Override
	public WeatherInfoResponse getWeathreInfo(WeatherInfoRequest request) {
//		0200(3시~5시), 0500(6시~8시), 0800(9시~11시), 1100(12시~14시), 1400(15시~17시), 1700(18시~20시), 2000(21시~23시), 2300(24시~02시)
		LocalTime now = LocalTime.now();
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");
		Integer formattedNow = Integer.parseInt(now.format(formatter));
		
		DateTimeFormatter formatterYYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
		String baseDate = date.format(formatterYYYYMMDD);
		
		String baseTime = switch (formattedNow) {
	    case 0, 1, 2 -> "2300";
	    case 3, 4, 5 -> "0200";
	    case 6, 7, 8 -> "0500";
	    case 9, 10, 11 -> "0800";
	    case 12, 13, 14 -> "1100";
	    case 15, 16, 17 -> "1400";
	    case 18, 19, 20 -> "1700";
	    case 21, 22, 23 -> "2000";
	    default -> throw new IllegalStateException("Unexpected value: " + formattedNow);
		};

		
		String xml  = weatherRestClient
				.get()
				.uri("/getVilageFcst?serviceKey="+kmaApiKey+"&numOfRows=1000&pageNo=1&base_date="+baseDate+"&base_time="+baseTime+"&nx="+request.getNx()+"&ny="+request.getNy())
				.retrieve()
		        .body(String.class); //날씨 데이터를 받아온다. 현재날짜, 현재시간, nx, ny기준으로 1000개
		
		XmlMapper xmlMapper = new XmlMapper(); // xml형식으로 변환
		WeatherInfoResponse result = new WeatherInfoResponse();
		try {
			JsonNode node = xmlMapper.readTree(xml.getBytes()); //xml -> json형식으로 변환
			
			Map<String, Object> response = objectMapper.convertValue( // 변환된 json형식의 데이터를  map 형식으로 변환
			        node,
			        new TypeReference<Map<String, Object>>() {}
			);
			
			Map<String, Object> body = (Map<String, Object>) response.get("body"); // map -> response 에서 body꺼내오기
			
			Map<String, Object> items = (Map<String, Object>) body.get("items"); // map -> items 에서 item 꺼내오기
			List<Map<String, Object>> itemList = 
			        (List<Map<String, Object>>) items.get("item");
			List<Map<String, Object>> filteredItemList = itemList.stream().filter(item->{
				String fcstDate = (String)item.get("fcstDate");
				String fcstTime = (String)item.get("fcstTime");
				return fcstDate.equals(request.getMeetupDate()) && fcstTime.equals(request.getMeetupTime() + "00");
			}).toList();

			/* <item>
			<baseDate>20260710</baseDate>
			<baseTime>0500</baseTime>
			<category>TMP</category>
			<fcstDate>20260710</fcstDate>
			<fcstTime>0600</fcstTime>
			<fcstValue>24</fcstValue>
			<nx>55</nx>
			<ny>127</ny>
			</item> 를 리스트로 받기*/ 
			
			for(Map<String, Object> item : filteredItemList ) { //아이템에서 값 뽑아오기
				String category = (String)item.get("category");				
				String fcstValue = (String)item.get("fcstValue");
//				String fcstDate = (String)item.get("fcstDate");
//				String fcstTime = (String)item.get("fcstTime");
//				System.out.println(fcstDate);
//				System.out.println(fcstTime);
				if(category.equals("TMP")) {
					result.setTmp(Double.parseDouble(fcstValue));
				}else if(category.equals("POP")) {
					result.setPop(Integer.parseInt(fcstValue));
				}else if(category.equals("SKY")) {
					String skyString = switch(Integer.parseInt(fcstValue)) {
					case 0,1,2,3,4,5 -> "맑음";
					case 6,7,8 -> "구름많음";
					case 9,10 -> "흐림";
					 default -> throw new IllegalStateException("Unexpected value: " + formattedNow);
					};
					result.setSky(skyString);
				}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		 return result;
		
	}

	@Override
    public AddressSearchResponse addressSearch(String keyword,Integer pstartno) {

        String json = vworldRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/req/search")
                        .queryParam("service", "search")
                        .queryParam("request", "search")
                        .queryParam("version", "2.0")
                        .queryParam("crs", "EPSG:4326")
                        .queryParam("size", 10)
                        .queryParam("page", 1)
                        .queryParam("type", "ADDRESS")
                        .queryParam("category", "ROAD")
                        .queryParam("format", "json")
                        .queryParam("errorformat", "json")
                        .queryParam("key", apiKey)
                        .queryParam("query", keyword)
                        .build())
                .retrieve()
                .body(String.class);

        try {

            JsonNode root = objectMapper.readTree(json);

            JsonNode items = root.path("response")
                                 .path("result")
                                 .path("items");
            
            JsonNode page = root.path("response")
                    .path("page");
            AddressSearchResponse response = new AddressSearchResponse();

            List<AddressSearchResponse.AddressSearchDto> list = new ArrayList<>();

            for (JsonNode item : items) {

            	AddressSearchResponse.AddressSearchDto dto = new AddressSearchResponse.AddressSearchDto();

                dto.setAddress(item.path("address").path("road").asText());
                dto.setRoad(item.path("address").path("road").asText());
                dto.setJibun(item.path("address").path("parcel").asText());
                dto.setZipNo(item.path("address").path("zipcode").asText());
                dto.setLongitude(item.path("point").path("x").asDouble());
                dto.setLatitude(item.path("point").path("y").asDouble());               
                String[] addr = dto.getRoad().split(" ");

                dto.setSido(addr[0]);
                dto.setSigungu(addr[1]);
                GridUtil.Grid grid = GridUtil.convert(dto.getLatitude(), dto.getLongitude());
                dto.setNx(grid.nx());
                dto.setNy(grid.ny());

                list.add(dto);
            }
            response.setTotalCount(page.path("total").asInt());
            response.setList(list);

            return response;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

	
}
