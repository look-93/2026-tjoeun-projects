package com.moit.advertisement.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.moit.advertisement.dto.AdvertisementChartDto;
import com.moit.advertisement.dto.AdvertisementDto;
import com.moit.advertisement.dto.AdvertisementImageDto;
import com.moit.advertisement.dto.AdvertisementPaymentDto;
import com.moit.advertisement.dto.AdvertisementSearchDto;
import com.moit.advertisement.entity.Advertisement;
import com.moit.advertisement.entity.AdvertisementClickLog;
import com.moit.advertisement.entity.AdvertisementImage;
import com.moit.advertisement.entity.AdvertisementImpressionLog;
import com.moit.advertisement.entity.AdvertisementPayment;
import com.moit.advertisement.enums.AdGrade;
import com.moit.advertisement.enums.AdPosition;
import com.moit.advertisement.enums.AdStatus;
import com.moit.advertisement.enums.ApprovalStatus;
import com.moit.advertisement.repository.AdvertisementClickLogRepository;
import com.moit.advertisement.repository.AdvertisementImageRepository;
import com.moit.advertisement.repository.AdvertisementImpressionLogRepository;
import com.moit.advertisement.repository.AdvertisementPaymentRepository;
import com.moit.advertisement.repository.AdvertisementPositionPriceRepository;
import com.moit.advertisement.repository.AdvertisementRepository;
import com.moit.member.entity.Member;
import com.moit.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementImageRepository advertisementImageRepository;
    private final MemberRepository memberRepository;
    
    private final AdvertisementClickLogRepository clickLogRepository;
    private final AdvertisementImpressionLogRepository impressionLogRepository;
    private final AdvertisementPaymentRepository advertisementPaymentRepository;

    private final MailService mailService;
    private final AiSummaryService aiSummaryService;

    private static final String UPLOAD_PATH = "C:/upload/ad";

    // =========================================================
    // 관리자 탭별 전용 구현 메서드
    // =========================================================

    @Override
    public List<AdvertisementDto> searchApprovalTabList(AdvertisementSearchDto dto) {
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize());
        return advertisementRepository.findApprovalTabList(pageable)
                .getContent().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public Long selectApprovalTabTotalCnt(AdvertisementSearchDto dto) {
        return advertisementRepository.countApprovalTabList();
    }
    
    @Override
    public List<AdvertisementPaymentDto> searchPaymentTabList(
            AdvertisementSearchDto dto) {

        Pageable pageable =
                PageRequest.of(
                        dto.getPage() - 1,
                        dto.getSize()
                );

        return advertisementPaymentRepository
                .findAllByOrderByCreatedAtDesc(pageable)
                .getContent()
                .stream()
                .map(this::toPaymentDto)
                .toList();
    }

    @Override
    public long selectPaymentTabTotalCnt(AdvertisementSearchDto dto) {
        return advertisementPaymentRepository.count();
    }

    @Override
    public List<AdvertisementDto> searchStatusTabList(AdvertisementSearchDto dto) {
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize());
        return advertisementRepository.findStatusTabList(pageable)
                .getContent().stream()
                .map(this::toDto)
                .toList();
    }

    public long selectStatusTabTotalCnt(AdvertisementSearchDto dto) {
        return advertisementRepository.countStatusTabList();
    }
    
    // =========================================================
    // 결제내역 목록
    // =========================================================
    @Override
    public List<AdvertisementPaymentDto> searchPaymentHistory(
            AdvertisementSearchDto dto) {

        Pageable pageable =
                PageRequest.of(
                        dto.getPage() - 1,
                        dto.getSize()
                );

        return advertisementPaymentRepository
                .findAllByOrderByCreatedAtDesc(pageable)
                .getContent()
                .stream()
                .map(this::toPaymentDto)
                .toList();
    }
    
    // =========================================================
    // 광고 목록 ( 사용자 )
    // =========================================================

    @Override
    public List<AdvertisementDto> searchMyAdvertisement(
            AdvertisementSearchDto dto) {

        Long advertiserId = dto.getAdvertiserId();

        if (advertiserId == null) {
            return List.of();
        }

        List<Advertisement> advertisements =
                advertisementRepository.findByAdvertiser_IdAndDeleteYn(advertiserId, 'N');

        return advertisements.stream()
                .map(this::toDto)
                .toList();
    }


    @Override
    public int selectMyAdvertisementTotalCnt(
            AdvertisementSearchDto dto) {

        Long advertiserId = dto.getAdvertiserId();

        if (advertiserId == null) {
            return 0;
        }

        return (int) advertisementRepository.countByAdvertiser_IdAndDeleteYn(advertiserId, 'N');
    }


    // =========================================================
    // 관리자 광고 목록
    // =========================================================

    @Override
    public List<AdvertisementDto> searchByAdmin(
            AdvertisementSearchDto dto) {

        List<Advertisement> advertisements;

        if (dto.getApprovalStatus() == null) {
            advertisements =
                    advertisementRepository.findByDeleteYn('N');
        } else {
            advertisements =
                    advertisementRepository
                            .findByDeleteYnAndApprovalStatus(
                                    'N',
                                    dto.getApprovalStatus()
                            );
        }

        return advertisements.stream()
                .map(this::toDto)
                .toList();
    }


    @Override
    public int selectAdminAdvertisementTotalCnt(
            AdvertisementSearchDto dto) {

        if (dto.getApprovalStatus() == null) {
            return (int)
                    advertisementRepository.countByDeleteYn('N');
        }

        return (int)
                advertisementRepository
                        .countByDeleteYnAndApprovalStatus(
                                'N',
                                dto.getApprovalStatus()
                        );
    }


    // =========================================================
    // 승인 대기 목록
    // =========================================================

    @Override
    public List<AdvertisementDto> searchWaitingList(
            AdvertisementSearchDto dto) {

        return advertisementRepository
        		.findByApprovalStatus(ApprovalStatus.WAITING)
                .stream()
                .filter(ad -> ad.getDeleteYn() == 'N')
                .map(this::toDto)
                .toList();
    }


    @Override
    public int selectWaitingTotalCnt(
            AdvertisementSearchDto dto) {

        return advertisementRepository
                .findByApprovalStatus(
                        ApprovalStatus.WAITING
                )
                .size();
    }


    // =========================================================
    // 광고 상세
    // =========================================================

    @Override
    public AdvertisementDto selectAdvertisementOne(
            Long adId) {

        Advertisement advertisement =
                advertisementRepository
                        .findByAdIdAndDeleteYn(adId, 'N')
                        .orElse(null);

        if (advertisement == null) {
            return null;
        }

        return toDto(advertisement);
    }


    // =========================================================
    // 광고 등록
    // =========================================================

    @Override
    @Transactional
    public Long insertAdvertisement(
    		AdvertisementDto.AdvertisementRequestDto dto,
            Long advertiserId) {

        Member advertiser =
                memberRepository.findById(advertiserId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "광고주 회원을 찾을 수 없습니다."
                                )
                        );

    	Advertisement advertisement =
    	        Advertisement.builder()
    	                .advertiser(advertiser)
    	                .title(dto.getTitle())
    	                .content(dto.getContent())
    	                .landingUrl(dto.getLandingUrl())
    	                .targetAgeMin(dto.getTargetAgeMin())
    	                .targetAgeMax(dto.getTargetAgeMax())
    	                .targetGender(dto.getTargetGender())
    	                .startDatetime(dto.getStartDatetime())
    	                .endDatetime(dto.getEndDatetime())
    	                .totalBudget(dto.getTotalBudget())
    	                .build();

        advertisementRepository.save(advertisement);

        return advertisement.getAdId();
    }
    
	 // =========================================================
	 // 광고 조회
	 // =========================================================
	
    @Override
    public AdvertisementDto selectTopAdvertisement(String position) {

        AdPosition adPosition = AdPosition.valueOf(position);

        List<Advertisement> advertisements =
                advertisementRepository.findAvailableAdvertisements(
                        adPosition,
                        PageRequest.of(0, 1)
                );

        if (advertisements.isEmpty()) {
            return null;
        }

        return toDto(advertisements.get(0));
    }


    // =========================================================
    // 광고 수정
    // =========================================================

    @Override
    @Transactional
    public int updateAdvertisement(
            Long adId,
            Long memberId,
            AdvertisementDto.AdvertisementUpdateRequestDto dto,
            List<MultipartFile> imageFiles,
            List<String> imageTypes) {

        Advertisement advertisement =
                advertisementRepository
                        .findById(adId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "광고를 찾을 수 없습니다."
                                )
                        );

        /*
         * Entity 내부 update 메서드를 이용한다.
         * Service에서 필드를 직접 변경하지 않는다.
         */
        advertisement.updateAdvertisement(
                dto.getTitle(),
                dto.getContent(),
                dto.getLandingUrl(),
                dto.getTargetAgeMin(),
                dto.getTargetAgeMax(),
                dto.getTargetGender(),
                dto.getStartDatetime(),
                dto.getEndDatetime(),
                dto.getTotalBudget()
        );


        // -----------------------------------------------------
        // 새 이미지가 있는 경우
        // -----------------------------------------------------

        boolean hasNewImage =
                imageFiles != null
                        && imageFiles.stream()
                        .anyMatch(file ->
                                file != null
                                        && !file.isEmpty()
                        );

        if (!hasNewImage) {
            return 1;
        }


        // 기존 이미지 삭제
        List<AdvertisementImage> oldImages =
                advertisementImageRepository
                        .findByAdvertisement_AdId( adId );

        deletePhysicalFiles(oldImages);

        advertisementImageRepository.deleteAll(oldImages);


        // 새 이미지 저장
        File directory =
                new File(UPLOAD_PATH);

        if (!directory.exists()) {
            directory.mkdirs();
        }


        for (int i = 0;
             i < imageFiles.size();
             i++) {

            MultipartFile file =
                    imageFiles.get(i);

            if (file == null || file.isEmpty()) {
                continue;
            }

            String originalName =
                    file.getOriginalFilename();

            String saveName =
                    UUID.randomUUID()
                            + "_"
                            + originalName;

            try {

                file.transferTo(
                        new File(
                                directory,
                                saveName
                        )
                );

            } catch (IOException e) {

                throw new RuntimeException(
                        "광고 이미지 저장 실패",
                        e
                );
            }


            AdvertisementImage image =
                    AdvertisementImage.builder()
                            .advertisement(advertisement)
                            .imageType(
                                    AdPosition.valueOf(
                                            imageTypes.get(i)
                                    )
                            )
                            .imageUrl(
                                    "/upload/ad/"
                                            + saveName
                            )
                            .build();

            advertisementImageRepository.save(image);
        }

        return 1;
    }


    // =========================================================
    // 광고 삭제
    // =========================================================

    @Override
    @Transactional
    public int deleteAdvertisement(
            Long adId) {

        Advertisement advertisement =
                advertisementRepository
                        .findById(adId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "광고를 찾을 수 없습니다."
                                )
                        );


        // 이미지 조회
        List<AdvertisementImage> images =
                advertisementImageRepository
                        .findByAdvertisement_AdId(adId);


        // 실제 파일 삭제
        deletePhysicalFiles(images);


        // 이미지 DB 삭제
        advertisementImageRepository
                .deleteAll(images);


        /*
         * 현재 Entity에는 deleteYn을 사용하고 있으므로
         * 실제 DELETE보다는 논리 삭제를 사용하는 것이 안전하다.
         */
        advertisement.delete();


        return 1;
    }


    // =========================================================
    // 광고 승인
    // =========================================================

    @Override
    @Transactional
    public int updateApprovalStatus(
            AdvertisementDto.AdvertisementAdminUpdateDto dto) {

        Advertisement advertisement =
                advertisementRepository
                        .findById(dto.getAdId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "광고를 찾을 수 없습니다."
                                )
                        );


        if ("APPROVED".equals(dto.getApprovalStatus())) {

            Member admin =
                    memberRepository
                            .findById(dto.getApprovedBy())
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "관리자를 찾을 수 없습니다."
                                    )
                            );

            advertisement.approve(admin);

            return 1;
        }


        if ("REJECTED".equals(
                dto.getApprovalStatus())) {

            advertisement.reject(
                    dto.getRejectReason()
            );

            return 1;
        }


        throw new IllegalArgumentException(
                "잘못된 승인 상태값입니다."
        );
    }


    // =========================================================
    // 광고 상태 변경
    // =========================================================

    @Override
    @Transactional
    public int updateAdvertisementStatus(
            AdvertisementDto.AdvertisementAdminUpdateDto dto) {

        Advertisement advertisement =
                advertisementRepository
                        .findById(dto.getAdId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "광고를 찾을 수 없습니다."
                                )
                        );

        AdStatus status =
                AdStatus.valueOf(
                        dto.getStatus()
                );

        advertisement.changeStatus(status);

        return 1;
    }


    // =========================================================
    // 광고 등급 변경
    // =========================================================

    @Override
    @Transactional
    public int updateAdGrade(
            Long adId,
            String adGrade) {

        Advertisement advertisement =
                advertisementRepository
                        .findById(adId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "광고를 찾을 수 없습니다."
                                )
                        );

        advertisement.changeGrade(
                AdGrade.valueOf(adGrade)
        );

        return 1;
    }


    // =========================================================
    // 광고 기간 변경
    // =========================================================

    @Override
    @Transactional
    public void updatePeriod(
            Long adId,
            LocalDateTime start,
            LocalDateTime end) {

        Advertisement advertisement =
                advertisementRepository
                        .findById(adId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "광고를 찾을 수 없습니다."
                                )
                        );

        advertisement.changePeriod(
                start,
                end
        );
    }


    // =========================================================
    // 이미지
    // =========================================================

    @Override
    @Transactional
    public int insertAdvertisementImage(
            AdvertisementImageDto dto) {

        Advertisement advertisement =
                advertisementRepository
                        .findById(dto.getAdId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "광고를 찾을 수 없습니다."
                                )
                        );


        AdvertisementImage image =
                AdvertisementImage.builder()
                        .advertisement(advertisement)
                        .imageType(
                                AdPosition.valueOf(
                                        dto.getImageType()
                                )
                        )
                        .imageUrl(dto.getImageUrl())
                        .build();


        advertisementImageRepository.save(image);

        return 1;
    }


    @Override
    public List<AdvertisementImageDto>
            selectAdvertisementImageList(
                    Long adId) {

        return advertisementImageRepository
                .findByAdvertisement_AdId(adId)
                .stream()
                .map(this::toImageDto)
                .toList();
    }


    @Override
    @Transactional
    public int deleteAdvertisementImage(
            Long adId) {

        List<AdvertisementImage> images =
                advertisementImageRepository
                        .findByAdvertisement_AdId(adId);

        deletePhysicalFiles(images);

        advertisementImageRepository
                .deleteAll(images);

        return images.size();
    }


    // =========================================================
    // 광고 노출 / 클릭
    // =========================================================

    @Override
    @Transactional
    public int updateImpressions(
            Long adId) {

        int result =
                advertisementRepository
                        .increaseImpressions(adId);

        if (result == 0) {

            throw new IllegalArgumentException(
                    "광고를 찾을 수 없습니다."
            );
        }

        return result;
    }


    @Override
    @Transactional
    public int updateAdvertisementClick(
            Long adId) {

        int result =
                advertisementRepository
                        .increaseClicks(adId);

        if (result == 0) {

            throw new IllegalArgumentException(
                    "광고를 찾을 수 없습니다."
            );
        }

        return result;
    }
    
    // 클릭 & 노툴 로그
	    
	 // =========================================================
	 // 클릭 로그
	 // =========================================================
	
    @Override
    @Transactional
    public boolean insertClickLog(
            Long adId,
            String position,
            Long memberId,
            String ip,
            String userAgent) {

        Advertisement advertisement =
                advertisementRepository.findById(adId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "광고를 찾을 수 없습니다."
                                )
                        );

        Member member = null;

        if (memberId != null) {
            member = memberRepository.findById(memberId)
                    .orElse(null);
        }

        AdPosition adPosition =
                AdPosition.valueOf(position);

        AdvertisementClickLog clickLog =
                AdvertisementClickLog.builder()
                        .advertisement(advertisement)
                        .member(member)
                        .deviceType(getDeviceType(userAgent))
                        .ipAddress(ip)
                        .position(adPosition)
                        .build();

        clickLogRepository.save(clickLog);

        return true;
    }
    
    private String getDeviceType(String userAgent) {

        if (userAgent == null) {
            return "UNKNOWN";
        }

        String ua = userAgent.toLowerCase();

        if (ua.contains("mobile")) {
            return "MOBILE";
        }

        if (ua.contains("tablet")
                || ua.contains("ipad")) {
            return "TABLET";
        }

        return "PC";
    }
	 
	// =========================================================
	// 노출 로그
	// =========================================================

	@Override
	@Transactional
	public boolean insertImpressionLog(
	        Long adId,
	        String position,
	        Long memberId,
	        String ip,
	        String userAgent) {

	    // 노출 로그 저장 로직
	    // TODO: AdvertisementImpressionLog Entity + Repository 연결
		Advertisement advertisement =
                advertisementRepository.findById(adId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "광고를 찾을 수 없습니다."
                                )
                        );

        Member member = null;

        if (memberId != null) {
            member = memberRepository.findById(memberId)
                    .orElse(null);
        }

        AdPosition adPosition =
                AdPosition.valueOf(position);
		
		AdvertisementImpressionLog impressionLog =
		        AdvertisementImpressionLog.builder()
		                .advertisement(advertisement)
		                .member(member)
		                .deviceType(getDeviceType(userAgent))
		                .ipAddress(ip)
		                .position(adPosition)
		                .build();

		impressionLogRepository.save(impressionLog);

	    return true;
	}


    // =========================================================
    // 기본 통계
    // =========================================================

    @Override
    public int selectTotalAdvertisementCnt() {

        return (int)
                advertisementRepository.count();
    }


    @Override
    public int selectOpenAdvertisementCnt() {
        return (int) advertisementRepository
                .countByStatus(AdStatus.OPEN);
    }


    @Override
    public int selectPendingAdvertisementCnt() {

        return (int) advertisementRepository
                .countByStatus(AdStatus.PENDING);
    }


    @Override
    public int selectClosedAdvertisementCnt() {

        return (int) advertisementRepository
                .countByStatus(AdStatus.CLOSED);
    }
    
    
	 // =========================================================
	 // 통계 차트
	 // =========================================================
	
	 @Override
	 public AdvertisementChartDto selectSummary() {
	
	     AdvertisementChartDto dto = new AdvertisementChartDto();
	
	     dto.setTotalAd(selectTotalAdvertisementCnt());
	
	     // TODO Repository에서 전체 노출/클릭 조회
	     // dto.setTotalImp(...);
	     // dto.setTotalClick(...);
	     // dto.setAvgCtr(...);
	
	     return dto;
	 }
	
	 @Override
	 public List<AdvertisementChartDto> selectDailyChart() {
	
	     // TODO 일일통계 Repository 조회
	     return List.of();
	 }
	
	 @Override
	 public List<AdvertisementChartDto> selectTopCtrChart() {
	
	     // TODO 광고별 CTR 계산 후 상위 5개 조회
	     return List.of();
	 }
	
	 @Override
	 public List<AdvertisementChartDto> selectGradeChart() {
	
	     // TODO AdGrade별 광고 개수 조회
	     return List.of();
	 }
	
	 @Override
	 public List<AdvertisementChartDto> selectPositionChart() {
	
	     // TODO 광고 위치별 노출 조회
	     return List.of();
	 }
	
	 @Override
	 public double selectExtensionRate() {
	
	     // TODO 연장 광고 / 전체 광고
	     return 0.0;
	 }
	
	 @Override
	 public List<AdvertisementChartDto> selectPositionCtrChart() {
	
	     // TODO 위치별 CTR 계산
	     return List.of();
	 }


    // =========================================================
    // DTO 변환
    // =========================================================

    private AdvertisementDto toDto( Advertisement ad) {

        AdvertisementDto dto =
                new AdvertisementDto();

        dto.setAdId(ad.getAdId());

        dto.setTitle(ad.getTitle());
        dto.setContent(ad.getContent());
        dto.setLandingUrl(ad.getLandingUrl());

        dto.setTargetAgeMin(
                ad.getTargetAgeMin()
        );

        dto.setTargetAgeMax(
                ad.getTargetAgeMax()
        );

        dto.setTargetGender(
                ad.getTargetGender()
        );

        dto.setStartDatetime(
                ad.getStartDatetime()
        );

        dto.setEndDatetime(
                ad.getEndDatetime()
        );

        dto.setStatus(
                ad.getStatus()
        );

        dto.setApprovalStatus(
                ad.getApprovalStatus()
        );

        dto.setPaymentStatus(
                ad.getPaymentStatus()
        );

        dto.setAdGrade(
                ad.getAdGrade()
        );

        if (ad.getAdvertiser() != null) {
            dto.setAdvertiserId(ad.getAdvertiser().getId()); 
            
            // Member 엔티티에 있는 닉네임
            dto.setAdvertiserNickname(ad.getAdvertiser().getNickname()); 
        }

        dto.setImpressions(
                ad.getImpressions()
        );

        dto.setClicks(
                ad.getClicks()
        );

        dto.setPriorityScore(
                ad.getPriorityScore()
        );

        dto.setTotalBudget(
                ad.getTotalBudget()
        );

        dto.setFatigueScore(
                ad.getFatigueScore()
        );

        dto.setReminder30dSent(
                ad.getReminder30dSent()
        );

        dto.setReminder14dSent(
                ad.getReminder14dSent()
        );

        dto.setDeleteYn(
                ad.getDeleteYn()
        );

        dto.setCreatedAt(
                ad.getCreatedAt()
        );

        dto.setUpdatedAt(
                ad.getUpdatedAt()
        );

        dto.setImageList(
                selectAdvertisementImageList(
                        ad.getAdId()
                )
        );

        return dto;
    }


    private AdvertisementImageDto toImageDto(
            AdvertisementImage image) {

        AdvertisementImageDto dto =
                new AdvertisementImageDto();

        dto.setImageId(
                image.getImageId()
        );

        dto.setAdId(
                image.getAdvertisement()
                        .getAdId()
        );

        dto.setImageType(
                image.getImageType()
                        .name()
        );

        dto.setImageUrl(
                image.getImageUrl()
        );

        return dto;
    }
    
    private AdvertisementPaymentDto toPaymentDto(
            AdvertisementPayment payment) {

        AdvertisementPaymentDto dto =
                new AdvertisementPaymentDto();

        dto.setPaymentId(
                payment.getPaymentId()
        );

        dto.setAdId(
                payment.getAdvertisement().getAdId()
        );

        dto.setAdTitle(
                payment.getAdvertisement().getTitle()
        );

        dto.setAdvertiserId(
                payment.getAdvertiser().getId()
        );

        dto.setPaymentType(
                payment.getPaymentType()
        );

        dto.setOrderId(
                payment.getOrderId()
        );

        dto.setPaymentKey(
                payment.getPaymentKey()
        );

        dto.setBaseAmount(
                payment.getBaseAmount()
        );

        dto.setPositionAmount(
                payment.getPositionAmount()
        );

        dto.setAmount(
                payment.getAmount()
        );

        dto.setPosition(
                payment.getPosition()
        );

        dto.setPaymentStatus(
                payment.getPaymentStatus()
        );

        dto.setPaymentMethod(
                payment.getPaymentMethod()
        );

        dto.setRequestedAt(
                payment.getRequestedAt()
        );

        dto.setPaidAt(
                payment.getPaidAt()
        );

        dto.setCancelledAt(
                payment.getCancelledAt()
        );

        dto.setCancelReason(
                payment.getCancelReason()
        );

        dto.setPeriodDays(
                payment.getPeriodDays()
        );

        dto.setStartDatetime(
                payment.getStartDatetime()
        );

        dto.setEndDatetime(
                payment.getEndDatetime()
        );

        dto.setCreatedAt(
                payment.getCreatedAt()
        );

        return dto;
    }


    // =========================================================
    // 실제 이미지 파일 삭제
    // =========================================================

    private void deletePhysicalFiles(
            List<AdvertisementImage> images) {

        for (AdvertisementImage image : images) {

            String imageUrl =
                    image.getImageUrl();

            if (imageUrl == null) {
                continue;
            }

            String fileName =
                    imageUrl.replace(
                            "/upload/ad/",
                            ""
                    );

            File file =
                    new File(
                            UPLOAD_PATH,
                            fileName
                    );

            if (file.exists()) {
                file.delete();
            }
        }
    }
}