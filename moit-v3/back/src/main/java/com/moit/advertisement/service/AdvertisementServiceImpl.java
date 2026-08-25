package com.moit.advertisement.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
import com.moit.advertisement.enums.PaymentHistoryStatus;
import com.moit.advertisement.enums.PaymentStatus;
import com.moit.advertisement.enums.PaymentType;
import com.moit.advertisement.repository.AdvertisementClickLogRepository;
import com.moit.advertisement.repository.AdvertisementImageRepository;
import com.moit.advertisement.repository.AdvertisementImpressionLogRepository;
import com.moit.advertisement.repository.AdvertisementPaymentRepository;
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
    private final AdvertisementCalculationService calculationService;
    
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
                        .build();

        advertisementRepository.save(advertisement);
        
        // =========================================================
        // 광고가 등록될 때 결제 대기(Payment) 데이터 미리 생성
        // =========================================================
        String generatedOrderId = "AD_" + advertisement.getAdId() + "_" + System.currentTimeMillis();

        AdvertisementPayment payment = AdvertisementPayment.builder()
                .advertisement(advertisement)
                .advertiser(advertiser)
                .paymentType(PaymentType.INITIAL) // 최초 결제
                .orderId(generatedOrderId)         // 토스가 검증할 주문번호
                .baseAmount(dto.getTotalBudget())  // (또는 기본금과 위치 추가금 분리해서 세팅)
                .positionAmount(BigDecimal.ZERO)
                .amount(dto.getTotalBudget())      // 최종 결제 금액
                .position(AdPosition.MAIN)         // 대표 위치 (또는 선택된 위치)
                .paymentStatus(PaymentHistoryStatus.REQUESTED) // 대기 상태
                .periodDays(30)                    // 기간 계산된 일수 (필요시 세팅)
                .startDatetime(dto.getStartDatetime())
                .endDatetime(dto.getEndDatetime())
                .build();

        advertisementPaymentRepository.save(payment);

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

            // 광고 승인
            advertisement.approve(admin);

            // 광고주 결제 요청 메일 발송
            Member advertiser = advertisement.getAdvertiser();

            if (advertiser != null) {

                String advertiserEmail = advertiser.getEmail();

                AdvertisementDto adDto = toDto(advertisement);

                mailService.sendAdvertisementPaymentRequestMail(
                        adDto,
                        advertiserEmail
                );
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
        dto.setTotalBudget( ad.getTotalBudget() );  
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
        if (ad.getStartDatetime() != null
                && ad.getEndDatetime() != null
                && ad.getAdGrade() != null
                && ad.getPendingPaymentType() != null) {

            List<AdPosition> positions =
                    imageList.stream()
                            .map(AdvertisementImageDto::getImageType)
                            .filter(type -> type != null)
                            .map(AdPosition::valueOf)
                            .toList();

            AdvertisementCalculationResultDto calculation =
                    calculationService.calculate(
                            ad.getStartDatetime(),
                            ad.getEndDatetime(),
                            ad.getAdGrade(),
                            ad.getPendingPaymentType(),
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
        // (본인의 AdStatus enum에 있는 대기 상태 이름에 맞춰주세요. 예: SCHEDULED, PENDING, READY 등)
        List<Advertisement> readyAds = advertisementRepository
                .findByStatusAndStartDatetimeLessThanEqual(AdStatus.PENDING, now);
        
        for (Advertisement ad : readyAds) {
            ad.changeStatus(AdStatus.OPEN);
        }

        // 2. 진행중(OPEN)인 광고 중, 종료 시간이 지난 것을 CLOSED(마감)로 변경
        List<Advertisement> expiredAds = advertisementRepository
                .findByStatusAndEndDatetimeLessThanEqual(AdStatus.OPEN, now);
        
        for (Advertisement ad : expiredAds) {
            ad.changeStatus(AdStatus.CLOSED);
        }
        
        // flush를 통해 DB에 즉시 반영 (선택사항이나 스케줄러 작업 시 권장)
        advertisementRepository.flush();
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

        for (Advertisement ad : ads30) {

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
        }
    }
}