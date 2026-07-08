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
	
	//모집글등록
	public int insertMeetup(MeetupDto meetupDto);
	
	//모집글수정
	public int updateMeetup(MeetupDto meetupDto);
	
	//모집글 삭제
	public int updateMeetupDeleteYn(int meetupId);
	
	//마이페이지 내 모집글
	public List<MeetupDto> selectMyMeetup(int pstartno,MeetupDto meetupDto);
	public int selectMyMeetupTotalCnt(MeetupDto meetupDto);
	public MeetupDto selectMyPageStats(int memberId);
	
	//마이페이지 내 신청글
	public List<MeetupDto> selectMyMeetupApply(int pstartno,MeetupDto meetupDto);
	public int selectMyMeetupApplyTotalCnt(MeetupDto meetupDto);
	
	//마이페이지 내모집글 - 신청자목록조회
	public List<MeetupDto> selectMeetupApplyMember(int meetupId);
	//마이페이지 내모집글 - 신청자목록조회 - 신청,거절
	public int changeMeetupApplyStatus(MeetupApplicationDto meetupApplicationDto);	
}
