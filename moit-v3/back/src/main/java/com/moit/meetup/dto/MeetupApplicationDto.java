package com.moit.meetup.dto;

import com.moit.meetup.entity.MeetupApplication;
import com.moit.meetup.enums.ApplyStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MeetupApplicationDto {
	private Long id;
	private ApplyStatus status;
	private String rejectReason;
	
	private Long meetupId;
	private String meetupTitle;
	private String meetupAt;
	
	public static MeetupApplicationDto  fromEntity(MeetupApplication application) {
		return MeetupApplicationDto.builder()
									.id(application.getId())
									.status(application.getStatus())
					                .rejectReason(application.getRejectReason())
					                .meetupId(application.getMeetup().getId())
					                .meetupTitle(application.getMeetup().getTitle())
					                .meetupAt(application.getMeetup().getMeetupAt())
					                .build();
	}
	
}
