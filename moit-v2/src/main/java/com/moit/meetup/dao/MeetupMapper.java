package com.moit.meetup.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.moit.meetup.dto.AdminMeetupStatusSummaryDto;
import com.moit.meetup.dto.MeetupApplicationDto;
import com.moit.meetup.dto.MeetupDto;
import com.moit.meetup.dto.MeetupImageDto;
import com.moit.meetup.dto.MeetupLikeDto;
import com.moit.meetup.dto.MeetupSearchDto;
import com.moit.meetup.dto.MeetupWeatherNotificationDto;
import com.moit.meetup.dto.TrustScoreDto;
import com.moit.meetup.dto.common.CategoryDto;
import com.moit.meetup.dto.common.SidoDto;
import com.moit.meetup.dto.common.SigunguDto;
import com.moit.meetup.dto.openapi.RecommendMeetupResponseDto;

@Mapper
public interface MeetupMapper {
	
	//관리자,사용자 - 목록 조회 + paging
	public List<MeetupDto> findAllMeetupBy(MeetupSearchDto meetupSearchDto);
	public int findAllMeetupCountBy(MeetupSearchDto meetupSearchDto);
	//관리자 상단 통계
	public AdminMeetupStatusSummaryDto findAdminMeetupStatusSummary();	
	//관리자 - 모임 리스트 삭제
	public int updateMeetupDeleteYn(int meetupId);	
	
	//모임 상세조회
	public MeetupDto selectMeetupDetail(int meetupId);
	
	//모임 상세조회 - 이미지 조회
	public List<MeetupImageDto> findMeetupImage(int MeetupId);	
	
	//모임 신청정보
	public MeetupApplicationDto findApplyInfo(MeetupApplicationDto meetupApplicationDto);
	
	//모임 신청
	public int insertApplication(MeetupApplicationDto meetupApplicationDto);
	
	//모임 신청 취소
	public int cancelApplyMeetup(MeetupApplicationDto meetupApplicationDto);
	
	//신청 있으면 업데이트
	public int updateApplication(MeetupApplicationDto meetupApplicationDto);
	
	//모집글등록
	public int insertMeetup(MeetupDto meetupDto);
	
	//모집글 등록 - images 파일 경로 저장
	public int insertImages(List<MeetupImageDto> list);
	
	//모집글 등록 - meetup_images 이미지 저장
	public int insertMeetupImages(Map<String, Object> map);
	
	//현재 시퀀스의 가장 마지막 값(CURRVAL)을 매퍼에서 가져오기
	public int getLastImageSequence();
	
	/* 이미지 삭제 */
	public int deleteMeetupImages(int meetupId);	
	public int deleteImages(List<MeetupImageDto> list);
	/* 이미지 삭제 */
	
	//모집글수정
	public int updateMeetup(MeetupDto meetupDto);
	
	//인기 모임 조회
	public List<MeetupDto>findPopularMeetup();
	
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
	
	//노쇼방지 계산식
	public TrustScoreDto calculatedScore(int memberId);
	public int updateAiSummaryAndTrustScore(TrustScoreDto trustScoreDto);
	
	//날씨
	public int insertNotification(MeetupWeatherNotificationDto dto);
	public List<MeetupDto> selectMeetupsBeforeTwoHours();
	
	//많이 참여한 카테고리 참여 횟수
	public List<MeetupDto> selectRecommendMeetupCount(int memberId);
	//가장 많이 참여한 부모 카테고리의 모집 중 모임 추천 리스트 조회
	public MeetupDto selectRecommendMeetups(MeetupDto meetupDto);
	
	//카테고리명으로 카테고리 아이디 찾기
	public int selectCategoryId(String category);
	
	public MeetupDto findById(int meetupId);
}