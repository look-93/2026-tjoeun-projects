package com.moit.advertisement.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.moit.advertisement.dao.AdvertisementMapper;
import com.moit.advertisement.dto.AdvertisementDto;
import com.moit.advertisement.dto.AdvertisementImageDto;
import com.moit.advertisement.dto.AdvertisementSearchDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdvertisementServiceImpl implements AdvertisementService {

	private final AdvertisementMapper advertisementMapper;
	private final MailService mailService;
	private static final String UPLOAD_PATH = "C:/upload/ad";

	// 스케쥴러
	@Override
	@Transactional
	public void updateAdvertisementStatus() {

		int openCnt = advertisementMapper.updatePendingToOpen();
		int closeCnt = advertisementMapper.updateOpenToClosed();

		System.out.println("OPEN 변경 : " + openCnt);
	    System.out.println("CLOSED 변경 : " + closeCnt);
	}
	
	@Override
	public int updatePriorityScore() {

	    return advertisementMapper.updatePriorityScore();

	}
	
	// 제휴사용자 목록
	@Override
	public List<AdvertisementDto> searchMyAdvertisement(AdvertisementSearchDto dto) {

		List<AdvertisementDto> list = advertisementMapper.searchMyAdvertisement(dto);

		for (AdvertisementDto ad : list) {
			ad.setImageList(advertisementMapper.selectAdvertisementImageList(ad.getAdId()));
		}

		return list;
	}

	// 제휴사용자 목록 개수
	@Override
	public int selectMyAdvertisementTotalCnt(AdvertisementSearchDto dto) {
		return advertisementMapper.selectMyAdvertisementTotalCnt(dto);
	}

	// 관리자 광고 목록
	@Override
	public List<AdvertisementDto> searchByAdmin(AdvertisementSearchDto dto) {

		List<AdvertisementDto> list = advertisementMapper.searchByAdmin(dto);

		for (AdvertisementDto ad : list) {
			ad.setImageList(advertisementMapper.selectAdvertisementImageList(ad.getAdId()));
		}

		return list;
	}

	// 관리자 광고 목록 개수
	@Override
	public int selectAdminAdvertisementTotalCnt(AdvertisementSearchDto dto) {
		return advertisementMapper.selectAdminAdvertisementTotalCnt(dto);
	}

	// 승인 대기 목록
	@Override
	public List<AdvertisementDto> searchWaitingList(AdvertisementSearchDto dto) {

		List<AdvertisementDto> list = advertisementMapper.searchWaitingList(dto);

		for (AdvertisementDto ad : list) {
			ad.setImageList(advertisementMapper.selectAdvertisementImageList(ad.getAdId()));
		}

		return list;
	}

	// 승인 대기 목록 개수
	@Override
	public int selectWaitingTotalCnt(AdvertisementSearchDto dto) {
		return advertisementMapper.selectWaitingTotalCnt(dto);
	}
	
	// 기간연장 승인 대기 목록
	@Override
	public List<AdvertisementDto> selectExtensionList(){
	    return advertisementMapper.selectExtensionList();
	}

	// 상세 조회
	@Override
	public AdvertisementDto selectAdvertisementOne(int adId) {

		AdvertisementDto dto = advertisementMapper.selectAdvertisementOne(adId);

		if (dto != null) {
			dto.setImageList(advertisementMapper.selectAdvertisementImageList(adId));
		}

		return dto;
	}

	// 광고 등록
	@Override
	public int insertAdvertisement(AdvertisementDto dto) {
		return advertisementMapper.insertAdvertisement(dto);
	}

	// 광고 수정
	@Override
	@Transactional
	public int updateAdvertisement(
	        AdvertisementDto dto,
	        List<MultipartFile> imageFiles,
	        List<String> imageTypes) {

	    // 광고 수정
	    advertisementMapper.updateAdvertisement(dto);

	    if (imageFiles == null || imageTypes == null) {
	        return 1;
	    }

	    boolean hasNewImage = imageFiles.stream()
	            .anyMatch(file -> file != null && !file.isEmpty());

	    if (!hasNewImage) {
	        return 1;
	    }

	    // 기존 이미지 조회
	    List<AdvertisementImageDto> oldImages =
	            advertisementMapper.selectAdvertisementImageList(dto.getAdId());

	    // 실제 파일 삭제
	    for (AdvertisementImageDto image : oldImages) {

	        if (image.getImageUrl() == null)
	            continue;

	        String fileName =
	                image.getImageUrl().replace("/upload/ad/", "");

	        File oldFile =
	                new File(UPLOAD_PATH, fileName);

	        if (oldFile.exists()) {
	            oldFile.delete();
	        }
	    }

	    // DB 삭제
	    advertisementMapper.deleteAdvertisementImages(dto.getAdId());

	    File dir = new File(UPLOAD_PATH);

	    if (!dir.exists()) {
	        dir.mkdirs();
	    }

	    for (int i = 0; i < imageFiles.size(); i++) {

	        MultipartFile file = imageFiles.get(i);

	        if (file == null || file.isEmpty()) {
	            continue;
	        }

	        String saveName =
	                UUID.randomUUID()
	                + "_"
	                + file.getOriginalFilename();

	        try {
	            file.transferTo(new File(dir, saveName));
	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }

	        AdvertisementImageDto imageDto =
	                new AdvertisementImageDto();

	        imageDto.setAdId(dto.getAdId());
	        imageDto.setImageType(imageTypes.get(i));
	        imageDto.setImageUrl("/upload/ad/" + saveName);

	        advertisementMapper.insertAdvertisementImage(imageDto);
	    }

	    return 1;
	}

	// 광고 삭제
	@Override
	@Transactional
	public int deleteAdvertisement(int adId) {

	    List<AdvertisementImageDto> imageList =
	            advertisementMapper.selectAdvertisementImageList(adId);

	    for (AdvertisementImageDto image : imageList) {

	        if (image.getImageUrl() == null) {
	            continue;
	        }

	        String fileName =
	                image.getImageUrl().replace("/upload/ad/", "");

	        File file =
	                new File(UPLOAD_PATH, fileName);

	        if (file.exists()) {
	            file.delete();
	        }
	    }

	    advertisementMapper.deleteAdvertisementImages(adId);

	    return advertisementMapper.deleteAdvertisement(adId);
	}

	// 상태 변경
	@Override
	public int updateAdvertisementStatus(AdvertisementDto dto) {
		return advertisementMapper.updateAdvertisementStatus(dto);
	}
	
	// 연장승인 상태 변경
	@Override
	@Transactional
	public void updateExtensionApprove(AdvertisementDto dto){
	    advertisementMapper.updateExtensionApprove(dto);
	}
	
	// 승인 설정
	@Override
	public int updateApprovalStatus(AdvertisementDto dto) {

	    if ("APPROVED".equals(dto.getApprovalStatus())) {

	    	 return advertisementMapper.approveAd(dto);

	    } else if ("REJECTED".equals(dto.getApprovalStatus())) {

	    	return advertisementMapper.rejectAd(dto);
	    }

	    throw new IllegalArgumentException("잘못된 상태값");
	}
	
	// 우선도 선택
	@Override
	public int updateAdGrade(
	        int adId,
	        String adGrade) {


	    return advertisementMapper.updateAdGrade(
	            adId,
	            adGrade
	    );

	}
	
	// 기간변경
	@Override
	public void updatePeriod(Long adId, LocalDateTime start, LocalDateTime end) {
	    advertisementMapper.updatePeriod(adId, start, end);
	}

	// 이미지 등록
	@Override
	public int insertAdvertisementImage(AdvertisementImageDto dto) {
		return advertisementMapper.insertAdvertisementImage(dto);
	}

	// 이미지 목록
	@Override
	public List<AdvertisementImageDto> selectAdvertisementImageList(int adId) {
		return advertisementMapper.selectAdvertisementImageList(adId);
	}

	// 이미지 삭제
	@Override
	public int deleteAdvertisementImage(int adId) {
		return advertisementMapper.deleteAdvertisementImages(adId);
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
	public AdvertisementDto selectTopAdvertisement( String position, Integer memberId, String sessionId) {
	    return advertisementMapper.selectTopAdvertisement( position, memberId, sessionId);
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
	
	// 클릭 로그 저장 
	@Override
	@Transactional
	public boolean insertClickLog(
	        int adId, String position,
	        HttpServletRequest request,
	        HttpSession session) {


		Integer loginMemberId =
	            (Integer) session.getAttribute("loginMemberId");

		String ip =
	            request.getRemoteAddr();
		
		String userAgent = request.getHeader("User-Agent");

		String deviceType = "PC";

		if(userAgent != null){
			String ua = userAgent.toLowerCase();
			
			if (ua.contains("ipad") || ua.contains("tablet")) {
		        deviceType = "TABLET";
		    } else if (ua.contains("mobile")
		            || ua.contains("android")
		            || ua.contains("iphone")) {
		        deviceType = "MOBILE";
		    }
		}

		
	    int count =
	        advertisementMapper.checkDuplicateClick( adId, loginMemberId, ip );


	    // 최근 1시간 클릭 기록 있으면 저장 X
	    if(count > 0){
	        return false;
	    }
	    System.out.println("deviceType = [" + deviceType + "]");
	    advertisementMapper.insertClickLog(
	            adId,
	            loginMemberId,
	            deviceType,
	            ip,
	            request.getSession().getId(),
	            request.getHeader("Referer"),
	            position
	    );
	    
	 // 로그인 회원이면 포인트 지급
	    if (loginMemberId != null) {

	        int pointCount =
	                advertisementMapper.checkAdvertisementPoint(loginMemberId);

	        if (pointCount == 0) {

	            advertisementMapper.updateMemberPoint(loginMemberId);

	            advertisementMapper.insertPointHistory(loginMemberId);
	        }
	    }
	    
	    return true;
	}
	
	// 노출 로그 저장 
	@Override
	@Transactional
	public boolean insertImpressionLog(
	        int adId, String position,
	        HttpServletRequest request,
	        HttpSession session) {


	    Integer memberId =
	        (Integer) session.getAttribute("loginMemberId");


	    String ip =
	        request.getRemoteAddr();


	    String sessionId =
	        request.getSession().getId();


	    String userAgent =
	        request.getHeader("User-Agent");


	    String deviceType = "PC";


	    if(userAgent != null){

	        String ua = userAgent.toLowerCase();

	        if(ua.contains("ipad")
	            || ua.contains("tablet")) {

	            deviceType = "TABLET";

	        } else if(ua.contains("mobile")
	                || ua.contains("android")
	                || ua.contains("iphone")) {

	            deviceType = "MOBILE";
	        }
	    }


	    int count =
	        advertisementMapper.checkDuplicateImpression(
	            adId,
	            memberId,
	            sessionId
	        );


	    if(count > 0){
	        return false;
	    }


	    advertisementMapper.insertImpressionLog(
	        adId,
	        memberId,
	        deviceType,
	        ip,
	        sessionId,
	        position
	    );


	    return true;
	}
	
	// 일일통계 
//	@Override
//	@Transactional
//	public void insertDailyStatistics(){
//
//	    advertisementMapper.insertDailyStatistics();
//
//	}

	@Override
	public void createDailyStatistics() {
		
	}
	
	
	@Override
	public void sendReminderMail() {

	    // 30일
	    List<AdvertisementDto> reminder30 =
	            advertisementMapper.selectReminder30List();

	    for (AdvertisementDto ad : reminder30) {

	        mailService.sendAdvertisementReminderMail(ad, 30);

	        advertisementMapper.updateReminder30Sent(ad.getAdId());

	    }

	    // 14일
	    List<AdvertisementDto> reminder14 =
	            advertisementMapper.selectReminder14List();

	    for (AdvertisementDto ad : reminder14) {

	        mailService.sendAdvertisementReminderMail(ad, 14);

	        advertisementMapper.updateReminder14Sent(ad.getAdId());

	    }

	}
	
}