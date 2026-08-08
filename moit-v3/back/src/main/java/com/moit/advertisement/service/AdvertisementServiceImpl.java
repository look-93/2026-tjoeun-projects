package com.moit.advertisement.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.moit.advertisement.dto.AdvertisementChartDto;
import com.moit.advertisement.dto.AdvertisementDto;
import com.moit.advertisement.dto.AdvertisementExtensionRequestDto;
import com.moit.advertisement.dto.AdvertisementImageDto;
import com.moit.advertisement.dto.AdvertisementSearchDto;
import com.moit.advertisement.dto.DashboardAiDto;
import com.moit.advertisement.mapper.AdvertisementMapper;
import com.moit.advertisement.type.AdvertisementPosition;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementMapper advertisementMapper;
    private final MailService mailService;
    private final AiSummaryService aiSummaryService;

    private static final String UPLOAD_PATH = "C:/upload/ad";


    // =========================================================
    // 광고 상태 / 우선도
    // =========================================================

    /**
     * 광고 상태 자동 변경
     * PENDING -> OPEN
     * OPEN -> CLOSED
     */
    @Override
    @Transactional
    public void updateAdvertisementStatus() {

        int openCnt = advertisementMapper.updatePendingToOpen();
        int closeCnt = advertisementMapper.updateOpenToClosed();

        System.out.println("OPEN 변경 : " + openCnt);
        System.out.println("CLOSED 변경 : " + closeCnt);
    }


    /**
     * 광고 우선도 점수 갱신
     */
    @Override
    @Transactional
    public int updatePriorityScore() {

        return advertisementMapper.updatePriorityScore();
    }


    // =========================================================
    // 광고 목록
    // =========================================================

    /**
     * 제휴사용자 광고 목록
     */
    @Override
    public List<AdvertisementDto> searchMyAdvertisement(
            AdvertisementSearchDto dto) {

        List<AdvertisementDto> list =
                advertisementMapper.searchMyAdvertisement(dto);

        for (AdvertisementDto ad : list) {

            ad.setImageList(
                    advertisementMapper
                            .selectAdvertisementImageList(ad.getAdId())
            );
        }

        return list;
    }


    /**
     * 제휴사용자 광고 목록 개수
     */
    @Override
    public int selectMyAdvertisementTotalCnt(
            AdvertisementSearchDto dto) {

        return advertisementMapper
                .selectMyAdvertisementTotalCnt(dto);
    }


    /**
     * 관리자 광고 목록
     */
    @Override
    public List<AdvertisementDto> searchByAdmin(
            AdvertisementSearchDto dto) {

        List<AdvertisementDto> list =
                advertisementMapper.searchByAdmin(dto);

        for (AdvertisementDto ad : list) {

            ad.setImageList(
                    advertisementMapper
                            .selectAdvertisementImageList(ad.getAdId())
            );

            AdvertisementDto stat =
                    advertisementMapper
                            .selectAdvertisementStatistics(ad.getAdId());

            if (stat != null) {

                ad.setRecentCtr(stat.getRecentCtr());
                ad.setPreviousCtr(stat.getPreviousCtr());
                ad.setRepeatRate(stat.getRepeatRate());

                calculateFatigue(ad);
            }
        }

        return list;
    }


    /**
     * 관리자 광고 목록 개수
     */
    @Override
    public int selectAdminAdvertisementTotalCnt(
            AdvertisementSearchDto dto) {

        return advertisementMapper
                .selectAdminAdvertisementTotalCnt(dto);
    }


    /**
     * 승인 대기 광고 목록
     */
    @Override
    public List<AdvertisementDto> searchWaitingList(
            AdvertisementSearchDto dto) {

        List<AdvertisementDto> list =
                advertisementMapper.searchWaitingList(dto);

        for (AdvertisementDto ad : list) {

            ad.setImageList(
                    advertisementMapper
                            .selectAdvertisementImageList(ad.getAdId())
            );
        }

        return list;
    }


    /**
     * 승인 대기 광고 개수
     */
    @Override
    public int selectWaitingTotalCnt(
            AdvertisementSearchDto dto) {

        return advertisementMapper.selectWaitingTotalCnt(dto);
    }


    /**
     * 광고 연장 신청 목록
     */
    @Override
    public List<AdvertisementDto> selectExtensionList() {

        return advertisementMapper.selectExtensionList();
    }


    // =========================================================
    // 광고 상세
    // =========================================================

    /**
     * 광고 상세 조회
     */
    @Override
    public AdvertisementDto selectAdvertisementOne(Long adId) {

        AdvertisementDto dto =
                advertisementMapper.selectAdvertisementOne(adId);

        if (dto != null) {

            dto.setImageList(
                    advertisementMapper
                            .selectAdvertisementImageList(adId)
            );
        }

        return dto;
    }


    // =========================================================
    // 광고 등록 / 수정 / 삭제
    // =========================================================

    /**
     * 광고 등록
     */
    @Override
    @Transactional
    public int insertAdvertisement(
            AdvertisementDto dto) {

        return advertisementMapper.insertAdvertisement(dto);
    }


    /**
     * 광고 수정
     *
     * 광고 정보 수정 + 새 이미지가 있으면
     * 기존 이미지 삭제 후 새 이미지 저장
     */
    @Override
    @Transactional
    public int updateAdvertisement(
            AdvertisementDto dto,
            List<MultipartFile> imageFiles,
            List<String> imageTypes) {

        // 광고 정보 수정
        advertisementMapper.updateAdvertisement(dto);

        // 이미지 정보가 없으면 광고 정보만 수정
        if (imageFiles == null || imageTypes == null) {
            return 1;
        }

        boolean hasNewImage =
                imageFiles.stream()
                        .anyMatch(file ->
                                file != null && !file.isEmpty());

        if (!hasNewImage) {
            return 1;
        }

        // 기존 이미지 조회
        List<AdvertisementImageDto> oldImages =
                advertisementMapper
                        .selectAdvertisementImageList(dto.getAdId());

        // 실제 파일 삭제
        for (AdvertisementImageDto image : oldImages) {

            if (image.getImageUrl() == null) {
                continue;
            }

            String fileName =
                    image.getImageUrl()
                            .replace("/upload/ad/", "");

            File oldFile =
                    new File(UPLOAD_PATH, fileName);

            if (oldFile.exists()) {
                oldFile.delete();
            }
        }

        // DB 이미지 삭제
        advertisementMapper
                .deleteAdvertisementImages(dto.getAdId());

        // 업로드 디렉토리 생성
        File dir = new File(UPLOAD_PATH);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 새 이미지 저장
        for (int i = 0; i < imageFiles.size(); i++) {

            MultipartFile file = imageFiles.get(i);

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
                        new File(dir, saveName)
                );

            } catch (IOException e) {

                throw new RuntimeException(
                        "광고 이미지 저장 실패", e
                );
            }

            AdvertisementImageDto imageDto =
                    new AdvertisementImageDto();

            imageDto.setAdId(dto.getAdId());
            imageDto.setImageType(imageTypes.get(i));
            imageDto.setImageUrl(
                    "/upload/ad/" + saveName
            );

            advertisementMapper
                    .insertAdvertisementImage(imageDto);
        }

        return 1;
    }


    /**
     * 광고 삭제
     */
    @Override
    @Transactional
    public int deleteAdvertisement(Long adId) {

        List<AdvertisementImageDto> imageList =
                advertisementMapper
                        .selectAdvertisementImageList(adId);

        // 실제 이미지 파일 삭제
        for (AdvertisementImageDto image : imageList) {

            if (image.getImageUrl() == null) {
                continue;
            }

            String fileName =
                    image.getImageUrl()
                            .replace("/upload/ad/", "");

            File file =
                    new File(UPLOAD_PATH, fileName);

            if (file.exists()) {
                file.delete();
            }
        }

        // DB 이미지 삭제
        advertisementMapper
                .deleteAdvertisementImages(adId);

        // 광고 삭제
        return advertisementMapper
                .deleteAdvertisement(adId);
    }


    // =========================================================
    // 승인 / 상태 / 등급 / 기간
    // =========================================================

    /**
     * 광고 상태 변경
     */
    @Override
    @Transactional
    public int updateAdvertisementStatus(
            AdvertisementDto dto) {

        return advertisementMapper
                .updateAdvertisementStatus(dto);
    }


    /**
     * 광고 승인 / 거절
     */
    @Override
    @Transactional
    public int updateApprovalStatus(
            AdvertisementDto dto) {

        if ("APPROVED".equals(dto.getApprovalStatus())) {

            return advertisementMapper
                    .approveAd(dto);
        }

        if ("REJECTED".equals(dto.getApprovalStatus())) {

            return advertisementMapper
                    .rejectAd(dto);
        }

        throw new IllegalArgumentException(
                "잘못된 승인 상태값입니다."
        );
    }


    /**
     * 광고 등급 변경
     */
    @Override
    @Transactional
    public int updateAdGrade(
    		Long adId,
            String adGrade) {

        return advertisementMapper
                .updateAdGrade(adId, adGrade);
    }


    /**
     * 광고 기간 변경
     */
    @Override
    @Transactional
    public void updatePeriod(
            Long adId,
            LocalDateTime start,
            LocalDateTime end) {

        advertisementMapper.updatePeriod(
                adId,
                start,
                end
        );
    }


    // =========================================================
    // 광고 연장
    // =========================================================

    /**
     * 광고 연장 신청
     */
	/*
	 * @Override
	 * 
	 * @Transactional public void requestExtension( AdvertisementExtensionRequestDto
	 * dto) {
	 * 
	 * AdvertisementDto ad = advertisementMapper
	 * .selectAdvertisementOne(dto.getAdId());
	 * 
	 * if (ad == null) { throw new IllegalArgumentException( "존재하지 않는 광고입니다." ); }
	 * 
	 * if (ad.getAdvertiserId() != dto.getAdvertiserId()) { throw new
	 * IllegalArgumentException( "광고 연장 신청 권한이 없습니다." ); }
	 * 
	 * if (dto.getExtensionRequestEndDatetime() == null) { throw new
	 * IllegalArgumentException( "연장 종료일을 입력해주세요." ); }
	 * 
	 * if (ad.getEndDatetime() == null) { throw new IllegalArgumentException(
	 * "기존 광고 종료일이 없습니다." ); }
	 * 
	 * if (dto.getExtensionRequestEndDatetime() .isBefore(ad.getEndDatetime())) {
	 * 
	 * throw new IllegalArgumentException( "기존 종료일 이후의 날짜만 신청할 수 있습니다." ); }
	 * 
	 * advertisementMapper.requestExtension(dto); }
	 */

    /**
     * 광고 연장 승인 상태 변경
     */
	/*
	 * @Override
	 * 
	 * @Transactional public void updateExtensionApprove( AdvertisementDto dto) {
	 * 
	 * advertisementMapper .updateExtensionApprove(dto); }
	 */


    // =========================================================
    // 이미지
    // =========================================================

    /**
     * 광고 이미지 등록
     */
    @Override
    @Transactional
    public int insertAdvertisementImage(
            AdvertisementImageDto dto) {

        return advertisementMapper
                .insertAdvertisementImage(dto);
    }


    /**
     * 광고 이미지 조회
     */
    @Override
    public List<AdvertisementImageDto>
            selectAdvertisementImageList(Long adId) {

        return advertisementMapper
                .selectAdvertisementImageList(adId);
    }


    /**
     * 광고 이미지 전체 삭제
     */
    @Override
    @Transactional
    public int deleteAdvertisementImage(Long adId) {

        return advertisementMapper
                .deleteAdvertisementImages(adId);
    }


    // =========================================================
    // 광고 노출 / 클릭
    // =========================================================

    /**
     * 광고 노출 수 증가
     */
    @Override
    @Transactional
    public int updateImpressions(Long adId) {

        return advertisementMapper
                .updateImpressions(adId);
    }


    /**
     * 광고 클릭 수 증가
     */
    @Override
    @Transactional
    public int updateAdvertisementClick(Long adId) {

        return advertisementMapper
                .updateAdvertisementClick(adId);
    }


    /**
     * 사용자 광고 1건 조회
     */
    @Override
    public AdvertisementDto selectTopAdvertisement(
            String position,
            Integer memberId,
            String sessionId) {

        return advertisementMapper
                .selectTopAdvertisement(
                        position,
                        memberId,
                        sessionId
                );
    }


    // =========================================================
    // 기본 통계
    // =========================================================

    @Override
    public int selectTotalAdvertisementCnt() {
        return advertisementMapper
                .selectTotalAdvertisementCnt();
    }


    @Override
    public int selectOpenAdvertisementCnt() {
        return advertisementMapper
                .selectOpenAdvertisementCnt();
    }


    @Override
    public int selectPendingAdvertisementCnt() {
        return advertisementMapper
                .selectPendingAdvertisementCnt();
    }


    @Override
    public int selectClosedAdvertisementCnt() {
        return advertisementMapper
                .selectClosedAdvertisementCnt();
    }


    // =========================================================
    // 클릭 로그
    // =========================================================

    @Override
    @Transactional
    public boolean insertClickLog(
    		Long adId,
            String position,
            HttpServletRequest request,
            HttpSession session) {

        Integer loginMemberId =
                (Integer) session
                        .getAttribute("loginMemberId");

        String ip =
                request.getRemoteAddr();

        String userAgent =
                request.getHeader("User-Agent");

        String deviceType =
                getDeviceType(userAgent);

        int count =
                advertisementMapper
                        .checkDuplicateClick(
                                adId,
                                loginMemberId,
                                ip
                        );

        // 최근 1시간 이내 클릭 기록이 있으면 저장하지 않음
        if (count > 0) {
            return false;
        }

        String sessionId =
                request.getSession().getId();

        String referrer =
                request.getHeader("Referer");

        advertisementMapper.insertClickLog(
                adId,
                loginMemberId,
                deviceType,
                ip,
                sessionId,
                referrer,
                position
        );

        // 로그인 회원이면 포인트 지급
        if (loginMemberId != null) {

            int pointCount =
                    advertisementMapper
                            .checkAdvertisementPoint(
                                    loginMemberId
                            );

            if (pointCount == 0) {

                advertisementMapper
                        .updateMemberPoint(
                                loginMemberId
                        );

                advertisementMapper
                        .insertPointHistory(
                                loginMemberId
                        );
            }
        }

        return true;
    }


    // =========================================================
    // 노출 로그
    // =========================================================

    @Override
    @Transactional
    public boolean insertImpressionLog(
    		Long adId,
            String position,
            HttpServletRequest request,
            HttpSession session) {

        Integer memberId =
                (Integer) session
                        .getAttribute("loginMemberId");

        String ip =
                request.getRemoteAddr();

        String sessionId =
                request.getSession().getId();

        String deviceType =
                getDeviceType(
                        request.getHeader("User-Agent")
                );

        int count =
                advertisementMapper
                        .checkDuplicateImpression(
                                adId,
                                memberId,
                                sessionId
                        );

        if (count > 0) {
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


    /**
     * User-Agent 기반 디바이스 구분
     */
    private String getDeviceType(String userAgent) {

        if (userAgent == null) {
            return "PC";
        }

        String ua =
                userAgent.toLowerCase();

        if (ua.contains("ipad")
                || ua.contains("tablet")) {

            return "TABLET";
        }

        if (ua.contains("mobile")
                || ua.contains("android")
                || ua.contains("iphone")) {

            return "MOBILE";
        }

        return "PC";
    }


    // =========================================================
    // 일일 통계
    // =========================================================

    @Override
    @Transactional
    public void insertDailyStatistics() {

        int result =
                advertisementMapper
                        .insertDailyStatistics();

        System.out.println(
                "================================="
        );
        System.out.println(
                "광고 일일통계 저장 건수 : " + result
        );
        System.out.println(
                "================================="
        );
    }


    // =========================================================
    // 광고 통계 차트
    // =========================================================

    @Override
    public AdvertisementChartDto selectSummary() {

        return advertisementMapper
                .selectSummary();
    }


    @Override
    public List<AdvertisementChartDto> selectDailyChart() {

        return advertisementMapper
                .selectDailyChart();
    }


    @Override
    public List<AdvertisementChartDto> selectTopCtrChart() {

        return advertisementMapper
                .selectTopCtrChart();
    }


    @Override
    public List<AdvertisementChartDto> selectGradeChart() {

        return advertisementMapper
                .selectGradeChart();
    }


    /**
     * 위치별 노출 통계
     *
     * DB에 데이터가 없는 위치도 0으로 반환
     */
    @Override
    public List<AdvertisementChartDto>
            selectPositionChart() {

        List<AdvertisementChartDto> dbList =
                advertisementMapper
                        .selectPositionChart();

        return Arrays.stream(
                    AdvertisementPosition.values()
                )
                .map(position ->
                        dbList.stream()
                                .filter(dto ->
                                        position.name()
                                                .equals(dto.getPosition())
                                )
                                .findFirst()
                                .orElseGet(() -> {

                                    AdvertisementChartDto empty =
                                            new AdvertisementChartDto();

                                    empty.setPosition(
                                            position.name()
                                    );

                                    empty.setImpressions(0);

                                    return empty;
                                })
                )
                .toList();
    }


    /**
     * 광고 연장률
     */
    @Override
    public double selectExtensionRate() {

        return advertisementMapper
                .selectExtensionRate();
    }


    /**
     * 위치별 CTR
     *
     * DB에 데이터가 없는 위치도 0으로 반환
     */
    @Override
    public List<AdvertisementChartDto>
            selectPositionCtrChart() {

        List<AdvertisementChartDto> dbList =
                advertisementMapper
                        .selectPositionCtrChart();

        return Arrays.stream(
                    AdvertisementPosition.values()
                )
                .map(position ->
                        dbList.stream()
                                .filter(dto ->
                                        position.name()
                                                .equals(dto.getPosition())
                                )
                                .findFirst()
                                .orElseGet(() -> {

                                    AdvertisementChartDto empty =
                                            new AdvertisementChartDto();

                                    empty.setPosition(
                                            position.name()
                                    );

                                    empty.setCtr(0.0);

                                    return empty;
                                })
                )
                .toList();
    }


    // =========================================================
    // AI 대시보드
    // =========================================================

    @Override
    public DashboardAiDto getDashboardAiData() {

        DashboardAiDto dto =
                new DashboardAiDto();

        // 1. 전체 통계
        AdvertisementChartDto chartSummary =
                advertisementMapper.selectSummary();

        if (chartSummary != null) {

            dto.setTotalAd(
                    chartSummary.getTotalAd()
            );

            dto.setTotalImp(
                    chartSummary.getTotalImp()
            );

            dto.setTotalClick(
                    chartSummary.getTotalClick()
            );

            dto.setAvgCtr(
                    chartSummary.getAvgCtr()
            );
        }

        // 2. 연장률
        dto.setExtensionRate(
                advertisementMapper
                        .selectExtensionRate()
        );

        // 3. 위치별 CTR
        List<AdvertisementChartDto> ctrList =
                advertisementMapper
                        .selectPositionCtrChart();

        if (ctrList != null && !ctrList.isEmpty()) {

            ctrList.sort(
                    Comparator.comparing(
                            AdvertisementChartDto::getCtr,
                            Comparator.nullsFirst(
                                    Comparator.naturalOrder()
                            )
                    )
            );

            dto.setWorstPosition(
                    ctrList.get(0).getPosition()
            );

            dto.setBestPosition(
                    ctrList.get(
                            ctrList.size() - 1
                    ).getPosition()
            );
        }

        // 4. 가장 많이 사용되는 광고 등급
        List<AdvertisementChartDto> gradeList =
                advertisementMapper
                        .selectGradeChart();

        if (gradeList != null
                && !gradeList.isEmpty()) {

            gradeList.sort(
                    Comparator.comparing(
                            AdvertisementChartDto::getCount,
                            Comparator.nullsFirst(
                                    Comparator.reverseOrder()
                            )
                    )
            );

            dto.setTopGrade(
                    gradeList.get(0).getAdGrade()
            );
        }

        // 5. 광고 피로도 경고 개수
        AdvertisementSearchDto searchDto =
                new AdvertisementSearchDto();

        searchDto.setPage(1);
        searchDto.setSize(1000);

        List<AdvertisementDto> adList =
                advertisementMapper
                        .searchByAdmin(searchDto);

        int warningCount = 0;

        if (adList != null) {

            for (AdvertisementDto ad : adList) {

                AdvertisementDto stat =
                        getAdvertisementStatistics(
                                ad.getAdId()
                        );

                if (stat != null
                        && "교체 권장".equals(
                                stat.getFatigueStatus()
                        )) {

                    warningCount++;
                }
            }
        }

        dto.setFatigueWarningCount(
                warningCount
        );

        // 6. AI 요약 생성
        String summary =
                aiSummaryService.createSummary(dto);

        dto.setSummary(summary);

        dto.setCreatedAt(
                LocalDateTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "yyyy-MM-dd HH:mm:ss"
                                )
                        )
        );

        return dto;
    }


    /**
     * AI 요약 저장
     */
    @Override
    @Transactional
    public void saveAiSummary(String summary) {

        advertisementMapper
                .insertAiSummary(summary);
    }


    /**
     * 가장 최근 AI 요약 조회
     */
    @Override
    public DashboardAiDto getLatestAiSummary() {

        return advertisementMapper
                .selectLatestAiSummary();
    }


    // =========================================================
    // 광고 피로도
    // =========================================================

    @Override
    public AdvertisementDto
            getAdvertisementStatistics(Long adId) {

        AdvertisementDto dto =
                advertisementMapper
                        .selectAdvertisementStatistics(adId);

        if (dto == null) {
            return null;
        }

        calculateFatigue(dto);

        return dto;
    }


    /**
     * 광고 피로도 계산
     */
    private void calculateFatigue(
            AdvertisementDto dto) {

        double recentCtr =
                dto.getRecentCtr() == null
                        ? 0
                        : dto.getRecentCtr();

        double previousCtr =
                dto.getPreviousCtr() == null
                        ? 0
                        : dto.getPreviousCtr();

        double repeatRate =
                dto.getRepeatRate() == null
                        ? 0
                        : dto.getRepeatRate();

        double decrease = 0;

        if (previousCtr > 0) {

            decrease =
                    ((previousCtr - recentCtr)
                            / previousCtr)
                            * 100;
        }

        double roundedDecrease =
                Math.round(decrease * 100)
                        / 100.0;

        dto.setCtrDecrease(
                roundedDecrease
        );

        double score =
                decrease * 0.6
                + repeatRate * 0.4;

        double roundedScore =
                Math.round(score * 100)
                        / 100.0;

        dto.setFatigueScore(
                roundedScore
        );

        if (score >= 70) {

            dto.setFatigueStatus(
                    "교체 권장"
            );

        } else if (score >= 40) {

            dto.setFatigueStatus(
                    "관심"
            );

        } else {

            dto.setFatigueStatus(
                    "정상"
            );
        }
    }


    // =========================================================
    // 광고 만료 알림 메일
    // =========================================================

    @Override
    @Transactional
    public void sendReminderMail() {

        // -----------------------------
        // 30일 전
        // -----------------------------

        List<AdvertisementDto> reminder30 =
                advertisementMapper
                        .selectReminder30List();

        System.out.println(
                "30일 만료 알림 대상 = "
                        + reminder30.size()
        );

        for (AdvertisementDto ad : reminder30) {

            mailService
                    .sendAdvertisementReminderMail(
                            ad,
                            30
                    );

            advertisementMapper
                    .updateReminder30Sent(
                            ad.getAdId()
                    );
        }


        // -----------------------------
        // 14일 전
        // -----------------------------

        List<AdvertisementDto> reminder14 =
                advertisementMapper
                        .selectReminder14List();

        System.out.println(
                "14일 만료 알림 대상 = "
                        + reminder14.size()
        );

        for (AdvertisementDto ad : reminder14) {

            mailService
                    .sendAdvertisementReminderMail(
                            ad,
                            14
                    );

            advertisementMapper
                    .updateReminder14Sent(
                            ad.getAdId()
                    );
        }
    }


	@Override
	public void updateExtensionApprove(AdvertisementDto dto) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void requestExtension(AdvertisementExtensionRequestDto dto) {
		// TODO Auto-generated method stub
		
	}
}
