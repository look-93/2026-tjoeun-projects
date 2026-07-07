package com.moit.meetup.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.moit.meetup.dto.AdminMeetupStatusSummaryDto;
import com.moit.meetup.dto.MeetupApplicationDto;
import com.moit.meetup.dto.MeetupDto;
import com.moit.meetup.dto.MeetupLikeDto;
import com.moit.meetup.dto.MeetupSearchDto;
import com.moit.meetup.dto.common.CategoryDto;
import com.moit.meetup.dto.common.SidoDto;
import com.moit.meetup.dto.common.SigunguDto;

@Mapper
public interface MeetupMapper {
	
	//관리자,사용자 - 목록 조회 + paging
	public List<MeetupDto> findAllMeetupBy(MeetupSearchDto meetupSearchDto);
	public int findAllMeetupCountBy(MeetupSearchDto meetupSearchDto);
	//관리자 상단 통계
	public AdminMeetupStatusSummaryDto findAdminMeetupStatusSummary();	
	//관리자 - 모임 리스트 삭제
	public int updateMeetupDeleteYn(int meetupId);	
	
	//사용자 - 모임 상세조회
	public MeetupDto selectMeetupDetail(int meetupId);
	
	//사용자 - 모임 신청정보
	public MeetupApplicationDto findApplyInfo(MeetupApplicationDto meetupApplicationDto);
	public int insertApplication(MeetupApplicationDto meetupApplicationDto);
	public int cancelApplyMeetup(MeetupApplicationDto meetupApplicationDto);
	public int updateApplication(MeetupApplicationDto meetupApplicationDto);
	
	//모집글등록
	public int insertMeetup(MeetupDto meetupDto);
	
	//모집글수정
	public int updateMeetup(MeetupDto meetupDto);
	
	//마이페이지 내 모집글 조회 + paging
	public List<MeetupDto> selectMyMeetup(MeetupDto meetupDto);
	public int selectMyMeetupTotalCnt(MeetupDto meetupDto);
	//마이페이지 내 모집글 통계
	public MeetupDto selectMyPageStats(int memberId);
	
	//마이페이지 내 신청글 조회 + paging
	public List<MeetupDto> selectMyMeetupApply(MeetupDto meetupDto);
	public int selectMyMeetupApplyTotalCnt(MeetupDto meetupDto);
	
	//마이페이지 내모집글 - 신청자목록조회
	public List<MeetupDto> selectMeetupApplyMember(int meetupId);
	//마이페이지 내모집글 - 신청자목록조회 - 신청,거절
	public int changeMeetupApplyStatus(MeetupApplicationDto meetupApplicationDto);
	
	//시도, 시군구
	public List<SidoDto> findAllSido();
	public List<SigunguDto> findAllSigungu();
	
	//카테고리
	public List<CategoryDto> findAllCategory();
	public List<CategoryDto> findAllChildCategory();	
	
	//좋아요기능
	public int insertMeetupLike(MeetupLikeDto meetupLikeDto);
	public int deleteMeetupLike(MeetupLikeDto meetupLikeDto);
	public MeetupLikeDto selectMeetupLike(MeetupLikeDto meetupLikeDto);
	public int countMeetupLike(MeetupLikeDto meetupLikeDto);	
	

}