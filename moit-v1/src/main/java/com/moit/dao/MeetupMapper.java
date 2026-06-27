package com.moit.dao;

import java.util.List;

import com.moit.dto.MeetupApplicationsDto;
import com.moit.dto.MeetupDto;
import com.moit.dto.MeetupImageDto;
import com.moit.dto.MeetupLikeDto;
import com.moit.dto.MeetupSearchDto;
import com.moit.dto.common.CategoryDto;
import com.moit.dto.common.SidoDto;
import com.moit.dto.common.SigunguDto;

@Mapper
public interface MeetupMapper {
	
	//admin
	public List<MeetupDto> searchByAdmin(MeetupSearchDto meetupSearchDto);
	public int selectMeetupTotalCnt(MeetupSearchDto meetupSearchDto);	
	public int deleteMeetup(int meetupId);
	
	//user
	public List<MeetupDto> searchByUser(MeetupSearchDto meetupSearchDto);
	public int selectUserMeetupTotalCnt(MeetupSearchDto meetupSearchDto);	
	public MeetupDto selectMeetupDetail(int meetupId);
	public MeetupImageDto selectMeetupImage(int meetupId);
	public int insertMeetup(MeetupDto meetupDto);
	public int updateMeetup(MeetupDto meetupDto);
	
	public int insertApplication(MeetupApplicationsDto meetupApplicationsDto);
	public MeetupApplicationsDto findApplyInfo(MeetupApplicationsDto meetupApplicationsDto);
	public int cancelApplyMeetup(MeetupApplicationsDto meetupApplicationsDto);
	public int updateApplication(MeetupApplicationsDto meetupApplicationsDto);
	
	
	//좋아요기능
	public int insertMeetupLike(MeetupLikeDto meetupLikeDto);
	public int deleteMeetupLike(MeetupLikeDto meetupLikeDto);
	public MeetupLikeDto selectMeetupLike(MeetupLikeDto meetupLikeDto);
	public int countMeetupLike(int meetupId);
	
	
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
	public int changeMeetupApplyStatus(MeetupApplicationsDto meetupApplicationsDto);

	//시도, 시군구
	public List<SidoDto> findAllSido();
	public List<SigunguDto> findAllSigungu();
	
	//카테고리
	public List<CategoryDto> findAllCategory();
	public List<CategoryDto> findAllChildCategory();	
	
	//login_id로 member_id 찾기
	public int findByMamberId(String loginId);

	
}