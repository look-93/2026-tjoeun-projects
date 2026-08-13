package com.moit.advertisement.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moit.advertisement.dto.AdvertisementChartDto;
import com.moit.advertisement.dto.AdvertisementDto;
import com.moit.advertisement.dto.AdvertisementSearchDto;
//import com.moit.advertisement.dto.DashboardAiDto;
import com.moit.advertisement.enums.ApprovalStatus;
import com.moit.advertisement.service.AdvertisementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/advertisement")
@Tag(
    name = "Admin Advertisement",
    description = "관리자 광고 관리 API"
)
public class AdvertisementAdminController {

    private final AdvertisementService advertisementService;
    
    // JWT 적용 후 로그인 사용자 ID로 변경
    private static final Long LOGIN_ADMIN_ID = 1L;
    
    // =========================================================
    // 승인 대기 목록
    // GET /api/admin/advertisement/approval
    // =========================================================
    @Operation(
	    summary = "광고 승인 대기 목록 조회",
	    description = "관리자가 승인 대기 중인 광고 목록을 조회합니다."
	)
    @GetMapping("/approval")
    public ResponseEntity<AdvertisementDto.AdvertisementPageResponseDto> approvalList(
            AdvertisementSearchDto dto) {

        dto.setPage(dto.getPage() <= 0 ? 1 : dto.getPage());
        dto.setSize(dto.getSize() <= 0 ? 10 : dto.getSize());

        dto.setApprovalStatus(ApprovalStatus.WAITING);

        List<AdvertisementDto> list =
                advertisementService.searchWaitingList(dto);

        int totalCnt =
                advertisementService.selectWaitingTotalCnt(dto);

        return ResponseEntity.ok(
                new AdvertisementDto.AdvertisementPageResponseDto(
                        list,
                        totalCnt,
                        dto.getPage(),
                        dto.getSize()
                )
        );
    }

    // =========================================================
    // 광고 관리 목록
    // GET /api/admin/advertisement
    //
    // tab = approval
    // tab = manage
    // =========================================================
    @Operation(
	    summary = "광고 관리 목록 조회",
	    description = "관리자가 승인 대기 또는 승인 완료된 광고 목록을 조회합니다."
	)
    @GetMapping
    public ResponseEntity<List<AdvertisementDto>> manageList(
            @RequestParam(required = false, defaultValue = "approval")
            String tab,

            AdvertisementSearchDto dto) {

        dto.setPage(dto.getPage() <= 0 ? 1 : dto.getPage());
        dto.setSize(dto.getSize() <= 0 ? 10 : dto.getSize());

        List<AdvertisementDto> list;

        if ("approval".equals(tab)) {

            dto.setApprovalStatus(ApprovalStatus.WAITING);

            list = advertisementService.searchWaitingList(dto);

        } else {

            dto.setApprovalStatus(ApprovalStatus.APPROVED);

            list = advertisementService.searchByAdmin(dto);
        }

        return ResponseEntity.ok(list);
    }


    // =========================================================
    // 광고 관리 목록 총 개수
    // GET /api/admin/advertisement/count
    // =========================================================
    @Operation(
	    summary = "광고 관리 목록 개수 조회",
	    description = "승인 대기 또는 승인 완료 광고의 전체 개수를 조회합니다."
	)
    @GetMapping("/count")
    public ResponseEntity<Long> manageCount(
            @RequestParam(required = false, defaultValue = "approval")
            String tab,

            AdvertisementSearchDto dto) {

        long totalCnt;

        if ("approval".equals(tab)) {

            dto.setApprovalStatus(ApprovalStatus.WAITING);

            totalCnt = advertisementService.selectWaitingTotalCnt(dto);

        } else {

            dto.setApprovalStatus(ApprovalStatus.APPROVED);

            totalCnt = advertisementService.selectAdminAdvertisementTotalCnt(dto);
        }

        return ResponseEntity.ok(totalCnt);
    }

    
    // =========================================================
    // 광고 상세
    // GET /api/admin/advertisement/{adId}
    // =========================================================
    @Operation(
	    summary = "광고 상세 조회",
	    description = "광고 ID를 이용하여 광고 상세 정보를 조회합니다."
	)
    @GetMapping("/{adId}")
    public ResponseEntity<AdvertisementDto> detail(
            @PathVariable Long adId) {

        AdvertisementDto dto =
                advertisementService.selectAdvertisementOne(adId);

        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dto);
    }

    // =========================================================
    // 광고 승인
    // PATCH /api/admin/advertisement/{adId}/approve
    // =========================================================
    @Operation(
	    summary = "광고 승인",
	    description = "관리자가 광고를 승인합니다."
	)
	@PatchMapping("/{adId}/approve")
	public ResponseEntity<Void> approve(
	        @PathVariable Long adId) {

	    AdvertisementDto.AdvertisementAdminUpdateDto dto =
	            new AdvertisementDto.AdvertisementAdminUpdateDto();

	    dto.setAdId(adId);
	    dto.setApprovalStatus("APPROVED");
	    dto.setStatus("PENDING");
	    dto.setApprovedBy(LOGIN_ADMIN_ID);
	    dto.setApprovedAt(LocalDateTime.now());

	    advertisementService.updateApprovalStatus(dto);

	    return ResponseEntity.ok().build();
	}

    // =========================================================
    // 광고 반려
    // PATCH /api/admin/advertisement/{adId}/reject
    // =========================================================
    @Operation(
	    summary = "광고 반려",
	    description = "관리자가 광고를 반려하고 반려 사유를 저장합니다."
	)
    @PatchMapping("/{adId}/reject")
    public ResponseEntity<Void> reject(
            @PathVariable Long adId,
            @RequestParam String rejectReason) {

        AdvertisementDto.AdvertisementAdminUpdateDto dto =
                new AdvertisementDto.AdvertisementAdminUpdateDto();

        dto.setAdId(adId);
        dto.setApprovalStatus("REJECTED");

        dto.setApprovedBy(LOGIN_ADMIN_ID);
        dto.setRejectReason(rejectReason);
        dto.setApprovedAt(LocalDateTime.now());

        advertisementService.updateApprovalStatus(dto);

        return ResponseEntity.ok().build();
    }

    // =========================================================
    // 광고 상태 변경
    // PATCH /api/admin/advertisement/{adId}/status
    // =========================================================
    @Operation(
	    summary = "광고 상태 변경",
	    description = "관리자가 광고의 상태를 변경합니다."
	)
    @PatchMapping("/{adId}/status")
    public ResponseEntity<Void> status(
            @PathVariable Long adId,
            @RequestParam String status) {

        AdvertisementDto.AdvertisementAdminUpdateDto dto =
                new AdvertisementDto.AdvertisementAdminUpdateDto();

        dto.setAdId(adId);
        dto.setStatus(status);

        dto.setStatusUpdatedBy(LOGIN_ADMIN_ID);
        dto.setStatusUpdatedAt(LocalDateTime.now());

        advertisementService.updateAdvertisementStatus(dto);

        return ResponseEntity.ok().build();
    }
    
    // =========================================================
    // 광고 등급 변경
    // PATCH /api/admin/advertisement/{adId}/grade
    // =========================================================
    @Operation(
	    summary = "광고 등급 변경",
	    description = "광고의 일반/프리미엄 등급을 변경합니다."
	)
    @PatchMapping("/{adId}/grade")
    public ResponseEntity<Void> updateGrade(
            @PathVariable Long adId,
            @RequestParam String adGrade) {

        advertisementService.updateAdGrade(
                adId,
                adGrade
        );

        return ResponseEntity.ok().build();
    }
    
    // =========================================================
    // 광고 기간 변경
    // PATCH /api/admin/advertisement/{adId}/period
    // =========================================================
    @Operation(
	    summary = "광고 기간 변경",
	    description = "광고의 시작일과 종료일을 변경합니다."
	)
    @PatchMapping("/{adId}/period")
    public ResponseEntity<Void> updatePeriod(
            @PathVariable Long adId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate start,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate end) {

        advertisementService.updatePeriod(
                adId,
                start.atStartOfDay(),
                end.atStartOfDay()
        );

        return ResponseEntity.ok().build();
    }
    
    // =========================================================
    // 광고 대시보드
    // =========================================================

    @GetMapping("/statistics")
    public ResponseEntity<?> statistics() {

        return ResponseEntity.ok(
                new Object() {

                    public final long totalAdCnt =
                            advertisementService
                                    .selectTotalAdvertisementCnt();

                    public final long openCnt =
                            advertisementService
                                    .selectOpenAdvertisementCnt();

                    public final long pendingCnt =
                            advertisementService
                                    .selectPendingAdvertisementCnt();

                    public final long closedCnt =
                            advertisementService
                                    .selectClosedAdvertisementCnt();
                }
        );
    }
    // =========================================================
    // 총 통계
    // GET /api/admin/advertisement/statistics/summary
    // =========================================================
    @Operation(
	    summary = "광고 통계 요약 조회",
	    description = "광고 전체 노출수, 클릭수 등의 요약 통계를 조회합니다."
	)
    @GetMapping("/statistics/summary")
    public ResponseEntity<AdvertisementChartDto> summary() {

        return ResponseEntity.ok(
                advertisementService.selectSummary()
        );
    }


    // =========================================================
    // 일일 통계
    // GET /api/admin/advertisement/statistics/daily
    // =========================================================
    @Operation(
	    summary = "광고 일일 통계 조회",
	    description = "날짜별 광고 노출 및 클릭 통계를 조회합니다."
	)
    @GetMapping("/statistics/daily")
    public ResponseEntity<List<AdvertisementChartDto>> dailyChart() {

        return ResponseEntity.ok(
                advertisementService.selectDailyChart()
        );
    }


    // =========================================================
    // CTR TOP 5
    // GET /api/admin/advertisement/statistics/ctr
    // =========================================================
    @Operation(
	    summary = "광고 CTR TOP 5 조회",
	    description = "CTR이 높은 광고 상위 5개를 조회합니다."
	)
    @GetMapping("/statistics/ctr")
    public ResponseEntity<List<AdvertisementChartDto>> ctrChart() {

        return ResponseEntity.ok(
                advertisementService.selectTopCtrChart()
        );
    }


    // =========================================================
    // 광고 등급 비율
    // GET /api/admin/advertisement/statistics/grade
    // =========================================================
    @Operation(
	    summary = "광고 등급별 비율 조회",
	    description = "전체 광고의 일반(GENERAL) 및 프리미엄(PREMIUM) 등급별 비율을 조회합니다."
	)
    @GetMapping("/statistics/grade")
    public ResponseEntity<List<AdvertisementChartDto>> gradeChart() {

        return ResponseEntity.ok(
                advertisementService.selectGradeChart()
        );
    }


    // =========================================================
    // 위치별 노출
    // GET /api/admin/advertisement/statistics/position
    // =========================================================
    @Operation(
	    summary = "광고 위치별 노출 통계 조회",
	    description = "광고가 노출된 위치별 노출 통계 데이터를 조회합니다."
	)
    @GetMapping("/statistics/position")
    public ResponseEntity<List<AdvertisementChartDto>> positionChart() {

        return ResponseEntity.ok(
                advertisementService.selectPositionChart()
        );
    }


    // =========================================================
    // 연장률
    // GET /api/admin/advertisement/statistics/extension-rate
    // =========================================================
    @Operation(
	    summary = "광고 연장률 조회",
	    description = "전체 광고의 기간 연장 비율을 조회합니다."
	)
    @GetMapping("/statistics/extension-rate")
    public ResponseEntity<Double> extensionRate() {

        return ResponseEntity.ok(
                advertisementService.selectExtensionRate()
        );
    }


    // =========================================================
    // 위치별 CTR
    // GET /api/admin/advertisement/statistics/position-ctr
    // =========================================================
    @Operation(
	    summary = "광고 위치별 CTR 조회",
	    description = "광고 노출 위치별 클릭률(CTR) 통계를 조회합니다."
	)
    @GetMapping("/statistics/position-ctr")
    public ResponseEntity<List<AdvertisementChartDto>> positionCtrChart() {

        return ResponseEntity.ok(
                advertisementService.selectPositionCtrChart()
        );
    }


//    // =========================================================
//    // AI 통계 요약
//    // GET /api/admin/advertisement/statistics/ai-summary
//    // =========================================================
//
//    @GetMapping("/statistics/ai-summary")
//    public ResponseEntity<DashboardAiDto> aiSummary() {
//
//        DashboardAiDto dto =
//                advertisementService.getLatestAiSummary();
//
//        if (dto == null) {
//
//            dto = new DashboardAiDto();
//
//            dto.setSummary("아직 생성된 AI 분석이 없습니다.");
//            dto.setCreatedAt("-");
//        }
//
//        return ResponseEntity.ok(dto);
//    }
}