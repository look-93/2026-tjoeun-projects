package com.moit.service;

import java.util.List;

import com.moit.dto.MeetupApplicationsDto;
import com.moit.dto.MeetupDto;
import com.moit.dto.MeetupImageDto;
import com.moit.dto.MeetupLikeDto;
import com.moit.dto.MeetupSearchDto;
import com.moit.dto.common.CategoryDto;
import com.moit.dto.common.SidoDto;
import com.moit.dto.common.SigunguDto;

public interface UserMeetupService {
	public List<MeetupDto> searchByUser(int pstartno,MeetupSearchDto meetupSearchDto);
	public int selectUserMeetupTotalCnt(MeetupSearchDto meetupSearchDto);	
	public MeetupDto selectMeetupDetail(int meetupId);
	public MeetupImageDto selectMeetupImage(int meetupId);
	public int insertMeetup(MeetupDto meetupDto);
	public int updateMeetup(MeetupDto meetupDto);
	public int deleteMeetup(int meetupId);
	
	public int insertApplication(MeetupApplicationsDto meetupApplicationsDto);
	public MeetupApplicationsDto findApplyInfo(MeetupApplicationsDto meetupApplicationsDto);
	
	//좋아요기능
	public boolean insertMeetupLike(MeetupLikeDto meetupLikeDto);
	public int deleteMeetupLike(MeetupLikeDto meetupLikeDto);
	public MeetupLikeDto selectMeetupLike(MeetupLikeDto meetupLikeDto);
	public int countMeetupLike(int meetupId);
	
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
	public int changeMeetupApplyStatus(MeetupApplicationsDto meetupApplicationsDto);
	
	public List<SidoDto> findAllSido();
	public List<SigunguDto> findAllSigungu();
	public List<CategoryDto> findAllCategory();
	public List<CategoryDto> findAllChildCategory();
	public int findByMamberId(String loginId);
	public int cancelApplyMeetup(MeetupApplicationsDto meetupApplicationsDto);	
}
