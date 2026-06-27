package com.moit.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moit.dao.MeetupMapper;
import com.moit.dto.MeetupApplicationsDto;
import com.moit.dto.MeetupDto;
import com.moit.dto.MeetupImageDto;
import com.moit.dto.MeetupLikeDto;
import com.moit.dto.MeetupSearchDto;
import com.moit.dto.common.CategoryDto;
import com.moit.dto.common.SidoDto;
import com.moit.dto.common.SigunguDto;

@Service
public class UserMeetupServiceImpl implements UserMeetupService{
	@Autowired MeetupMapper meetupMapper;
	
	//모집조회+검색
	@Override
	public List<MeetupDto> searchByUser(int pstartno,MeetupSearchDto meetupSearchDto) {
		meetupSearchDto.setEnd(9);
		meetupSearchDto.setStart((pstartno-1)*9);
		return meetupMapper.searchByUser(meetupSearchDto);
	}
	
	//모집조회+검색
	@Override
	public int selectUserMeetupTotalCnt(MeetupSearchDto meetupSearchDto) {
		return meetupMapper.selectUserMeetupTotalCnt(meetupSearchDto);
	}
	
	//모집상세조회
	@Override
	public MeetupDto selectMeetupDetail(int meetupId) {
		return meetupMapper.selectMeetupDetail(meetupId);
	}
	
	//이미지조회
	@Override
	public MeetupImageDto selectMeetupImage(int meetupId) {
		return meetupMapper.selectMeetupImage(meetupId);
	}
	
	//모집저장
	@Override
	public int insertMeetup(MeetupDto meetupDto) {
		return meetupMapper.insertMeetup(meetupDto);
	}
	
	//모집수정
	@Override
	public int updateMeetup(MeetupDto meetupDto) {
		return meetupMapper.updateMeetup(meetupDto);
	}
	
	//모집글삭제
	@Override
	public int deleteMeetup(int meetupId) {		
		return meetupMapper.deleteMeetup(meetupId);
	}
	
	//마이페이지 내 모집글 조회 + paging
	@Override
	public List<MeetupDto> selectMyMeetup(int pstartno,MeetupDto meetupDto) {
		meetupDto.setEnd(4);
		meetupDto.setStart((pstartno-1)*4);		
		return meetupMapper.selectMyMeetup(meetupDto);
	}	
	
	//마이페이지 내 모집글 조회 + paging
	@Override
	public int selectMyMeetupTotalCnt(MeetupDto meetupDto) {
		return meetupMapper.selectMyMeetupTotalCnt(meetupDto);
	}

	//마이페이지 내 모집글 통계
	@Override
	public MeetupDto selectMyPageStats(int memberId) {
		return meetupMapper.selectMyPageStats(memberId);
	}
	
	//모집신청
	@Override
	public int insertApplication(MeetupApplicationsDto meetupApplicationsDto) {
		MeetupApplicationsDto find = this.findApplyInfo(meetupApplicationsDto);
		System.out.println(find);
		if(find != null) {
			return meetupMapper.updateApplication(find);	
		}else {
			return meetupMapper.insertApplication(meetupApplicationsDto);
		}
	}
	
	//모집신청 정보조회
	@Override
	public MeetupApplicationsDto findApplyInfo(MeetupApplicationsDto meetupApplicationsDto) {
		return meetupMapper.findApplyInfo(meetupApplicationsDto);
	}

	//모집신청 취소
	@Override
	public int cancelApplyMeetup(MeetupApplicationsDto meetupApplicationsDto) {
		return meetupMapper.cancelApplyMeetup(meetupApplicationsDto);
	}
	
	//마이페이지 내 신청글 조회 + paging
	@Override
	public List<MeetupDto> selectMyMeetupApply(int pstartno, MeetupDto meetupDto) {
		meetupDto.setEnd(4);
		meetupDto.setStart((pstartno-1)*4);
		return meetupMapper.selectMyMeetupApply(meetupDto);
	}
	
	//마이페이지 내 신청글 조회 + paging
	@Override
	public int selectMyMeetupApplyTotalCnt(MeetupDto meetupDto) {
		return meetupMapper.selectMyMeetupApplyTotalCnt(meetupDto);
	}
	
	//마이페이지 내모집글 - 신청자목록조회
	@Override
	public List<MeetupDto> selectMeetupApplyMember(int meetupId) {
		return meetupMapper.selectMeetupApplyMember(meetupId);
	}

	//마이페이지 내모집글 - 신청자목록조회 - 신청,거절
	@Override
	public int changeMeetupApplyStatus(MeetupApplicationsDto meetupApplicationsDto) {
		return meetupMapper.changeMeetupApplyStatus(meetupApplicationsDto);
	}

	// 시도
	@Override
	public List<SidoDto> findAllSido() {
		return meetupMapper.findAllSido();
	}
	
	// 시군구
	@Override
	public List<SigunguDto> findAllSigungu() {
		return meetupMapper.findAllSigungu();
	}
	
	//부모카테고리
	@Override
	public List<CategoryDto> findAllCategory() {
		return meetupMapper.findAllCategory();
	}
	
	//자식카테고리
	@Override
	public List<CategoryDto> findAllChildCategory() {
		return meetupMapper.findAllChildCategory();
	}

	//login_id로 member_id 찾기
	@Override
	public int findByMamberId(String loginId) {
		return meetupMapper.findByMamberId(loginId);
	}
	
	//좋아요기능
	@Override
	public boolean insertMeetupLike(MeetupLikeDto meetupLikeDto) {
		MeetupLikeDto find = this.selectMeetupLike(meetupLikeDto);
		if(find != null) {
			meetupMapper.deleteMeetupLike(find);	
			return false;
		}else {
			meetupMapper.insertMeetupLike(meetupLikeDto);
			return true;
		}
	}
	
	//좋아요갯수조회
	@Override
	public int countMeetupLike(int meetupId) {
		return meetupMapper.countMeetupLike(meetupId);
	}

	@Override
	public int deleteMeetupLike(MeetupLikeDto meetupLikeDto) {
		return meetupMapper.deleteMeetupLike(meetupLikeDto);
	}

	@Override
	public MeetupLikeDto selectMeetupLike(MeetupLikeDto meetupLikeDto) {
		return meetupMapper.selectMeetupLike(meetupLikeDto);
	}

}
