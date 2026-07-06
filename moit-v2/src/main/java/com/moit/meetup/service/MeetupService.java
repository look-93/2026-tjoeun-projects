package com.moit.meetup.service;

import java.util.List;

import com.moit.meetup.dto.MeetupApplicationDto;
import com.moit.meetup.dto.MeetupDto;
import com.moit.meetup.dto.MeetupLikeDto;
import com.moit.meetup.dto.MeetupSearchDto;
import com.moit.meetup.dto.common.CategoryDto;
import com.moit.meetup.dto.common.SidoDto;
import com.moit.meetup.dto.common.SigunguDto;

public interface MeetupService {
	// 사용자 - 목록 조회 + paging
	public List<MeetupDto> findAllMeetupBy(int pstartno, MeetupSearchDto meetupSearchDto);
	public int findAllMeetupCountBy(MeetupSearchDto meetupSearchDto);
	
	//시도, 시군구
	public List<SidoDto> findAllSido();
	public List<SigunguDto> findAllSigungu();
	
	//카테고리
	public List<CategoryDto> findAllCategory();
	public List<CategoryDto> findAllChildCategory();
	
	//좋아요기능
	public boolean insertMeetupLike(MeetupLikeDto meetupLikeDto);
	public int deleteMeetupLike(MeetupLikeDto meetupLikeDto);
	public MeetupLikeDto selectMeetupLike(MeetupLikeDto meetupLikeDto);
	public int countMeetupLike(MeetupLikeDto meetupLikeDto);	
	
	//사용자 - 모임 상세조회 - 모임 상세조회
	public MeetupDto selectMeetupDetail(int meetupId);
	//사용자 - 모임 신청정보
	public MeetupApplicationDto findApplyInfo(MeetupApplicationDto meetupApplicationsDto);
	//모임 신청
	public int insertApplication(MeetupApplicationDto meetupApplicationDto);
	//모임 신청 취소
	public int cancelApplyMeetup(MeetupApplicationDto meetupApplicationDto);	
}
