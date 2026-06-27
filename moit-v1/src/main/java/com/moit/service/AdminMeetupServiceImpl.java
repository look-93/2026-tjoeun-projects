package com.moit.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moit.dao.MeetupMapper;
import com.moit.dto.MeetupDto;
import com.moit.dto.MeetupSearchDto;

@Service
public class AdminMeetupServiceImpl implements AdminMeetupService{
	
	@Autowired MeetupMapper meetupMapper;
	
	

	@Override
	public List<MeetupDto> searchByAdmin(int pstartno,MeetupSearchDto meetupSearchDto) {	
		meetupSearchDto.setEnd(10);
		meetupSearchDto.setStart((pstartno-1)*10);
		return meetupMapper.searchByAdmin(meetupSearchDto);
	}

	@Override
	public int selectMeetupTotalCnt(MeetupSearchDto meetupSearchDto) {
		return meetupMapper.selectMeetupTotalCnt(meetupSearchDto);
	}

	@Override
	public int deleteMeetup(int meetupId) {		
		return meetupMapper.deleteMeetup(meetupId);
	}	
	
	

}
