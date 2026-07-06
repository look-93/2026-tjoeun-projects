package com.moit.advertisement.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.moit.advertisement.dto.AdvertisementDto;
import com.moit.advertisement.dto.AdvertisementSearchDto;
import com.moit.advertisement.service.AdvertisementService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/advertisement")
public class AdvertisementAdminController {

    private final AdvertisementService advertisementService;

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

        } else {

            dto.setApprovalStatus("APPROVED");

            list = advertisementService.searchByAdmin(dto);
            totalCnt = advertisementService.selectAdminAdvertisementTotalCnt(dto);
        }

        int totalPage = (int) Math.ceil((double) totalCnt / dto.getSize());

        model.addAttribute("list", list);
        model.addAttribute("dto", dto);
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

    // 승인 처리
    @PostMapping("/approve")
    public String approve(@RequestParam int adId,
                          HttpSession session) {

        Integer loginMemberId = getLogin(session);

        AdvertisementDto dto = new AdvertisementDto();
        dto.setAdId(adId);
        dto.setApprovalStatus("APPROVED");
        dto.setApprovedBy(loginMemberId);
        dto.setApprovedAt(LocalDateTime.now());

        advertisementService.updateApprovalStatus(dto);

        return "redirect:/admin/advertisement/manageList?tab=approval";
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

    // 상태 변경 
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
    
    @PostMapping("updatePeriod")
    public String updatePeriod(
            @RequestParam Long adId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        advertisementService.updatePeriod(adId, start.atStartOfDay(), end.atStartOfDay());
        return "redirect:/admin/advertisement/detail?adId=" + adId + "&mode=manage";
    }
    
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

    // 로그인 헬퍼
    private Integer getLogin(HttpSession session) {
        Integer id = (Integer) session.getAttribute("loginMemberId");
        return (id != null) ? id : 22;
    }
}