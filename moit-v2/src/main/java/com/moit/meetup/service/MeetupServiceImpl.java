package com.moit.meetup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moit.meetup.dao.MeetupMapper;
import com.moit.meetup.dto.MeetupApplicationDto;
import com.moit.meetup.dto.MeetupDto;
import com.moit.meetup.dto.MeetupLikeDto;
import com.moit.meetup.dto.MeetupSearchDto;
import com.moit.meetup.dto.common.CategoryDto;
import com.moit.meetup.dto.common.SidoDto;
import com.moit.meetup.dto.common.SigunguDto;

@Service
public class MeetupServiceImpl implements MeetupService{
	@Autowired MeetupMapper meetupMapper; 
	
	//사용자 - 목록 조회 + paging
	@Override
	public List<MeetupDto> findAllMeetupBy(int pstartno, MeetupSearchDto meetupSearchDto) {
		int pageSize = 10;
		meetupSearchDto.setEnd(pageSize);
		meetupSearchDto.setStart((pstartno-1)*10);
		//System.out.println(pstartno);
		return meetupMapper.findAllMeetupBy(meetupSearchDto);
	}
	@Override
	public int findAllMeetupCountBy(MeetupSearchDto meetupSearchDto) {
		return meetupMapper.findAllMeetupCountBy(meetupSearchDto);
	}
	
	//모집신청 정보조회
	@Override
	public MeetupApplicationDto findApplyInfo(MeetupApplicationDto meetupApplicationDto) {
		return meetupMapper.findApplyInfo(meetupApplicationDto);
	}	
	
	//모집상세조회
	@Override
	public MeetupDto selectMeetupDetail(int meetupId) {
		MeetupDto dto = meetupMapper.selectMeetupDetail(meetupId);

		//System.out.println(dto + "??????????????????????????????????????");

		return dto;
	}
	
	//모집 - 신청
	@Override
	public int insertApplication(MeetupApplicationDto meetupApplicationsDto) {
		System.out.println(meetupApplicationsDto.getMemberId());
		System.out.println(meetupApplicationsDto.getMeetupId());
		MeetupApplicationDto find = this.findApplyInfo(meetupApplicationsDto);
		
		if(find != null) {
			return meetupMapper.updateApplication(find);	
		}else {
			return meetupMapper.insertApplication(meetupApplicationsDto);
		}
	}
	
	//모집 - 신청취소
	@Override
	public int cancelApplyMeetup(MeetupApplicationDto meetupApplicationDto) {
		return meetupMapper.cancelApplyMeetup(meetupApplicationDto);
	}
	
	//모집글수정
	@Override
	public int updateMeetup(MeetupDto meetupDto) {
		return meetupMapper.updateMeetup(meetupDto);
	}
	
	//마이페이지 내 모집글 조회 + paging
	@Override
	public List<MeetupDto> selectMyMeetup(int pstartno,MeetupDto meetupDto) {
		meetupDto.setEnd(10);
		meetupDto.setStart((pstartno-1)*10);		
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
	
	//마이페이지 내 신청글 조회 + paging
	@Override
	public List<MeetupDto> selectMyMeetupApply(int pstartno, MeetupDto meetupDto) {
		meetupDto.setEnd(10);
		meetupDto.setStart((pstartno-1)*10);
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
	public int changeMeetupApplyStatus(MeetupApplicationDto meetupApplicationDto) {
		return meetupMapper.changeMeetupApplyStatus(meetupApplicationDto);
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
	public int countMeetupLike(MeetupLikeDto meetupLikeDto) {
		return meetupMapper.countMeetupLike(meetupLikeDto);
	}

	@Override
	public int deleteMeetupLike(MeetupLikeDto meetupLikeDto) {
		return meetupMapper.deleteMeetupLike(meetupLikeDto);
	}

	@Override
	public MeetupLikeDto selectMeetupLike(MeetupLikeDto meetupLikeDto) {
		return meetupMapper.selectMeetupLike(meetupLikeDto);
	}
	
	// 시도
	@Override
	public List<SidoDto> findAllSido() {
		return meetupMapper.findAllSido();
	}
	
	//시군구
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

}
