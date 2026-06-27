package com.moit.dto;

import java.util.List;

import lombok.Data;

@Data
public class MeetupApplicationsDto {
	private int applicationId;
	private int meetupId;
	private int memberId;
	private String status;
	private String rejectReason;
	private String deleteYn;
	private String createdAt;
	private String updatedAt;	
	private List<String> statusList;
}
