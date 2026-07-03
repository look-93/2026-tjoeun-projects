package com.moit.meetup.service;

import java.util.List;

import com.moit.meetup.dto.MeetupDto;
import com.moit.meetup.dto.MeetupSearchDto;

public interface AdminMeetupService {	
	
	/* 관리자 */
	
	//관리자 meetup 조회
	public List<MeetupDto> findAllMeetupBy(int pstartno, MeetupSearchDto meetupSearchDto);
	public int findAllMeetupCountBy(MeetupSearchDto meetupSearchDto);
	/* 관리자 */
	
	
}
