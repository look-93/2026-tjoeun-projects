package com.moit.advertisement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.moit.advertisement.dto.AdvertisementChartDto;
import com.moit.advertisement.dto.AdvertisementDto;
import com.moit.advertisement.dto.AdvertisementImageDto;
import com.moit.advertisement.dto.AdvertisementSearchDto;
import com.moit.advertisement.dto.DashboardAiDto;
import com.moit.advertisement.dto.ExtensionRequestDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public interface AdvertisementService {

	// 스케쥴러
	void updateAdvertisementStatus();
	
	int updatePriorityScore();
	
	// 제휴사용자 목록
	List<AdvertisementDto> searchMyAdvertisement(AdvertisementSearchDto dto);

	// 제휴사용자 목록 개수
	int selectMyAdvertisementTotalCnt(AdvertisementSearchDto dto);

	// 관리자 목록
	List<AdvertisementDto> searchByAdmin(AdvertisementSearchDto dto);

	// 관리자 목록 개수
	int selectAdminAdvertisementTotalCnt(AdvertisementSearchDto dto);

	// 승인 목록
	List<AdvertisementDto> searchWaitingList(AdvertisementSearchDto dto);

	// 승인 목록 개수
	int selectWaitingTotalCnt(AdvertisementSearchDto dto);

	// 기간연장 승인 대기 목록
	List<AdvertisementDto> selectExtensionList();
	
    // 상세 조회
    AdvertisementDto selectAdvertisementOne(int adId);

    // 광고 등록
    int insertAdvertisement(AdvertisementDto dto);

    // 광고 수정
    int updateAdvertisement(
            AdvertisementDto dto,
            List<MultipartFile> imageFiles,
            List<String> imageTypes);

    // 광고 삭제
    int deleteAdvertisement(int adId);

    // 승인
    int updateApprovalStatus(AdvertisementDto dto);
    
    // 상태 변경
    int updateAdvertisementStatus(AdvertisementDto dto);
    
 // 연장승인 상태 변경
    void updateExtensionApprove( AdvertisementDto dto );

    // 우선도 설정
	int updateAdGrade(int adId, String adGrade);
	
	// 기간 변경
    void updatePeriod(Long adId, LocalDateTime start, LocalDateTime end);
    
    // 이미지 등록
    int insertAdvertisementImage(AdvertisementImageDto dto);

    // 이미지 전체 조회
    List<AdvertisementImageDto> selectAdvertisementImageList(int adId);

    // 이미지 삭제
    int deleteAdvertisementImage(int adId);

    // 노출 수 증가
    int updateImpressions(int adId);

    // 클릭 수 증가
    int updateAdvertisementClick(int adId);

    // 광고 조회
    AdvertisementDto selectTopAdvertisement(String position, Integer memberId, String sessionId);

    // 통계
    int selectTotalAdvertisementCnt();

    int selectOpenAdvertisementCnt();

    int selectPendingAdvertisementCnt();

    int selectClosedAdvertisementCnt();

    // 클릭 로그
	boolean insertClickLog(int adId, String position, HttpServletRequest request, HttpSession session);
	
	// 노출 로그
	boolean insertImpressionLog(int adId, String position, HttpServletRequest request, HttpSession session);

	// 일일통계
	void insertDailyStatistics();
	
	// 통계 차트
	// 총 통계
	AdvertisementChartDto selectSummary();
	// 7일치 통계차트
	List<AdvertisementChartDto> selectDailyChart();
	// ctr 탑5
	List<AdvertisementChartDto> selectTopCtrChart();
	// 등급비율
	List<AdvertisementChartDto> selectGradeChart();
	// 위치별 노출
	List<AdvertisementChartDto> selectPositionChart(); 
	// 연장률
	double selectExtensionRate();	
	// 위치별 ctr 차트
	List<AdvertisementChartDto> selectPositionCtrChart();
	// AI 통계 요약
	DashboardAiDto getDashboardAiData();
	DashboardAiDto getLatestAiSummary(); 
    void saveAiSummary(String summary); 
	
	
	// 피로도
	AdvertisementDto getAdvertisementStatistics(int adId);

	// 메일 발송
	void sendReminderMail();

	void requestExtension(ExtensionRequestDto dto);

}