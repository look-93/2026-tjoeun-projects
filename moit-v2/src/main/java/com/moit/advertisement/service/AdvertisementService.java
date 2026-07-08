package com.moit.advertisement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.moit.advertisement.dto.AdvertisementDto;
import com.moit.advertisement.dto.AdvertisementImageDto;
import com.moit.advertisement.dto.AdvertisementSearchDto;

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

    // 메인 광고 조회
    AdvertisementDto selectTopAdvertisement(String position);

    // 통계
    int selectTotalAdvertisementCnt();

    int selectOpenAdvertisementCnt();

    int selectPendingAdvertisementCnt();

    int selectClosedAdvertisementCnt();




}