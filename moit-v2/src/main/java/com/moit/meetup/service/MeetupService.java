package com.moit.meetup.service;

import java.util.List;

import com.moit.meetup.dto.MeetupDto;
import com.moit.meetup.dto.MeetupSearchDto;

public interface MeetupService {	
	
	/* 관리자 */
	
	//관리자 meetup 조회
	public List<MeetupDto> adminList(int pstartno, MeetupSearchDto meetupSearchDto);
	
	/* 관리자 */
	
	
}
