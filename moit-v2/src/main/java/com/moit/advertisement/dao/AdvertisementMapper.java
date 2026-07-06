package com.moit.advertisement.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.moit.advertisement.dto.AdvertisementDto;
import com.moit.advertisement.dto.AdvertisementImageDto;
import com.moit.advertisement.dto.AdvertisementSearchDto;

@Mapper
public interface AdvertisementMapper {

	// 제휴사용자 목록
	List<AdvertisementDto> searchMyAdvertisement(AdvertisementSearchDto dto);

	int selectMyAdvertisementTotalCnt(AdvertisementSearchDto dto);

	// 관리자 목록
	List<AdvertisementDto> searchByAdmin(AdvertisementSearchDto dto);

	int selectAdminAdvertisementTotalCnt(AdvertisementSearchDto dto);

	// 승인 목록
	List<AdvertisementDto> searchWaitingList(AdvertisementSearchDto dto);

	int selectWaitingTotalCnt(AdvertisementSearchDto dto);

    // 상세 조회
    AdvertisementDto selectAdvertisementOne(int adId);

    // 등록
    int insertAdvertisement(AdvertisementDto dto);

    // 수정
    int updateAdvertisement(AdvertisementDto dto);

    // 논리 삭제 (adId만 사용)
    int deleteAdvertisement(int adId);

    // 승인 상태 변경
    int updateApprovalStatus(AdvertisementDto dto);
    
    // 상태 변경
    int updateAdvertisementStatus(AdvertisementDto dto);
    
    // 이미지 등록
    int insertAdvertisementImage(AdvertisementImageDto dto);

    // 이미지 조회
    List<AdvertisementImageDto> selectAdvertisementImageList(int adId);

    // 이미지 삭제
    int deleteAdvertisementImage(int adId);

    // 노출 증가
    int updateImpressions(int adId);
    // 클릭 증가
    int updateAdvertisementClick(int adId);

    // 사용자 광고 1건
    AdvertisementDto selectTopAdvertisement();

    // 통계
    int selectTotalAdvertisementCnt();

    int selectOpenAdvertisementCnt();

    int selectPendingAdvertisementCnt();

    int selectClosedAdvertisementCnt();
}