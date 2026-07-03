package com.moit.advertisement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.moit.advertisement.dao.AdvertisementMapper;
import com.moit.advertisement.dto.AdvertisementDto;
import com.moit.advertisement.dto.AdvertisementSearchDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdvertisementServiceImpl implements AdvertisementService {

	private final AdvertisementMapper advertisementMapper;

	// 제휴사용자 목록
	@Override
	public List<AdvertisementDto> searchMyAdvertisement(AdvertisementSearchDto dto) {
	    return advertisementMapper.searchMyAdvertisement(dto);
	}

	// 제휴사용자 목록 개수
	@Override
	public int selectMyAdvertisementTotalCnt(AdvertisementSearchDto dto) {
	    return advertisementMapper.selectMyAdvertisementTotalCnt(dto);
	}

	// 관리자 목록
	@Override
	public List<AdvertisementDto> searchByAdmin(AdvertisementSearchDto dto) {
	    return advertisementMapper.searchByAdmin(dto);
	}

	// 관리자 목록 개수
	@Override
	public int selectAdminAdvertisementTotalCnt(AdvertisementSearchDto dto) {
	    return advertisementMapper.selectAdminAdvertisementTotalCnt(dto);
	}

	// 승인 목록
	@Override
	public List<AdvertisementDto> searchApprovalList(AdvertisementSearchDto dto) {
	    return advertisementMapper.searchApprovalList(dto);
	}

	// 승인 목록 개수
	@Override
	public int selectApprovalAdvertisementTotalCnt(AdvertisementSearchDto dto) {
	    return advertisementMapper.selectApprovalAdvertisementTotalCnt(dto);
	}

    // 상세 조회
    @Override
    public AdvertisementDto selectAdvertisementOne(int adId) {
        return advertisementMapper.selectAdvertisementOne(adId);
    }

    // 광고 등록
    @Override
    public int insertAdvertisement(AdvertisementDto dto) {
        return advertisementMapper.insertAdvertisement(dto);
    }

    // 광고 수정
    @Override
    public int updateAdvertisement(AdvertisementDto dto) {
        return advertisementMapper.updateAdvertisement(dto);
    }

    // 광고 삭제
    @Override
    public int deleteAdvertisement(int adId) {
        return advertisementMapper.deleteAdvertisement(adId);
    }
    
    // 승인 상태 변경
	@Override
	public int updateApprovalStatus(AdvertisementDto dto) {
		return advertisementMapper.updateApprovalStatus(dto);
	}

    // 상태 변경
    @Override
    public int updateAdvertisementStatus(AdvertisementDto dto) {
        return advertisementMapper.updateAdvertisementStatus(dto);
    }

    // 노출 수 증가
    @Override
    public int updateImpressions(int adId) {
        return advertisementMapper.updateImpressions(adId);
    }

    // 클릭 수 증가
    @Override
    public int updateAdvertisementClick(int adId) {
        return advertisementMapper.updateAdvertisementClick(adId);
    }

    // 메인 광고 조회
    @Override
    public AdvertisementDto selectTopAdvertisement() {
        return advertisementMapper.selectTopAdvertisement();
    }

    // 통계
    @Override
    public int selectTotalAdvertisementCnt() {
        return advertisementMapper.selectTotalAdvertisementCnt();
    }

    @Override
    public int selectOpenAdvertisementCnt() {
        return advertisementMapper.selectOpenAdvertisementCnt();
    }

    @Override
    public int selectPendingAdvertisementCnt() {
        return advertisementMapper.selectPendingAdvertisementCnt();
    }

    @Override
    public int selectClosedAdvertisementCnt() {
        return advertisementMapper.selectClosedAdvertisementCnt();
    }


}