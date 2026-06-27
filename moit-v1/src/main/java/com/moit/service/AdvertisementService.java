package com.moit.service;

import java.util.List;

import com.moit.dto.AdvertisementDto;
import com.moit.dto.AdvertisementSearchDto;

public interface AdvertisementService {

    // 목록 + 검색 + 페이징
	public List<AdvertisementDto> searchByAdmin(AdvertisementSearchDto dto);

    // 전체 개수
	public int selectAdvertisementTotalCnt(AdvertisementSearchDto dto);

    // 상세
	public AdvertisementDto selectAdvertisementOne(int adId);

    // 등록
	public int insertAdvertisement(AdvertisementDto dto);

    // 수정
	public int updateAdvertisement(AdvertisementDto dto);

    // 삭제 (논리삭제)
	public int deleteAdvertisement(AdvertisementDto dto);

    // 상태 변경
	public int updateAdvertisementStatus(AdvertisementDto dto);

    // 클릭 수 증가
	public int updateAdvertisementClick(int adId);

    // 노출 수 증가
	public int updateImpressions(int adId);
	
	
	// 통계
	public int selectTotalAdvertisementCnt();

	public int selectOpenAdvertisementCnt();

	public int selectPendingAdvertisementCnt();

	public int selectClosedAdvertisementCnt();
	
	
	
	
	////////////////// 클릭+노출 테스트
	public AdvertisementDto selectTopAdvertisement();
}
