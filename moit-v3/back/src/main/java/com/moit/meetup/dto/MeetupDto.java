package com.moit.meetup.dto;

import java.util.List;

import com.moit.common.entity.Sigungu;
import com.moit.meetup.entity.Meetup;
import com.moit.meetup.enums.ApplyStatus;
import com.moit.meetup.enums.MeetupStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MeetupDto {
	
	@Setter
	@Getter
	@NoArgsConstructor
	public static class MeetupRequestDto{
		private Long id;
		private Long memberId;
		private String title;
		private String content;
		private Integer maxParticipants;
		private Integer minParticipants;
		private Long sigunguId;
		private Long categoryId;
		private String address;
		private String meetupAt;
		private MeetupStatus meetupStatus;
		private Double latitude;
		private Double longitude;
		private String addressDetail;
		private Integer nx;
		private Integer ny;	
	}
	
	@Getter
	@Setter
	public static class MeetupResponseDto{
		private Long id;
		private String title;
		private String content;
		private Integer maxParticipants;
		private Integer minParticipants;

		private String address;
		private String meetupAt;
		private MeetupStatus meetupStatus;
		private Double latitude;
		private Double longitude;
		private String addressDetail;
		private Integer nx;
		private Integer ny;
		private ApplyStatus applyStatus;
		
		private String nickname;
		private String sigunguName;
		private String sidoName;
		
		private Long totalParticipants;
		
		public static MeetupResponseDto listFrom(Meetup meetup) { // list에만 보여줄 MeetupResponse
			MeetupResponseDto response = new MeetupResponseDto();
			Sigungu sigungu = meetup.getSigungu();
			response.setTitle(meetup.getTitle());
			response.setNickname(meetup.getMember().getNickname());
			
			response.setSidoName(sigungu.getSido().getName());
			response.setSigunguName(sigungu.getName());
			
			//좋아요
			//지역
			//등등...
			return response;
		}
		
		public static MeetupResponseDto detailFrom(Meetup meetup) { // 상세페이지 MeetupResponse
		    MeetupResponseDto response = new MeetupResponseDto();

		    response.setId(meetup.getId());
		    response.setTitle(meetup.getTitle());
		    response.setContent(meetup.getContent());
		    response.setMaxParticipants(meetup.getMaxParticipants());
		    response.setMinParticipants(meetup.getMinParticipants());
		    response.setAddress(meetup.getAddress());
		    response.setMeetupAt(meetup.getMeetupAt());
		    response.setMeetupStatus(meetup.getMeetupStatus());
		    response.setLatitude(meetup.getLatitude());
		    response.setLongitude(meetup.getLongitude());
		    response.setAddressDetail(meetup.getAddressDetail());
		    response.setNx(meetup.getNx());
		    response.setNy(meetup.getNy());

		    return response;
		}
	}
	
	//목록조회 응답용 dto
	@Getter
	@Setter
	public static class MeetupListResponseDto{
		private List<MeetupResponseDto> meetups;
		private Long totalCount; //전체갯수 100 , 101
		private Long totalPage;  //총 몇개 페이지를 만들것인가 10개씩 보여준다고하면  10개 페이지가 나옴 , 11페이지
	}
	
}

