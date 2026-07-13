package com.moit.advertisement.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moit.advertisement.dto.AdvertisementChartDto;
import com.moit.advertisement.dto.AdvertisementDto;
import com.moit.advertisement.dto.AdvertisementSearchDto;
import com.moit.advertisement.dto.DashboardAiDto;
import com.moit.advertisement.service.AdvertisementService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/advertisement")
public class AdvertisementAdminController {

    private final AdvertisementService advertisementService;

    // 스케줄러
    @EnableScheduling
    @SpringBootApplication
    public class MoitApplication {

    }
    
    // 승인 대기 목록
    @GetMapping("/approvalList")
    public String approvalList(AdvertisementSearchDto dto, Model model) {

        dto.setPage(dto.getPage() <= 0 ? 1 : dto.getPage());
        dto.setSize(dto.getSize() <= 0 ? 10 : dto.getSize());

        dto.setApprovalStatus("WAITING");

        List<AdvertisementDto> list =
                advertisementService.searchWaitingList(dto);

        int totalCnt =
                advertisementService.selectWaitingTotalCnt(dto);

        model.addAttribute("list", list);
        model.addAttribute("dto", dto);
        model.addAttribute("totalCnt", totalCnt);

        return "admin/advertisement/approvalList";
    }

    // 광고 관리 목록
    @GetMapping("/manageList")
    public String manageList(
            @RequestParam(required = false, defaultValue = "approval") String tab,
            AdvertisementSearchDto dto,
            Model model) {

        dto.setPage(dto.getPage() <= 0 ? 1 : dto.getPage());
        dto.setSize(dto.getSize() <= 0 ? 10 : dto.getSize());

        List<AdvertisementDto> list;
        int totalCnt;

        if ("approval".equals(tab)) {

            dto.setApprovalStatus("WAITING");

            list = advertisementService.searchWaitingList(dto);
            totalCnt = advertisementService.selectWaitingTotalCnt(dto);

            model.addAttribute("waitingCnt", totalCnt);

        } 
        else if ("extension".equals(tab)) {


            list = advertisementService.selectExtensionList();

            totalCnt = list.size();


        } else {

            dto.setApprovalStatus("APPROVED");

            list = advertisementService.searchByAdmin(dto);
            totalCnt = advertisementService.selectAdminAdvertisementTotalCnt(dto);
        }

        int totalPage = (int) Math.ceil((double) totalCnt / dto.getSize());
        
     // 데이터가 없어서 totalPage가 0이 나오더라도 최소 1페이지로 고정
        if (totalPage == 0) { totalPage = 1; }
        
     // 페이지 블럭 (10개 단위)
        int pageBlock = 10;

        int startPage = ((dto.getPage() - 1) / pageBlock) * pageBlock + 1;

        int endPage = Math.min(startPage + pageBlock - 1, totalPage);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("list", list);
        model.addAttribute("dto", dto);
        model.addAttribute("search", dto);
        model.addAttribute("totalCnt", totalCnt);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("tab", tab);

        model.addAttribute("totalAdCnt", advertisementService.selectTotalAdvertisementCnt());
        model.addAttribute("openCnt", advertisementService.selectOpenAdvertisementCnt());
        model.addAttribute("pendingCnt", advertisementService.selectPendingAdvertisementCnt());
        model.addAttribute("closedCnt", advertisementService.selectClosedAdvertisementCnt());

        model.addAttribute("now", LocalDateTime.now());

        return "admin/advertisement/manageList";
    }

    
    // 🔥 통합 상세 페이지 
    @GetMapping("/detail")
    public String detail(@RequestParam int adId,
                         @RequestParam String mode,
                         Model model) {

        AdvertisementDto dto =
                advertisementService.selectAdvertisementOne(adId);

        if (dto == null) {
            return "redirect:/admin/advertisement/manageList";
        }

        model.addAttribute("dto", dto);
        model.addAttribute("mode", mode);

        return "admin/advertisement/detail";
    }

    // 게시 승인 처리
    @PostMapping("/approve")
    public String approve(@RequestParam int adId,
                          HttpSession session) {

        Integer loginMemberId = getLogin(session);

        AdvertisementDto dto = new AdvertisementDto();
        dto.setAdId(adId);
        dto.setApprovalStatus("APPROVED");
        dto.setStatus("PENDING");
        dto.setApprovedBy(loginMemberId);
        dto.setApprovedAt(LocalDateTime.now());

        advertisementService.updateApprovalStatus(dto);

        return "redirect:/admin/advertisement/manageList?tab=approval";
    }
    // 연장 승인
    @PostMapping("/extensionApprove")
    public String extensionApprove(
            @RequestParam int adId) {


        AdvertisementDto dto = new AdvertisementDto();

        dto.setAdId(adId);


        advertisementService.updateExtensionApprove(dto);


        return "redirect:/admin/advertisement/manageList?tab=extension";
    }

    // 반려 처리
    @PostMapping("/reject")
    public String reject(@RequestParam int adId,
                         @RequestParam String rejectReason,
                         HttpSession session) {

        Integer loginMemberId = getLogin(session);

        AdvertisementDto dto = new AdvertisementDto();
        dto.setAdId(adId);
        dto.setApprovalStatus("REJECTED");
        dto.setApprovedBy(loginMemberId);
        dto.setRejectReason(rejectReason);
        dto.setApprovedAt(LocalDateTime.now());

        advertisementService.updateApprovalStatus(dto);

        return "redirect:/admin/advertisement/manageList?tab=approval";
    }

    // 목록 상태 변경 
    @PostMapping("/status")
    public String status(@RequestParam int adId,
                         @RequestParam String status,
                         HttpSession session) {

        Integer loginMemberId = getLogin(session);

        AdvertisementDto dto = new AdvertisementDto();
        dto.setAdId(adId);
        dto.setStatus(status);
        dto.setStatusUpdatedBy(loginMemberId);
        dto.setStatusUpdatedAt(LocalDateTime.now());

        advertisementService.updateAdvertisementStatus(dto);

        return "redirect:/admin/advertisement/manageList?tab=status";
    }
    
    // 우선도 선택(일반 / 프리미엄)
    @PostMapping("/updateGrade")
    public String updateGrade(
            @RequestParam int adId,
            @RequestParam String adGrade) {


        advertisementService.updateAdGrade(
                adId,
                adGrade
        );


        return "redirect:/admin/advertisement/detail?adId=" + adId + "&mode=manage";
    }
    
    // 기간 변경
    @PostMapping("updatePeriod")
    public String updatePeriod(
            @RequestParam Long adId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        advertisementService.updatePeriod(adId, start.atStartOfDay(), end.atStartOfDay());
        return "redirect:/admin/advertisement/detail?adId=" + adId + "&mode=manage";
    }
    
    // 상세에서 상태 변경
    @PostMapping("/status/detail")
    public String statusFromDetail(@RequestParam int adId,
		            @RequestParam String status,
		            HttpSession session) {
		
    	Integer loginMemberId = getLogin(session);
    	
		AdvertisementDto dto = new AdvertisementDto();
		dto.setAdId(adId);
		dto.setStatus(status);
		dto.setStatusUpdatedBy(loginMemberId);
		dto.setStatusUpdatedAt(LocalDateTime.now());
		
		advertisementService.updateAdvertisementStatus(dto);
		
		 return "redirect:/admin/advertisement/detail?adId=" + dto.getAdId() + "&mode=manage";
	}
    
    // 광고 대시보드 페이지
    @GetMapping("/statistics")
    public String statistics() {

        return "admin/advertisement/statistics";

    }
    // 대시보드 차트
    // 총 통계
    @ResponseBody
    @GetMapping("/chart/summary")
    public AdvertisementChartDto summary(){

        return advertisementService.selectSummary();

    }
    // 일일통계 차트
    @ResponseBody
    @GetMapping("/chart/daily")
    public List<AdvertisementChartDto> dailyChart(){

        return advertisementService.selectDailyChart();

    }
    // ctr 탑5
    @ResponseBody
    @GetMapping("/chart/ctr")
    public List<AdvertisementChartDto> ctrChart(){

        return advertisementService.selectTopCtrChart();

    }
    // 등급비율
    @ResponseBody
    @GetMapping("/chart/grade")
    public List<AdvertisementChartDto> gradeChart(){

        return advertisementService.selectGradeChart();

    }
    // 위치별 노출
    @GetMapping("/chart/position")
    @ResponseBody
    public List<AdvertisementChartDto> positionChart() {

        return advertisementService.selectPositionChart();

    }
    // 연장률
    @GetMapping("/chart/extension-rate")
    @ResponseBody
    public double extensionRate() {

        return advertisementService.selectExtensionRate();

    }
    // 위치별 ctr 차트
    @GetMapping("/chart/positionCtr")
    @ResponseBody
    public List<AdvertisementChartDto> positionCtrChart(){

        return advertisementService.selectPositionCtrChart();

    }
    // AI 통계 요약
 // AI 통계 요약
    @GetMapping("/chart/ai-summary")
    @ResponseBody
    public DashboardAiDto aiSummary() {

        return advertisementService.getDashboardAiData();

    }

    // 로그인 헬퍼
    private Integer getLogin(HttpSession session) {
        Integer id = (Integer) session.getAttribute("loginMemberId");
        return (id != null) ? id : 22;
    }
}