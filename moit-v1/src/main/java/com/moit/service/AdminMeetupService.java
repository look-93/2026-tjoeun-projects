package com.moit.service;

import java.util.HashMap;
import java.util.List;

import com.moit.dto.MeetupDto;
import com.moit.dto.MeetupSearchDto;

public interface AdminMeetupService {
	public List<MeetupDto> searchByAdmin(int pstartno, MeetupSearchDto meetupSerchDto);
	public int selectMeetupTotalCnt(MeetupSearchDto meetupSerchDto);
	public int deleteMeetup(int meetupId);
}

