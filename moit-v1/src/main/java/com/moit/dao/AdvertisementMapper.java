package com.moit.dao;

import java.util.List;

import com.moit.dto.AdvertisementDto;
import com.moit.dto.AdvertisementSearchDto;

@Mapper
public interface AdvertisementMapper {
	
    // 목록 + 검색 + 페이징
    public List<AdvertisementDto> searchByAdmin(
            AdvertisementSearchDto dto);

    public int selectAdvertisementTotalCnt(
            AdvertisementSearchDto dto);

    // 상세
    public AdvertisementDto selectAdvertisementOne(int adId);

    // 등록
    public int insertAdvertisement(
            AdvertisementDto dto);

    // 수정
    public int updateAdvertisement(
            AdvertisementDto dto);

    // 삭제   -  논리삭제
    public int deleteAdvertisement(AdvertisementDto dto);

    // 상태변경
    public int updateAdvertisementStatus(
            AdvertisementDto dto);

    // 클릭수 증가
    public int updateAdvertisementClick(int adId);
    
    // 통계
    public int selectTotalAdvertisementCnt();

    public int selectOpenAdvertisementCnt();

    public int selectPendingAdvertisementCnt();

    public int selectClosedAdvertisementCnt();

    ///////////////////// 

    // 전체 조회
    //public List<AdvertisementDto> selectAll();

    // 노출 수 증가
    public int updateImpressions(int adId);

    // 메인 노출용 (priority + 랜덤/정렬)
    //public List<AdvertisementDto> selectMainAds();

    //  조건 조회

    // 활성 광고 (기간 + 상태)
    //public List<AdvertisementDto> selectActiveAds();

    // 위치별 광고 (MAIN_TOP, SIDEBAR 등)
    //public List<AdvertisementDto> selectByPosition(String position);

    // 타겟 필터 (연령/성별/디바이스)
    //public List<AdvertisementDto> selectByTarget(HashMap<String, Object> map);

    public AdvertisementDto selectTopAdvertisement();
}