package com.moit.meetup.service;

import java.util.List;

import com.moit.meetup.dto.MeetupDto;
import com.moit.meetup.dto.MeetupSearchDto;

public interface AdminMeetupService {	
	
	//관리자 - 목록 조회 + paging
	public List<MeetupDto> findAllMeetupBy(int pstartno, MeetupSearchDto meetupSearchDto);
	public int findAllMeetupCountBy(MeetupSearchDto meetupSearchDto);
	//관리자 - 모임 리스트 삭제
	public int updateMeetupDeleteYn(int meetupId);

	
	
}
