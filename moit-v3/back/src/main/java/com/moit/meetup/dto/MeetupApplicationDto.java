package com.moit.meetup.dto;

import java.util.List;

import com.moit.meetup.dto.MeetupDto.MeetupResponseDto;
import com.moit.meetup.entity.MeetupApplication;
import com.moit.meetup.enums.ApplyStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MeetupApplicationDto {
	
	@Getter
	@Setter
	@NoArgsConstructor
	public static class MeetupApplicationRequestDto{
		private Long applicationId;
		private String rejectReason;
		private ApplyStatus applyStatus;
	}

	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MeetupApplicationResponseDto {
		//내가 신청한 모집글
		private Long id;
		private String rejectReason;
		private Long meetupId;
		private String meetupTitle;
		private String meetupAt;
		private ApplyStatus applyStatus;
		private String meetupStatus;
		
		public static MeetupApplicationResponseDto fromEntity(MeetupApplication application) {
			return MeetupApplicationResponseDto.builder()
										.id(application.getId())
										.applyStatus(application.getApplyStatus())
						                .rejectReason(application.getRejectReason())
						                .meetupId(application.getMeetup().getId())
						                .meetupTitle(application.getMeetup().getTitle())
						                .meetupAt(application.getMeetup().getMeetupAt())
						                .build();
		}
	}
	
	@Getter
	@Setter
	public static class MyApplicationListResponseDto{
		//내가 신청한 모집글 목록 조회(페이징)
		private List<MeetupApplicationResponseDto> applications;
		private Long totalCount; //전체갯수 100 , 101
		private Long totalPage;  //총 몇개 페이지를 만들것인가 10개씩 보여준다고하면  10개 페이지가 나옴 , 11페이지
	}	
	
	@Getter
	@Setter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor	
	public static class MeetupApplicantResponseDto {
		//마이페이지 내 모집글 신청자조회
		private Long memberId;
		private String nickname;
		private String rejectReason;
		private String aiSummary; 
		private ApplyStatus applyStatus;
		
		public static MeetupApplicantResponseDto fromEntity(MeetupApplication application) {
			return MeetupApplicantResponseDto.builder()
										.memberId(application.getMember().getId())
										.nickname(application.getMember().getNickname())
						                .rejectReason(application.getRejectReason())
						                .aiSummary(application.getMember().getMemberInfo().getAiSummary())
						                .applyStatus(application.getApplyStatus())
						                .build();
		}
	}
	
	@Getter
	@Setter
	public static class MeetupApplyMemberListResponseDto{
		//마이페이지 내 모집글 신청자 리스트(페이징)
		private List<MeetupApplicantResponseDto> applicants;
		private Long totalCount; //전체갯수 100 , 101
		private Long totalPage;  //총 몇개 페이지를 만들것인가 10개씩 보여준다고하면  10개 페이지가 나옴 , 11페이지
	}
	
}
