package com.moit.advertisement.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.moit.advertisement.dto.AdminAdvertisementStatDto;
import com.moit.advertisement.dto.AdvertisementCalculationResultDto;
import com.moit.advertisement.dto.AdvertisementChartDto;
import com.moit.advertisement.dto.AdvertisementDto;
import com.moit.advertisement.dto.AdvertisementImageDto;
import com.moit.advertisement.dto.AdvertisementPaymentDto;
import com.moit.advertisement.dto.AdvertisementPositionPriceDto;
import com.moit.advertisement.dto.AdvertisementPriceDto;
import com.moit.advertisement.dto.AdvertisementScore;
import com.moit.advertisement.dto.AdvertisementSearchDto;
import com.moit.advertisement.dto.DashboardAiDto;
import com.moit.advertisement.entity.Advertisement;
import com.moit.advertisement.entity.AdvertisementAiSummary;
import com.moit.advertisement.entity.AdvertisementClickLog;
import com.moit.advertisement.entity.AdvertisementDailyStatistics;
import com.moit.advertisement.entity.AdvertisementImage;
import com.moit.advertisement.entity.AdvertisementImpressionLog;
import com.moit.advertisement.entity.AdvertisementPayment;
import com.moit.advertisement.entity.AdvertisementPrice;
import com.moit.advertisement.enums.AdGrade;
import com.moit.advertisement.enums.AdPosition;
import com.moit.advertisement.enums.AdStatus;
import com.moit.advertisement.enums.ApprovalStatus;
import com.moit.advertisement.enums.PaymentHistoryStatus;
import com.moit.advertisement.enums.PaymentStatus;
import com.moit.advertisement.enums.PaymentType;
import com.moit.advertisement.repository.AdvertisementAiSummaryRepository;
import com.moit.advertisement.repository.AdvertisementClickLogRepository;
import com.moit.advertisement.repository.AdvertisementDailyStatisticsRepository;
import com.moit.advertisement.repository.AdvertisementImageRepository;
import com.moit.advertisement.repository.AdvertisementImpressionLogRepository;
import com.moit.advertisement.repository.AdvertisementPaymentRepository;
import com.moit.advertisement.repository.AdvertisementPositionPriceRepository;
import com.moit.advertisement.repository.AdvertisementPriceRepository;
import com.moit.advertisement.repository.AdvertisementRepository;
import com.moit.member.entity.Member;
import com.moit.member.entity.MemberInfo;
import com.moit.member.entity.PointHistory;
import com.moit.member.enums.PointTypeEnum;
import com.moit.member.repository.MemberInfoRepository;
import com.moit.member.repository.MemberRepository;
import com.moit.member.repository.PointHistoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementImageRepository advertisementImageRepository;
    
    private final AdvertisementCalculationService calculationService;
    
    private final AdvertisementDailyStatisticsRepository dailyStatisticsRepository;
    private final AdvertisementImpressionLogRepository impressionLogRepository;
    private final AdvertisementClickLogRepository clickLogRepository;
    private final AdvertisementPaymentRepository advertisementPaymentRepository;
    private final AdvertisementPriceRepository advertisementPriceRepository;
    private final AdvertisementPositionPriceRepository advertisementPositionPriceRepository;
        
    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final PointHistoryRepository pointHistoryRepository;

    private final MailService mailService;
    private final AdvertisementAiSummaryRepository aiSummaryRepository;

    private static final String UPLOAD_PATH = "C:/upload/ad";

    // =========================================================
    // 관리자 탭별 전용 구현 메서드
    // =========================================================

    @Override
    public List<AdvertisementDto> searchApprovalTabList(AdvertisementSearchDto dto) {
    	Pageable pageable = createPageable(dto);
        
        return advertisementRepository.findApprovalTabList(dto.getSearchText(), dto.getStatus(), pageable)
                .getContent().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public Long selectApprovalTabTotalCnt(AdvertisementSearchDto dto) {
        
        return advertisementRepository.countApprovalTabList(dto.getSearchText(), dto.getStatus());
    }
    
    @Override
    public List<AdvertisementPaymentDto> searchPaymentTabList(
            AdvertisementSearchDto dto) {

    	Pageable pageable = createPaymentPageable(dto);
        String searchText = dto.getSearchText();
        String status = dto.getStatus(); 

        return advertisementPaymentRepository
        		.findByAdvertisement_DeleteYn('N', searchText, status, pageable)
                .getContent()
                .stream()
                .map(this::toPaymentDto)
                .toList();
    }

    @Override
    public long selectPaymentTabTotalCnt(AdvertisementSearchDto dto) {
    	String searchText = dto.getSearchText();
    	String status = dto.getStatus();
    	
    	return advertisementPaymentRepository.countByAdvertisement_DeleteYnAndSearchTextAndStatus('N', searchText, status);
    }

    @Override
    public List<AdvertisementDto> searchStatusTabList(AdvertisementSearchDto dto) {
        Pageable pageable = createPageable(dto);
        
        String searchText = dto.getSearchText();
        String adStatus = dto.getStatus(); // 운영 상태(OPEN, PENDING, CLOSED)
        
        return advertisementRepository.findStatusTabList(searchText, adStatus, pageable)
                .getContent().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public long selectStatusTabTotalCnt(AdvertisementSearchDto dto) {
        String searchText = dto.getSearchText();
        String adStatus = dto.getStatus();
        
        return advertisementRepository.countStatusTabList(searchText, adStatus);
    }
    
    // =========================================================
    // 결제내역 목록
    // =========================================================
    @Override
    public List<AdvertisementPaymentDto> searchPaymentHistory(
            AdvertisementSearchDto dto) {

    	Pageable pageable = createPaymentPageable(dto);
        String searchText = dto.getSearchText();
        String status = dto.getStatus();

        return advertisementPaymentRepository
        		.findByAdvertisement_DeleteYn('N', searchText, status, pageable)
                .getContent()
                .stream()
                .map(this::toPaymentDto)
                .toList();
    }
    
    @Override
    public AdminAdvertisementStatDto.ApprovalStat getApprovalStats() {
        
        // 승인 탭 전용 쿼리 - 전체 개수
        long tabTotalCount = advertisementRepository.countApprovalTabList(null, null);
        
        // 대기와 반려 상태 개수 조회
        long waiting = advertisementRepository.countByDeleteYnAndApprovalStatus('N', ApprovalStatus.WAITING);
        long rejected = advertisementRepository.countByDeleteYnAndApprovalStatus('N', ApprovalStatus.REJECTED);
        
        // 결제 대기 = 전체 개수 - 대기 - 반려 
        long paymentWaiting = tabTotalCount - waiting - rejected;
        if (paymentWaiting < 0) {
            paymentWaiting = 0;
        }
        
        return AdminAdvertisementStatDto.ApprovalStat.builder()
                .totalCount(tabTotalCount)
                .waitingCount(waiting)
                .paymentWaitingCount(paymentWaiting)
                .rejectedCount(rejected)
                .build();
    }

    @Override
    public AdminAdvertisementStatDto.PaymentStat getPaymentStats() {
        return AdminAdvertisementStatDto.PaymentStat.builder()
                .totalCount(advertisementPaymentRepository.countByAdvertisement_DeleteYn('N'))
                // 결제 대기 (REQUESTED)
                .waitingCount(advertisementPaymentRepository.countByAdvertisement_DeleteYnAndPaymentStatus('N', PaymentHistoryStatus.REQUESTED))
                // 신규 결제 완료 (INITIAL + PAID)
                .newPaymentCount(advertisementPaymentRepository.countByAdvertisement_DeleteYnAndPaymentTypeAndPaymentStatus('N', PaymentType.INITIAL, PaymentHistoryStatus.PAID))
                // 연장 결제 완료 (EXTENSION + PAID)
                .extensionPaymentCount(advertisementPaymentRepository.countByAdvertisement_DeleteYnAndPaymentTypeAndPaymentStatus('N', PaymentType.EXTENSION, PaymentHistoryStatus.PAID))
                .build();
    }

    @Override
    public AdminAdvertisementStatDto.StatusStat getStatusStats() {
        
        // 운영 탭 전용 쿼리 - 전체 개수
        long tabTotalCount = advertisementRepository.countStatusTabList(null, null);
        
        // 상태별 개수
        long beforeOpen = advertisementRepository.countByDeleteYnAndPaymentStatusAndStatus('N', PaymentStatus.PAID, AdStatus.PENDING);
        long open = advertisementRepository.countByDeleteYnAndPaymentStatusAndStatus('N', PaymentStatus.PAID, AdStatus.OPEN);
        long closed = advertisementRepository.countByDeleteYnAndPaymentStatusAndStatus('N', PaymentStatus.PAID, AdStatus.CLOSED);

        // 카드에 매핑
        return AdminAdvertisementStatDto.StatusStat.builder()
                .totalCount(tabTotalCount) 
                .beforeOpenCount(beforeOpen)
                .openCount(open)
                .closedCount(closed)
                .build();
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

        Pageable pageable = createPageable(dto);

        return advertisementRepository
                .searchMyAdvertisement(
                        advertiserId,
                        dto.getSearchText(),
                        pageable
                )
                .getContent()
                .stream()
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

        return (int) advertisementRepository.countMyAdvertisement(
                advertiserId,
                dto.getSearchText()
        );
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

    	return (int) advertisementRepository
                .countByDeleteYnAndApprovalStatus(
                        'N',
                        ApprovalStatus.WAITING
                );
    }

    // =========================================================
    // 정렬(Sort) 공통 처리 메서드
    // =========================================================
    private Pageable createPageable(AdvertisementSearchDto dto) {
        String sortParam = dto.getSort();
        
        // 기본값: 최신순 (createdAt 내림차순)
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        if (sortParam != null && !sortParam.isEmpty() && !"all".equals(sortParam)) {
            switch (sortParam.toLowerCase()) {
                case "start":
                    sort = Sort.by(Sort.Direction.ASC, "startDatetime"); // 시작 빠른순
                    break;
                case "end":
                    sort = Sort.by(Sort.Direction.ASC, "endDatetime"); // 종료 임박순
                    break;
                case "budget":
                	sort = Sort.by(Sort.Direction.DESC, "totalBudget"); // 예산 높은순
                	break;
                case "amount":
                	sort = Sort.by(Sort.Direction.DESC, "amount"); // 결제 금액 높은순
                    break;
                case "impressions":
                	sort = Sort.by(Sort.Direction.DESC, "impressions"); // 노출수순
                    break;
                case "clicks":
                	sort = Sort.by(Sort.Direction.DESC, "clicks"); // 클릭수순
                    break;
                case "grade":
                    sort = Sort.by(Sort.Direction.ASC, "adGrade"); // 등급순 (알파벳 오름차순 시 GENERAL -> PREMIUM)
                    break;
                case "date": 
                    sort = Sort.by(Sort.Direction.ASC, "createdAt"); // 결제 예정순 (오래된 순)
                    break;
                case "status":
                    sort = Sort.by( Sort.Direction.ASC, "status" );
                    break;
            }
        }
        
        return PageRequest.of(dto.getPage() - 1, dto.getSize(), sort);
    }
    
    // 결제 탭 정렬
    private Pageable createPaymentPageable(AdvertisementSearchDto dto) {
        String sortParam = dto.getSort();
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        if (sortParam != null && !sortParam.isEmpty() && !"all".equals(sortParam)) {
            switch (sortParam.toLowerCase()) {
                case "budget":
                    sort = Sort.by(Sort.Direction.DESC, "advertisement.totalBudget"); // 앞에 advertisement. 추가!
                    break;
                case "amount":
                    sort = Sort.by(Sort.Direction.DESC, "amount");
                    break;
                case "impressions":
                    sort = Sort.by(Sort.Direction.DESC, "advertisement.impressions"); // 앞에 advertisement. 추가!
                    break;
                case "clicks":
                    sort = Sort.by(Sort.Direction.DESC, "advertisement.clicks");      // 앞에 advertisement. 추가!
                    break;
                case "grade":
                    sort = Sort.by(Sort.Direction.ASC, "advertisement.adGrade");
                    break;
                case "date": 
                    sort = Sort.by(Sort.Direction.ASC, "createdAt");
                    break;
            }
        }
        return PageRequest.of(dto.getPage() - 1, dto.getSize(), sort);
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
                        .adGrade(dto.getAdGrade())
                        .pendingPaymentType(dto.getPaymentType())
                        .startDatetime(dto.getStartDatetime())
                        .endDatetime(dto.getEndDatetime())
                        .totalBudget(dto.getTotalBudget())
                        
                        // 신규 광고 등록 초기 상태
                        .approvalStatus(ApprovalStatus.WAITING)
                        .paymentStatus(PaymentStatus.WAITING)
                        .status(AdStatus.PENDING)
                        
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
                        adPosition
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
        
        // 결제 완료 후에는 광고 수정 불가
        if (advertisement.getPaymentStatus() == PaymentStatus.PAID) {
            throw new IllegalStateException(
                    "결제가 완료된 광고는 수정할 수 없습니다."
            );
        }

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

        advertisement.resetApprovalStatusForUpdate();
        
        // 반려 후 수정하면 기존 결제대기 내역 제거
        List<AdvertisementPayment> requestedPayments =
                advertisementPaymentRepository
                        .findAllByAdvertisement_AdIdAndPaymentStatus(
                                adId,
                                PaymentHistoryStatus.REQUESTED
                        );

        advertisementPaymentRepository.deleteAll(requestedPayments);
        advertisementPaymentRepository.flush();

        // -----------------------------------------------------
        // 이미지 수정 처리 (위치별 개별 갱신)
        // -----------------------------------------------------
        if (imageFiles != null && !imageFiles.isEmpty() && imageTypes != null && !imageTypes.isEmpty()) {
            File directory = new File(UPLOAD_PATH);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            for (int i = 0; i < imageFiles.size(); i++) {
                MultipartFile file = imageFiles.get(i);
                
                // 파일이 비어있으면 해당 위치는 건너뜁니다 (기존 이미지 유지)
                if (file == null || file.isEmpty()) {
                    continue;
                }

                String imageTypeName = imageTypes.get(i);
                AdPosition targetPosition = AdPosition.valueOf(imageTypeName);

                // 해당 위치(Position)에 이미 등록된 기존 이미지가 있다면 물리 파일 및 DB에서 삭제
                List<AdvertisementImage> existingImages = advertisementImageRepository.findByAdvertisement_AdId(adId);
                for (AdvertisementImage oldImg : existingImages) {
                    if (oldImg.getImageType() == targetPosition) {
                        // 물리 파일 삭제
                        if (oldImg.getImageUrl() != null) {
                            String fileName = oldImg.getImageUrl().replace("/upload/ad/", "");
                            File physicalFile = new File(UPLOAD_PATH, fileName);
                            if (physicalFile.exists()) {
                                physicalFile.delete();
                            }
                        }
                        // DB에서 해당 위치 이미지 삭제
                        advertisementImageRepository.delete(oldImg);
                    }
                }

                // 삭제가 DB에 즉시 반영
                advertisementImageRepository.flush();
                
                // 2. 새로운 파일 업로드 및 저장
                String originalName = file.getOriginalFilename();
                String saveName = UUID.randomUUID().toString() + "_" + originalName;

                try {
                    file.transferTo(new File(directory, saveName));
                } catch (IOException e) {
                    throw new RuntimeException("광고 이미지 저장 실패", e);
                }

                AdvertisementImage newImage = AdvertisementImage.builder()
                        .advertisement(advertisement)
                        .imageType(targetPosition)
                        .imageUrl("/upload/ad/" + saveName)
                        .build();

                advertisementImageRepository.save(newImage);
            }
        }

        return 1;
    }


    // =========================================================
    // 광고 삭제
    // =========================================================

    @Override
    @Transactional
    public int deleteAdvertisement(
            Long adId,
            Long memberId) {

        Advertisement advertisement =
                advertisementRepository
                        .findById(adId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "광고를 찾을 수 없습니다."
                                )
                        );
        if (!advertisement.getAdvertiser().getId().equals(memberId)) {
            throw new IllegalArgumentException("본인의 광고만 삭제할 수 있습니다.");
        }


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

        // 기존 결제 상태 확인
        PaymentStatus paymentStatus = advertisement.getPaymentStatus();
        
        // 광고 승인
        advertisement.approve(admin);

        // ==========================================
        // 최초 승인 + 결제 전인 경우에만 메일 발송
        // ==========================================
        if (paymentStatus == PaymentStatus.WAITING) {

            Member advertiser =
                    advertisement.getAdvertiser();

            if (advertiser != null) {

                String advertiserEmail =
                        advertiser.getEmail();

                AdvertisementDto adDto = new AdvertisementDto();

                adDto.setAdId(advertisement.getAdId());
                adDto.setTitle(advertisement.getTitle());
                adDto.setStartDatetime(
                        advertisement.getStartDatetime()
                );
                adDto.setEndDatetime(
                        advertisement.getEndDatetime()
                );

                // 승인 시점에는 결제 내역이 없으므로
                // 광고에 저장된 실제 광고 금액 사용
                adDto.setPaymentAmount(
                        advertisement.getTotalBudget()
                );

                mailService.sendAdvertisementPaymentRequestMail(
                        adDto,
                        advertiserEmail
                );
             }
          }

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
    
    
    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementPriceDto> getInitialPrices() {

        List<AdvertisementPrice> priceList =
                advertisementPriceRepository
		                .findByPaymentTypeOrderByPeriodDaysAsc(
		                        PaymentType.INITIAL
		                );

        return priceList.stream()
                .map(price -> AdvertisementPriceDto.builder()
                        .priceId(price.getPriceId())
                        .adGrade(price.getAdGrade())
                        .periodDays(price.getPeriodDays())
                        .paymentType(price.getPaymentType())
                        .basePrice(price.getBasePrice())
                        .build())
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementPositionPriceDto> getPositionPrices() {

        return advertisementPositionPriceRepository
                .findAllByOrderByPositionAsc()
                .stream()
                .map(price -> {
                    AdvertisementPositionPriceDto dto =
                            new AdvertisementPositionPriceDto();

                    dto.setPositionPriceId(price.getPositionPriceId());
                    dto.setPosition(price.getPosition());
                    dto.setAdditionalPrice(price.getAdditionalPrice());

                    return dto;
                })
                .toList();
    }
    
    // =========================================================
    // 결제 생성
    // =========================================================
    @Override
    @Transactional
    public AdvertisementPaymentDto createInitialPayment(
            Long adId,
            Long memberId) {

        Advertisement advertisement =
                advertisementRepository
                        .findByAdIdAndDeleteYn(adId, 'N')
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "광고를 찾을 수 없습니다."
                                )
                        );

        // 광고주 본인 확인
        if (!advertisement.getAdvertiser().getId().equals(memberId)) {
            throw new IllegalArgumentException(
                    "본인의 광고만 결제할 수 있습니다."
            );
        }

        // 승인된 광고만 결제 가능
        if (advertisement.getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new IllegalArgumentException(
                    "승인된 광고만 결제할 수 있습니다."
            );
        }

        // 이미 결제된 광고인지 확인
        if (advertisement.getPaymentStatus() == PaymentStatus.PAID) {
            throw new IllegalArgumentException(
                    "이미 결제가 완료된 광고입니다."
            );
        }

        // 기존 결제 요청이 있으면 재사용
        AdvertisementPayment payment =
                advertisementPaymentRepository
                        .findByAdvertisement_AdIdAndPaymentStatus(
                                adId,
                                PaymentHistoryStatus.REQUESTED
                        )
                        .orElse(null);

        if (payment == null) {

            // =====================================================
            // 이미지 위치 조회
            // =====================================================

            List<AdvertisementImageDto> imageList =
                    selectAdvertisementImageList(adId);

            List<AdPosition> positions =
                    imageList.stream()
                            .map(AdvertisementImageDto::getImageType)
                            .filter(type -> type != null)
                            .map(AdPosition::valueOf)
                            .toList();

            // =====================================================
            // 결제 타입
            // =====================================================

            PaymentType paymentType =
                    advertisement.getPendingPaymentType();

            if (paymentType == null) {
                paymentType = PaymentType.INITIAL;
            }

            // =====================================================
            // 서버에서 가격 재계산
            // =====================================================

            AdvertisementCalculationResultDto calculation =
                    calculationService.calculate(
                            advertisement.getStartDatetime(),
                            advertisement.getEndDatetime(),
                            advertisement.getAdGrade(),
                            paymentType,
                            positions
                    );

            BigDecimal baseAmount =
                    calculation.getBasePrice();

            BigDecimal positionAmount =
                    calculation.getPositionPrice();

            BigDecimal amount =
                    calculation.getTotalAmount();

            // =====================================================
            // 주문번호
            // =====================================================

            String orderId =
                    "AD_"
                    + adId
                    + "_"
                    + UUID.randomUUID()
                            .toString()
                            .replace("-", "")
                            .substring(0, 12);

            // =====================================================
            // 결제 이력 생성
            // =====================================================

            payment = AdvertisementPayment.builder()
                    .advertisement(advertisement)
                    .advertiser(advertisement.getAdvertiser())
                    .paymentType(paymentType)
                    .orderId(orderId)

                    .baseAmount(baseAmount)
                    .positionAmount(positionAmount)
                    .amount(amount)

                    // 실제 광고 위치 저장
                    .position(positions.isEmpty()
                            ? AdPosition.MAIN
                            : positions.get(0))

                    .paymentStatus(PaymentHistoryStatus.REQUESTED)

                    .periodDays(
                            calculation.getTotalDays()
                    )

                    .startDatetime(
                            advertisement.getStartDatetime()
                    )

                    .endDatetime(
                            advertisement.getEndDatetime()
                    )

                    .build();

            advertisementPaymentRepository.save(payment);
        }

        return toPaymentDto(payment);
    }
    
    
    @Override
    @Transactional
    public AdvertisementPaymentDto createExtensionPayment(
            Long adId,
            Long memberId,
            int days) {
    	
    	// 광고주 본인의 광고인지 확인
        Advertisement advertisement =
                advertisementRepository
                        .findByAdIdAndAdvertiser_Id(adId, memberId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "광고를 찾을 수 없습니다."
                                )
                        );

        // 연장 가능 기간 검증
        if (days != 7 &&
            days != 14 &&
            days != 30 &&
            days != 60 &&
            days != 90) {

            throw new IllegalArgumentException(
                    "지원하지 않는 연장 기간입니다."
            );
        }

        // DB에서 연장 가격 조회
        AdvertisementPrice price =
                advertisementPriceRepository
                        .findByPaymentTypeAndAdGradeAndPeriodDays(
                                PaymentType.EXTENSION,
                                advertisement.getAdGrade(),
                                days
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "해당 연장 기간의 가격을 찾을 수 없습니다."
                                )
                        );

        BigDecimal amount = price.getBasePrice();

        // 연장 결제 대기 상태
        advertisement.waitForExtensionPayment();

        // 결제 주문번호 생성
        String orderId = "AD_EXT_" + adId + "_" + UUID.randomUUID();

        AdvertisementPayment payment =
                AdvertisementPayment.builder()
                        .advertisement(advertisement)
                        .advertiser(advertisement.getAdvertiser())
                        .orderId(orderId)
                        .baseAmount(amount)
                        .positionAmount(BigDecimal.ZERO)
                        .amount(amount)
                        .paymentType(PaymentType.EXTENSION)
                        .periodDays(days)
                        .build();

        advertisementPaymentRepository.save(payment);

        return AdvertisementPaymentDto.from(payment);
    }

    // 연장 가격 조회
    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementPriceDto> getExtensionPrices(Long adId, Long memberId) {

        Advertisement advertisement =
                advertisementRepository.findById(adId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "광고를 찾을 수 없습니다."
                                )
                        );

			    advertisementRepository
			        .findByAdIdAndAdvertiser_Id(adId, memberId)
			        .orElseThrow(() ->
			            new IllegalArgumentException("본인의 광고만 조회할 수 있습니다.")
			        );

        AdGrade adGrade = advertisement.getAdGrade();

        List<AdvertisementPrice> priceList =
                advertisementPriceRepository
                        .findByPaymentTypeAndAdGradeOrderByPeriodDaysAsc(
                                PaymentType.EXTENSION,
                                adGrade
                        );

        return priceList.stream()
                .map(price -> AdvertisementPriceDto.builder()
                        .priceId(price.getPriceId())
                        .adGrade(price.getAdGrade())
                        .periodDays(price.getPeriodDays())
                        .paymentType(price.getPaymentType())
                        .basePrice(price.getBasePrice())
                        .build()
                )
                .toList();
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
    // 이미지 저장
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

        AdPosition position = AdPosition.valueOf(dto.getImageType());
        
        // 같은 광고에 같은 위치 이미지 중복 등록 방지
        boolean exists =
                advertisementImageRepository
                        .findByAdvertisement_AdIdAndImageType(
                                dto.getAdId(),
                                position
                        )
                        .isPresent();

        if (exists) {
            throw new IllegalArgumentException(
                    "이미 해당 위치에 광고 이미지가 등록되어 있습니다."
            );
        }
        
        // 최대 4개 위치
        List<AdvertisementImage> existingImages =
                advertisementImageRepository
                        .findByAdvertisement_AdId(dto.getAdId());

        if (existingImages.size() >= 4) {
            throw new IllegalArgumentException(
                    "광고 이미지는 최대 4개 위치까지 등록할 수 있습니다."
            );
        }
        
        AdvertisementImage image =
                AdvertisementImage.builder()
                        .advertisement(advertisement)
                        .imageType(position)
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
                advertisementImageRepository.findByAdvertisement_AdId(adId);

        deletePhysicalFiles(images);

        advertisementImageRepository
                .deleteAll(images);

        return images.size();
    }


    // =========================================================
    // 광고 노출 / 클릭
    // =========================================================

    @Override
    public AdvertisementDto selectAdvertisement(
            String position,
            Long memberId,
            String sessionId) {

        AdPosition adPosition = AdPosition.valueOf(position);

        List<Advertisement> candidates =
                advertisementRepository
                        .findAvailableAdvertisements(adPosition);

        if (candidates.isEmpty()) {
            return null;
        }
        
        List<AdvertisementScore> scores = new ArrayList<>();

        for (Advertisement ad : candidates) {

            int recentImpressions =
                    advertisementRepository.countRecentImpressions(
                            ad.getAdId(),
                            memberId,
                            sessionId
                    );

            int fatigue  = calculateFatigue(recentImpressions);

            double score =
                    calculateFinalScore(
                            ad,
                            fatigue
                    );

            scores.add(
                    new AdvertisementScore(
                            ad,
                            score
                    )
            );
        }

        Advertisement selected = weightedRandomSelect(scores);

        return toDto(selected);
    }
	    
	 // =========================================================
	 // 클릭 로그 + 클릭수 + 포인트 적립
	 // =========================================================
	 @Override
	 @Transactional
	 public boolean insertClickLog(
	         Long adId,
	         String position,
	         Long memberId,
	         String sessionId,
	         String ip,
	         String userAgent,
	         String referrer) {
	
	     // 광고 조회
	     Advertisement advertisement =
	             advertisementRepository.findById(adId)
	                     .orElseThrow(() ->
	                             new IllegalArgumentException(
	                                     "광고를 찾을 수 없습니다."
	                             )
	                     );
	
	     AdPosition adPosition =
	             AdPosition.valueOf(position);
	
	     // 최근 1시간 이내 중복 클릭 확인
	     LocalDateTime oneHourAgo =
	             LocalDateTime.now().minusHours(1);
	
	     boolean alreadyClicked;
	
	     if (memberId != null) {
	
	         // 로그인 사용자
	         // 광고 + 회원 + 위치 기준
	         alreadyClicked =
	                 clickLogRepository
	                         .existsByAdvertisement_AdIdAndMember_IdAndPositionAndClickedAtAfter(
	                                 adId,
	                                 memberId,
	                                 adPosition,
	                                 oneHourAgo
	                         );
	
	     } else {
	
	         // 비로그인 사용자
	         // 광고 + session + 위치 기준
	         alreadyClicked =
	                 clickLogRepository
	                         .existsByAdvertisement_AdIdAndIpAddressAndPositionAndClickedAtAfter(
	                                 adId,
	                                 sessionId,
	                                 adPosition,
	                                 oneHourAgo
	                         );
	     }
	
	     // 중복 클릭
	     if (alreadyClicked) {
	         return false;
	     }
	
	     // 회원 조회
	     Member member = null;
	
	     if (memberId != null) {
	         member =
	                 memberRepository.findById(memberId)
	                         .orElse(null);
	     }
	
	     // 클릭 로그 저장
	     AdvertisementClickLog clickLog =
	             AdvertisementClickLog.builder()
	                     .advertisement(advertisement)
	                     .member(member)
	                     .deviceType(getDeviceType(userAgent))
	                     .ipAddress(ip)
	                     .referrer(referrer)
	                     .position(adPosition)
	                     .build();
	
	     clickLogRepository.save(clickLog);
	
	     // 광고 클릭수 증가
	     advertisement.increaseClicks();
	
	     // 로그인 사용자 포인트 적립
	     if (member != null) {
	
	         final int POINT = 10;
	
	         MemberInfo memberInfo =
	                 memberInfoRepository.findById(memberId)
	                         .orElseThrow(() ->
	                                 new IllegalArgumentException(
	                                         "회원 정보를 찾을 수 없습니다."
	                                 )
	                         );
	
	         int currentPoint =
	                 memberInfo.getPoint() == null
	                         ? 0
	                         : memberInfo.getPoint();
	
	         memberInfo.setPoint(currentPoint + POINT);
	
	         // 포인트 적립 이력
	         PointHistory history =
	                 new PointHistory();
	
	         history.setMember(member);
	         history.setPointPm(POINT);
	         history.setPointType(PointTypeEnum.SAVE.name());
	         history.setPointReason("광고 클릭 적립");
	
	         pointHistoryRepository.save(history);
	     }
	
	     return true;
	 }
    
    private String getDeviceType(String userAgent) {

        if (userAgent == null) {
            return "UNKNOWN";
        }

        String ua = userAgent.toLowerCase();

        if (ua.contains("ipad")
                || ua.contains("tablet")) {
            return "TABLET";
        }

        if (ua.contains("mobile")) {
            return "MOBILE";
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
	        String sessionId,
	        String ip,
	        String userAgent) {

	    // 노출 로그 저장 로직
		Advertisement advertisement =
                advertisementRepository.findById(adId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "광고를 찾을 수 없습니다."
                                )
                        );
		
		AdPosition adPosition = AdPosition.valueOf(position);

	    // 최근 10분 이내 동일 광고 + IP + 위치 노출 여부 확인
	    LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);

	    boolean alreadyViewed;

	    if (memberId != null) {

	        // 로그인 사용자
	        alreadyViewed =
	                impressionLogRepository
	                        .existsByAdvertisement_AdIdAndMember_IdAndPositionAndViewedAtAfter(
	                                adId,
	                                memberId,
	                                adPosition,
	                                tenMinutesAgo
	                        );

	    } else {

	        // 비로그인 사용자
	        alreadyViewed =
	                impressionLogRepository
	                        .existsByAdvertisement_AdIdAndSessionIdAndPositionAndViewedAtAfter(
	                                adId,
	                                sessionId,
	                                adPosition,
	                                tenMinutesAgo
	                        );
	    }

	    // 10분 이내 이미 노출됨
	    if (alreadyViewed) { return false; }

        Member member = null;

        if (memberId != null) {
            member = memberRepository.findById(memberId)
                    .orElse(null);
        }
		
		AdvertisementImpressionLog impressionLog =
		        AdvertisementImpressionLog.builder()
		                .advertisement(advertisement)
		                .member(member)
		                .sessionId(sessionId)
		                .deviceType(getDeviceType(userAgent))
		                .ipAddress(ip)
		                .position(adPosition)
		                .build();

		impressionLogRepository.save(impressionLog);
		advertisementRepository.increaseImpressions(adId);

	    return true;
	}


    // =========================================================
    // 기본 통계
    // =========================================================

    @Override
    public int selectTotalAdvertisementCnt() {

    	return (int) advertisementRepository.countByDeleteYn('N');
    }


    @Override
    public int selectOpenAdvertisementCnt() {
        return (int) advertisementRepository
                .countByDeleteYnAndStatus('N', AdStatus.OPEN);
    }

    @Override
    public int selectPendingAdvertisementCnt() {
        return (int) advertisementRepository
                .countByDeleteYnAndStatus('N', AdStatus.PENDING);
    }

    @Override
    public int selectClosedAdvertisementCnt() {
        return (int) advertisementRepository
                .countByDeleteYnAndStatus('N', AdStatus.CLOSED);
    }
    
    
	 // =========================================================
	 // 통계 차트
	 // =========================================================
    @Override
    @Transactional
    public void insertDailyStatistics() {

        LocalDate statDate = LocalDate.now().minusDays(1);

        List<AdvertisementImpressionLog> impressionLogs =
                impressionLogRepository.findByViewedAtBetween(
                        statDate.atStartOfDay(),
                        statDate.plusDays(1).atStartOfDay()
                );

        Map<String, Long> impressionMap =
                impressionLogs.stream()
                        .collect(Collectors.groupingBy(
                                log -> log.getAdvertisement().getAdId()
                                        + "_" + log.getPosition().name(),
                                Collectors.counting()
                        ));

        List<AdvertisementClickLog> clickLogs =
                clickLogRepository.findByClickedAtBetween(
                        statDate.atStartOfDay(),
                        statDate.plusDays(1).atStartOfDay()
                );

        Map<String, Long> clickMap =
                clickLogs.stream()
                        .collect(Collectors.groupingBy(
                                log -> log.getAdvertisement().getAdId()
                                        + "_" + log.getPosition().name(),
                                Collectors.counting()
                        ));

        Set<String> keys = new HashSet<>();

        keys.addAll(impressionMap.keySet());
        keys.addAll(clickMap.keySet());

        for (String key : keys) {

            String[] split = key.split("_");

            Long adId = Long.valueOf(split[0]);
            AdPosition position = AdPosition.valueOf(split[1]);

            if (dailyStatisticsRepository
                    .existsByAdvertisement_AdIdAndStatDateAndPosition(
                            adId,
                            statDate,
                            position)) {
                continue;
            }

            Advertisement advertisement =
                    advertisementRepository.findById(adId)
                            .orElse(null);

            if (advertisement == null) {
                continue;
            }

            long impressions =
                    impressionMap.getOrDefault(key, 0L);

            long clicks =
                    clickMap.getOrDefault(key, 0L);

            BigDecimal ctr =
                    impressions == 0
                            ? BigDecimal.ZERO
                            : BigDecimal.valueOf(
                                    ((double) clicks / impressions) * 100
                            ).setScale(2, RoundingMode.HALF_UP);

            BigDecimal fatigueScore =
                    calculateFatigueScore(
                            adId,
                            statDate
                    );

            AdvertisementDailyStatistics statistics =
                    AdvertisementDailyStatistics.builder()
                            .advertisement(advertisement)
                            .statDate(statDate)
                            .impressions(impressions)
                            .clicks(clicks)
                            .ctr(ctr)
                            .fatigueScore(fatigueScore)
                            .position(position)
                            .build();

            dailyStatisticsRepository.save(statistics);
        }
    }
	
    @Override
    @Transactional(readOnly = true)
    public AdvertisementChartDto selectSummary() {

        AdvertisementChartDto dto = new AdvertisementChartDto();

        // 삭제되지 않은 광고 수
        int totalAd = selectTotalAdvertisementCnt();

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6);

        Long totalImp =
                dailyStatisticsRepository.sumImpressions(
                        startDate,
                        today
                );

        Long totalClick =
                dailyStatisticsRepository.sumClicks(
                        startDate,
                        today
                );

        if (totalImp == null) {
            totalImp = 0L;
        }

        if (totalClick == null) {
            totalClick = 0L;
        }

        double avgCtr = totalImp == 0
                ? 0.0
                : (totalClick.doubleValue() / totalImp.doubleValue()) * 100;

        dto.setTotalAd(totalAd);
        dto.setTotalImp(totalImp.intValue());
        dto.setTotalClick(totalClick.intValue());
        dto.setAvgCtr(avgCtr);

        return dto;
    }
	
    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementChartDto> selectDailyChart() {

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6);

        List<AdvertisementDailyStatistics> statistics =
                dailyStatisticsRepository.findRecentStatistics(startDate);

        return statistics.stream()
                .collect(Collectors.groupingBy(
                        AdvertisementDailyStatistics::getStatDate,
                        TreeMap::new,
                        Collectors.toList()
                ))
                .entrySet()
                .stream()
                .map(entry -> {

                    LocalDate date = entry.getKey();

                    long impressions = entry.getValue()
                            .stream()
                            .mapToLong(s -> s.getImpressions() == null
                                    ? 0L
                                    : s.getImpressions())
                            .sum();

                    long clicks = entry.getValue()
                            .stream()
                            .mapToLong(s -> s.getClicks() == null
                                    ? 0L
                                    : s.getClicks())
                            .sum();

                    AdvertisementChartDto dto = new AdvertisementChartDto();

                    dto.setStatDate(date.toString());
                    dto.setImpressions((int) impressions);
                    dto.setClicks((int) clicks);

                    return dto;
                })
                .toList();
    }
	
    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementChartDto> selectTopCtrChart() {

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6);

        List<AdvertisementDailyStatistics> statistics =
                dailyStatisticsRepository.findRecentStatistics(startDate);

        return statistics.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getAdvertisement().getAdId(),
                        Collectors.toList()
                ))
                .values()
                .stream()
                .map(list -> {

                    long impressions = list.stream()
                            .mapToLong(s -> s.getImpressions() == null
                                    ? 0L
                                    : s.getImpressions())
                            .sum();

                    long clicks = list.stream()
                            .mapToLong(s -> s.getClicks() == null
                                    ? 0L
                                    : s.getClicks())
                            .sum();

                    double ctr = impressions == 0
                            ? 0.0
                            : ((double) clicks / impressions) * 100;

                    Advertisement advertisement =
                            list.get(0).getAdvertisement();

                    AdvertisementChartDto dto =
                            new AdvertisementChartDto();

                    dto.setTitle(advertisement.getTitle());
                    dto.setCtr(ctr);

                    return dto;

                })
                .sorted(
                        Comparator.comparing(
                                AdvertisementChartDto::getCtr,
                                Comparator.nullsLast(
                                        Comparator.reverseOrder()
                                )
                        )
                )
                .limit(5)
                .toList();
    }
	
    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementChartDto> selectGradeChart() {

        List<Advertisement> advertisements =
                advertisementRepository.findByDeleteYn('N');

        return advertisements.stream()
                .collect(Collectors.groupingBy(
                        Advertisement::getAdGrade,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .map(entry -> {

                    AdvertisementChartDto dto =
                            new AdvertisementChartDto();

                    dto.setAdGrade(entry.getKey().name());
                    dto.setCount(entry.getValue().intValue());

                    return dto;

                })
                .toList();
    }
	
    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementChartDto> selectPositionChart() {

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6);

        List<AdvertisementDailyStatistics> statistics =
                dailyStatisticsRepository.findRecentStatistics(startDate);

        return statistics.stream()
                .collect(Collectors.groupingBy(
                        AdvertisementDailyStatistics::getPosition,
                        Collectors.summingLong(
                                s -> s.getImpressions() == null
                                        ? 0L
                                        : s.getImpressions()
                        )
                ))
                .entrySet()
                .stream()
                .map(entry -> {

                    AdvertisementChartDto dto =
                            new AdvertisementChartDto();

                    dto.setPosition(entry.getKey().name());
                    dto.setImpressions(entry.getValue().intValue());

                    return dto;

                })
                .toList();
    }
	
    @Override
    @Transactional(readOnly = true)
    public double selectExtensionRate() {

        long totalPaid =
                advertisementPaymentRepository
                        .countByAdvertisement_DeleteYnAndPaymentStatus(
                                'N',
                                PaymentHistoryStatus.PAID
                        );

        long extensionPaid =
                advertisementPaymentRepository
                        .countByAdvertisement_DeleteYnAndPaymentTypeAndPaymentStatus(
                                'N',
                                PaymentType.EXTENSION,
                                PaymentHistoryStatus.PAID
                        );

        if (totalPaid == 0) {
            return 0.0;
        }

        return ((double) extensionPaid / totalPaid) * 100;
    }
	
    @Override
    @Transactional(readOnly = true)
    public List<AdvertisementChartDto> selectPositionCtrChart() {

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6);

        List<AdvertisementDailyStatistics> statistics =
                dailyStatisticsRepository.findRecentStatistics(startDate);

        return statistics.stream()
                .collect(Collectors.groupingBy(
                        AdvertisementDailyStatistics::getPosition,
                        Collectors.toList()
                ))
                .entrySet()
                .stream()
                .map(entry -> {

                    long impressions = entry.getValue()
                            .stream()
                            .mapToLong(s -> s.getImpressions() == null
                                    ? 0L
                                    : s.getImpressions())
                            .sum();

                    long clicks = entry.getValue()
                            .stream()
                            .mapToLong(s -> s.getClicks() == null
                                    ? 0L
                                    : s.getClicks())
                            .sum();

                    double ctr = impressions == 0
                            ? 0.0
                            : ((double) clicks / impressions) * 100;

                    AdvertisementChartDto dto =
                            new AdvertisementChartDto();

                    dto.setPosition(entry.getKey().name());
                    dto.setCtr(ctr);

                    return dto;

                })
                .toList();
    }
	 
	 
    // ai 요약 저장
    @Override
    @Transactional
    public void saveAiSummary(String summary) {

        AdvertisementAiSummary entity =
                AdvertisementAiSummary.builder()
                        .summary(summary)
                        .build();

        aiSummaryRepository.save(entity);
    }
    
    // 최근 ai 요약 불러오기
    @Override
    @Transactional(readOnly = true)
    public DashboardAiDto getLatestAiSummary() {

        AdvertisementAiSummary entity =
        		aiSummaryRepository
                        .findTopByOrderByCreatedAtDesc()
                        .orElse(null);

        if (entity == null) {
            return null;
        }

        DashboardAiDto dto = new DashboardAiDto();

        dto.setSummaryId(entity.getSummaryId().intValue());
        dto.setSummary(entity.getSummary());
        dto.setCreatedAt(entity.getCreatedAt().toString());

        return dto;
    }
    
    @Override
    @Transactional(readOnly = true)
    public DashboardAiDto getDashboardAiData() {

        DashboardAiDto dto = new DashboardAiDto();  
        AdvertisementChartDto summary = selectSummary();

        if (summary != null) {
            dto.setTotalAd(summary.getTotalAd());
            dto.setTotalImp(summary.getTotalImp());
            dto.setTotalClick(summary.getTotalClick());
            dto.setAvgCtr(summary.getAvgCtr());
        }
        dto.setExtensionRate( selectExtensionRate() );

        // 아래 3개는 기존 Dashboard 조회 결과에서 계산
        List<AdvertisementChartDto> positionList = selectPositionCtrChart();

        if (positionList != null && !positionList.isEmpty()) {
            AdvertisementChartDto best =
                    positionList.stream()
                            .max((a, b) ->
                                    Double.compare(
                                            a.getCtr(),
                                            b.getCtr()
                                    ))
                            .orElse(null);

            AdvertisementChartDto worst =
                    positionList.stream()
                            .min((a, b) ->
                                    Double.compare(
                                            a.getCtr(),
                                            b.getCtr()
                                    ))
                            .orElse(null);

            if (best != null) {
                dto.setBestPosition(
                        best.getPosition()
                );
            }

            if (worst != null) {
                dto.setWorstPosition(
                        worst.getPosition()
                );
            }
        }
        List<AdvertisementChartDto> gradeList = selectGradeChart();

        if (gradeList != null && !gradeList.isEmpty()) {
            AdvertisementChartDto topGrade =
                    gradeList.stream()
                            .max((a, b) ->
                                    Integer.compare(
                                            a.getCount(),
                                            b.getCount()
                                    ))
                            .orElse(null);

            if (topGrade != null) {
                dto.setTopGrade(
                        topGrade.getAdGrade()
                );
            }
        }
        return dto;
    }

    // =========================================================
    // DTO 변환
    // =========================================================

    private AdvertisementDto toDto( Advertisement ad) {

        AdvertisementDto dto = new AdvertisementDto();

        dto.setAdId(ad.getAdId());

        dto.setTitle(ad.getTitle());
        dto.setContent(ad.getContent());
        dto.setLandingUrl(ad.getLandingUrl());

        dto.setTargetAgeMin( ad.getTargetAgeMin() );  
        dto.setTargetAgeMax( ad.getTargetAgeMax() );  
        dto.setTargetGender( ad.getTargetGender() );

        dto.setStartDatetime( ad.getStartDatetime() );  
        dto.setEndDatetime( ad.getEndDatetime() );

        dto.setStatus( ad.getStatus() );  
        dto.setApprovalStatus( ad.getApprovalStatus() );  
        dto.setPaymentStatus( ad.getPaymentStatus() );  
        dto.setAdGrade( ad.getAdGrade() );
        dto.setPendingPaymentType(ad.getPendingPaymentType());

        if (ad.getAdvertiser() != null) {
            dto.setAdvertiserId(ad.getAdvertiser().getId()); 
            dto.setAdvertiserNickname(ad.getAdvertiser().getNickname());
        }

        dto.setImpressions( ad.getImpressions() );  
        dto.setClicks( ad.getClicks() );
        dto.setPriorityScore( ad.getPriorityScore() );  
        dto.setRejectReason(ad.getRejectReason());  
        dto.setFatigueScore( ad.getFatigueScore() );

        dto.setReminder30dSent( ad.getReminder30dSent() );  
        dto.setReminder14dSent( ad.getReminder14dSent() );  
        dto.setDeleteYn( ad.getDeleteYn() );  
        dto.setCreatedAt( ad.getCreatedAt() );  
        dto.setUpdatedAt( ad.getUpdatedAt() ); 
        
        // 이미지 목록
        List<AdvertisementImageDto> imageList = selectAdvertisementImageList(ad.getAdId());
        dto.setImageList(imageList);

        // 광고 가격 계산
        
        	// Advertisement에 저장된 실제 광고 예산
        	dto.setTotalBudget(ad.getTotalBudget());
        
        if (ad.getStartDatetime() != null
                && ad.getEndDatetime() != null
                && ad.getAdGrade() != null) {

            List<AdPosition> positions =
                    imageList.stream()
                            .map(AdvertisementImageDto::getImageType)
                            .filter(type -> type != null)
                            .map(AdPosition::valueOf)
                            .toList();

            // 광고 등록 당시 사용한 결제 타입
            PaymentType paymentType = ad.getPendingPaymentType();
            
            // pendingPaymentType이 없으면 기본 결제 타입 사용
            if (paymentType == null) {
                paymentType = PaymentType.INITIAL;
            }
            
            AdvertisementCalculationResultDto calculation =
                    calculationService.calculate(
                            ad.getStartDatetime(),
                            ad.getEndDatetime(),
                            ad.getAdGrade(),
                            paymentType,
                            positions
                    );

            dto.setTotalDays( calculation.getTotalDays() );  
            dto.setBasePrice( calculation.getBasePrice() );  
            dto.setPositionPrice( calculation.getPositionPrice() );  
            dto.setCalculatedAmount( calculation.getTotalAmount() );
        }
        
     // 결제 정보
     advertisementPaymentRepository.findTopByAdvertisement_AdIdOrderByCreatedAtDesc(ad.getAdId())
             .ifPresent(payment -> {

                 dto.setPaymentType(payment.getPaymentType());  
                 dto.setPaymentHistoryStatus( payment.getPaymentStatus() );

                 dto.setPaymentAmount( payment.getAmount() );  
                 dto.setPaidAt( payment.getPaidAt() );
                 
                 dto.setOrderId(payment.getOrderId());
                 dto.setPaymentKey(payment.getPaymentKey());
                 
                 dto.setPaymentMethod(payment.getPaymentMethod());
                 
                 dto.setBaseAmount(payment.getBaseAmount());
                 dto.setPositionAmount(payment.getPositionAmount());
                 
                 dto.setCancelledAt(payment.getCancelledAt());
                 dto.setCancelReason(payment.getCancelReason());
             });
        
        return dto;
    }


    private AdvertisementImageDto toImageDto(
            AdvertisementImage image) {

        AdvertisementImageDto dto = new AdvertisementImageDto();  
        
        dto.setImageId( image.getImageId() );  
        dto.setAdId( image.getAdvertisement() .getAdId() );  
        dto.setImageType( image.getImageType() .name() );  
        dto.setImageUrl( image.getImageUrl() );

        return dto;
    }
    
    private AdvertisementPaymentDto toPaymentDto(
            AdvertisementPayment payment) {

        AdvertisementPaymentDto dto = new AdvertisementPaymentDto();

        dto.setPaymentId( payment.getPaymentId() );  
        dto.setAdId( payment.getAdvertisement().getAdId() );  
        dto.setAdTitle( payment.getAdvertisement().getTitle() );  
        dto.setAdvertiserId( payment.getAdvertiser().getId() );  
        
        dto.setAdvertiserNickname( payment.getAdvertiser().getNickname() );
        dto.setAdGrade( payment.getAdvertisement().getAdGrade() );
        
        dto.setPaymentType( payment.getPaymentType() );  
        dto.setOrderId( payment.getOrderId() );

        dto.setPaymentKey( payment.getPaymentKey() );  
        dto.setBaseAmount( payment.getBaseAmount() );  
        dto.setPositionAmount( payment.getPositionAmount() );
        dto.setAmount( payment.getAmount() );

        dto.setPosition( payment.getPosition() );  
        dto.setPaymentStatus( payment.getPaymentStatus() );  
        dto.setPaymentMethod( payment.getPaymentMethod() );  
        dto.setRequestedAt( payment.getRequestedAt() );

        dto.setPaidAt( payment.getPaidAt() );  
        dto.setCancelledAt( payment.getCancelledAt() );  
        dto.setCancelReason( payment.getCancelReason() );  
        dto.setPeriodDays( payment.getPeriodDays() );

        dto.setStartDatetime( payment.getStartDatetime() );  
        dto.setEndDatetime( payment.getEndDatetime() );  
        dto.setCreatedAt( payment.getCreatedAt() );

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
    
    // =========================================================
    // 광고 상태 자동 갱신 (스케줄러용)
    // =========================================================
    @Override
    @Transactional
    public void updateAdvertisementStatus() {
        LocalDateTime now = LocalDateTime.now();

        // 1. 대기중(SCHEDULED or PENDING)인 광고 중, 시작 시간이 도래한 것을 OPEN으로 변경
        List<Advertisement> readyAds = advertisementRepository
        		.findByStatusAndPaymentStatusAndStartDatetimeLessThanEqual(
                        AdStatus.PENDING,
                        PaymentStatus.PAID,
                        now
                    );
        
        for (Advertisement ad : readyAds) {
            ad.changeStatus(AdStatus.OPEN);
            
            System.out.println(
                    "[광고 OPEN] 광고 ID="
                    + ad.getAdId()
                    + ", 결제상태="
                    + ad.getPaymentStatus()
            );
        }

        // 2. 진행중(OPEN)인 광고 중, 종료 시간이 지난 것을 CLOSED(마감)로 변경
        List<Advertisement> expiredAds = advertisementRepository
        		.findByStatusAndPaymentStatusAndEndDatetimeLessThanEqual(
                        AdStatus.OPEN,
                        PaymentStatus.PAID,
                        now
                    );
        
        for (Advertisement ad : expiredAds) {
            ad.changeStatus(AdStatus.CLOSED);
            
            System.out.println(
                    "[광고 CLOSED] 광고 ID="
                    + ad.getAdId()
            );
        }
        
        // flush를 통해 DB에 즉시 반영 (선택사항이나 스케줄러 작업 시 권장)
        advertisementRepository.flush();
    }
    
    
    // =========================================================
    // 광고 우선도(피로도) 자동 갱신 (스케줄러용)
    // =========================================================
    @Override
    @Transactional
    public int updatePriorityScore() {

        List<Advertisement> advertisements =
                advertisementRepository.findPriorityUpdateTargets();

        int count = 0;

        for (Advertisement advertisement : advertisements) {

            int score = calculatePriorityScore(advertisement);

            advertisement.updatePriorityScore(score);

            count++;
        }

        return count;
    }
    
    private int calculatePriorityScore(
            Advertisement advertisement) {

        int score;

        // ==========================================
        // 1. 광고 등급 기본 점수
        // ==========================================

        if (advertisement.getAdGrade() == AdGrade.PREMIUM) {
            score = 7;
        } else {
            score = 3;
        }


        // ==========================================
        // 2. CTR 보정
        // ==========================================

        long impressions =
                advertisement.getImpressions() == null
                        ? 0L
                        : advertisement.getImpressions();

        long clicks =
                advertisement.getClicks() == null
                        ? 0L
                        : advertisement.getClicks();


        double ctr = 0.0;

        if (impressions > 0) {
            ctr = (double) clicks / impressions * 100;
        }


        if (ctr >= 5.0) {
            score += 2;
        } else if (ctr >= 2.0) {
            score += 1;
        }


        // ==========================================
        // 3. 피로도 보정
        // ==========================================

        BigDecimal fatigue =
                advertisement.getFatigueScore();

        if (fatigue != null) {

            double fatigueScore =
                    fatigue.doubleValue();

            if (fatigueScore >= 80) {
                score -= 2;
            } else if (fatigueScore >= 50) {
                score -= 1;
            }
        }


        // ==========================================
        // 4. 등급별 범위 제한
        // ==========================================

        if (advertisement.getAdGrade() == AdGrade.PREMIUM) {

            // PREMIUM : 5 ~ 10
            score = Math.max(5, Math.min(score, 10));

        } else {

            // GENERAL : 1 ~ 6
            score = Math.max(1, Math.min(score, 6));
        }


        return score;
    }
    
    private BigDecimal calculateFatigueScore(
            Long adId,
            LocalDate statDate) {

        LocalDateTime start =
                statDate.atStartOfDay();

        LocalDateTime end =
                statDate.plusDays(1).atStartOfDay();

        long impressions =
                impressionLogRepository.countByAdvertisement_AdIdAndViewedAtBetween(
                        adId,
                        start,
                        end
                );

        /*
         * 일일 피로도 계산
         *
         * 현재 기준:
         * 100회 노출당 1점
         * 최대 100점
         */
        BigDecimal fatigueScore =
                BigDecimal.valueOf(impressions)
                        .divide(
                                BigDecimal.valueOf(100),
                                2,
                                RoundingMode.HALF_UP
                        );

        if (fatigueScore.compareTo(BigDecimal.valueOf(100)) > 0) {
            fatigueScore = BigDecimal.valueOf(100);
        }

        return fatigueScore;
    }
    
    
    // =========================================================
    // 30일, 14일 연장메일 발송 (스케줄러용)
    // =========================================================
    @Override
    @Transactional
    public void sendReminderMail() {

        LocalDateTime now = LocalDateTime.now();

        // D-30
        LocalDateTime day30Start =
                now.plusDays(30).withHour(0).withMinute(0).withSecond(0).withNano(0);

        LocalDateTime day30End = day30Start.plusDays(1);

        List<Advertisement> ads30 =
                advertisementRepository.findReminder30Advertisements(
                        day30Start,
                        day30End
                );

        System.out.println("======================================");
        System.out.println("[연장메일 스케줄러 실행]");
        System.out.println("현재시간 : " + now);
        System.out.println("D-30 시작 : " + day30Start);
        System.out.println("D-30 종료 : " + day30End);
        System.out.println("D-30 대상 광고 수 : " + ads30.size());
        System.out.println("======================================");
        
        for (Advertisement ad : ads30) {

        	System.out.println(
        	        "[D-30 메일 발송 대상] "
        	        + "adId=" + ad.getAdId()
        	        + ", title=" + ad.getTitle()
        	        + ", email=" + ad.getAdvertiser().getEmail()
        	        + ", endDatetime=" + ad.getEndDatetime()
        	    );
        	
            AdvertisementDto dto = toDto(ad);

            Member advertiser = ad.getAdvertiser();

            if (advertiser == null || advertiser.getEmail() == null
                    || advertiser.getEmail().isBlank()) {

                System.out.println( "광고주 이메일 없음 - 광고 ID : " + ad.getAdId() );

                continue;
            }

            mailService.sendAdvertisementReminderMail(
                    dto,
                    advertiser.getEmail(),
                    30
            );

            ad.markReminder30dSent();
            
            System.out.println(
                    "[D-30 메일 발송 완료] adId="
                    + ad.getAdId()
            );
        }


        // D-14
        LocalDateTime day14Start =
                now.plusDays(14).withHour(0).withMinute(0).withSecond(0).withNano(0);

        LocalDateTime day14End = day14Start.plusDays(1);

        List<Advertisement> ads14 =
                advertisementRepository.findReminder14Advertisements(
                        day14Start,
                        day14End
                );

        for (Advertisement ad : ads14) {

            AdvertisementDto dto = toDto(ad);

            Member advertiser = ad.getAdvertiser();
            
            System.out.println(
            		"[D-14 메일 발송 대상] "
            				+ "adId=" + ad.getAdId()
            				+ ", title=" + ad.getTitle()
            				+ ", email=" + (
            						advertiser != null
            						? advertiser.getEmail()
            								: "NULL"
            						)
            				+ ", endDatetime=" + ad.getEndDatetime()
            		);

            if (advertiser == null || advertiser.getEmail() == null
                    || advertiser.getEmail().isBlank()) {

                System.out.println( "광고주 이메일 없음 - 광고 ID : " + ad.getAdId() );

                continue;
            }

            mailService.sendAdvertisementReminderMail(
                    dto,
                    advertiser.getEmail(),
                    14
            );

            ad.markReminder14dSent();
            
            System.out.println(
                    "[D-14 메일 발송 완료] adId="
                    + ad.getAdId()
            );
        }
    }
    
    private int calculateFatigue(int recentImpressions) {

        if (recentImpressions >= 10) {
            return 3;
        }

        if (recentImpressions >= 6) {
            return 2;
        }

        if (recentImpressions >= 3) {
            return 1;
        }

        return 0;
    }
    
    private double calculateGradeWeight(Advertisement ad) {

        if (ad.getAdGrade() == AdGrade.PREMIUM) {
            return 1.3;
        }

        return 1.0;
    }
    
    private double calculateFinalScore(
            Advertisement ad,
            int userFatigue
    ) {

        // 광고 자체 우선도
        double priorityWeight = calculatePriorityWeight(ad);

        // PREMIUM 보정
        double gradeWeight = calculateGradeWeight(ad);

        // 사용자 피로도
        double fatigueWeight =calculateFatigueWeight(userFatigue);

        return priorityWeight
                * gradeWeight
                * fatigueWeight;
    }
    
    private double calculateFatigueWeight(int fatigue) {

        return switch (fatigue) {
            case 0 -> 1.0;
            case 1 -> 0.8;
            case 2 -> 0.6;
            case 3 -> 0.3;
            default -> 0.2;
        };
    }
    
    private double calculatePriorityWeight(
            Advertisement ad
    ) {

        int priority =
                ad.getPriorityScore() != null
                        ? ad.getPriorityScore()
                        : 1;

        return Math.min(
                2.0,
                1.0 + (priority * 0.15)
        );
    }
    
    private Advertisement weightedRandomSelect(
            List<AdvertisementScore> scores
    ) {

        double totalScore = scores.stream()
                      .mapToDouble(AdvertisementScore::getScore)
                      .filter(score -> score > 0)
                      .sum();

        // 모든 광고 점수가 0 이하라면 전체 후보 중 랜덤
        if (totalScore <= 0) {

            return scores.get(
                    ThreadLocalRandom.current()
                            .nextInt(scores.size())
            ).getAdvertisement();
        }

        double random =
                ThreadLocalRandom.current()
                        .nextDouble(totalScore);

        double accumulated = 0;

        for (AdvertisementScore score : scores) {

            if (score.getScore() <= 0) {
                continue;
            }

            accumulated += score.getScore();

            if (random <= accumulated) {
                return score.getAdvertisement();
            }
        }

        // 거의 오지 않지만 안전장치
        return scores.stream()
                .filter(item -> item.getScore() > 0)
                .findFirst()
                .map(AdvertisementScore::getAdvertisement)
                .orElse(
                        scores.get(
                                ThreadLocalRandom.current()
                                        .nextInt(scores.size())
                        ).getAdvertisement()
                );
    }
}