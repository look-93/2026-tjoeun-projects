package com.moit.meetup.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.moit.meetup.dto.AdminMeetupStatusSummaryDto;
import com.moit.meetup.dto.MeetupDto;
import com.moit.meetup.dto.MeetupSearchDto;

@Mapper
public interface MeetupMapper {
	
	//관리자 - 목록 조회 + paging
	public List<MeetupDto> findAllMeetupBy(MeetupSearchDto meetupSearchDto);
	public int findAllMeetupCountBy(MeetupSearchDto meetupSearchDto);
	//관리자 상단 통계
	public AdminMeetupStatusSummaryDto findAdminMeetupStatusSummary();	
	//관리자 - 모임 리스트 삭제
	public int updateMeetupDeleteYn(int meetupId);	
	
}