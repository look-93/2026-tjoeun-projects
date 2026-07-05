package com.moit.advertisement.controller;

import java.time.LocalDateTime;
import java.util.List;

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
    public String approvalList(
            AdvertisementSearchDto dto,
            Model model) {

        int page = dto.getPage() <= 0 ? 1 : dto.getPage();
        int size = dto.getSize() <= 0 ? 10 : dto.getSize();

        dto.setPage(page);
        dto.setSize(size);

        List<AdvertisementDto> list =
                advertisementService.searchWaitingList(dto);

        int totalCnt =
                advertisementService.selectWaitingTotalCnt(dto);

        int totalPage =
                (int)Math.ceil((double)totalCnt / size); 

        model.addAttribute("list", list);
        model.addAttribute("dto", dto);
        model.addAttribute("totalCnt", totalCnt);
        model.addAttribute("totalPage", totalPage);

        return "admin/advertisement/approvalList";
    }

    // 승인 상세
    @GetMapping("/approvalDetail")
    public String approvalDetail(
            @RequestParam int adId,
            Model model) {

        AdvertisementDto dto =
                advertisementService.selectAdvertisementOne(adId);

        if(dto == null) {
            return "redirect:/admin/advertisement/approvalList";
        }

        model.addAttribute("dto", dto);
        model.addAttribute(
                "imageList",
                advertisementService.selectAdvertisementImageList(adId));

        return "admin/advertisement/approvalDetail";
    }

    // 승인 처리
    @PostMapping("/approve")
    public String approve(
    		@RequestParam int adId,
            HttpSession session) {

        Integer loginMemberId =
                (Integer)session.getAttribute("loginMemberId");

        if(loginMemberId == null) {
            loginMemberId = 22;
        }

        AdvertisementDto dto = new AdvertisementDto();
        dto.setAdId(adId);
        dto.setApprovalStatus("APPROVED");
        dto.setApprovedBy(loginMemberId);
        dto.setApprovedAt(LocalDateTime.now());

        advertisementService.updateApprovalStatus(dto);

        return "redirect:/admin/advertisement/manageList";
    }

    // 반려 처리
    @PostMapping("/reject")
    public String reject(
    		@RequestParam int adId,
            @RequestParam String rejectReason,
            HttpSession session) {

        Integer loginMemberId =
                (Integer)session.getAttribute("loginMemberId");

        if(loginMemberId == null) {
            loginMemberId = 22;
        }

        AdvertisementDto dto = new AdvertisementDto();
        dto.setAdId(adId);
        dto.setApprovalStatus("REJECTED");
        dto.setApprovedBy(loginMemberId);
        dto.setRejectReason(rejectReason);
        dto.setApprovedAt(LocalDateTime.now());

        advertisementService.updateApprovalStatus(dto);

        return "redirect:/admin/advertisement/approvalList";
    }

    // 광고 관리 목록
    @GetMapping("/manageList")
    public String manageList(
    		@RequestParam(required = false, defaultValue = "approval") String tab,
            AdvertisementSearchDto dto,
            Model model) {

        int page = dto.getPage() <= 0 ? 1 : dto.getPage();
        int size = dto.getSize() <= 0 ? 10 : dto.getSize();

        dto.setPage(page);
        dto.setSize(size);

        List<AdvertisementDto> list;
        int totalCnt;
        
     // 1) 승인 탭 (WAITING 고정)
        if ("approval".equals(tab)) {

            dto.setApprovalStatus("WAITING"); // 핵심

            list = advertisementService.searchWaitingList(dto);
            totalCnt = advertisementService.selectWaitingTotalCnt(dto);

            model.addAttribute("waitingCnt",
                    advertisementService.selectWaitingTotalCnt(dto));

        }
        
     // 2) 운영 탭 (APPROVED 고정)
        else {

            dto.setApprovalStatus("APPROVED"); // 핵심

            list = advertisementService.searchByAdmin(dto);
            totalCnt = advertisementService.selectAdminAdvertisementTotalCnt(dto);
        }

        int totalPage = (int) Math.ceil((double) totalCnt / size);

     // 공통 통계
        model.addAttribute("totalAdCnt", advertisementService.selectTotalAdvertisementCnt());
        model.addAttribute("openCnt", advertisementService.selectOpenAdvertisementCnt());
        model.addAttribute("pendingCnt", advertisementService.selectPendingAdvertisementCnt());
        model.addAttribute("closedCnt", advertisementService.selectClosedAdvertisementCnt());
        
    // 페이징/리스트
        model.addAttribute("list", list);
        model.addAttribute("dto", dto);
        model.addAttribute("totalCnt", totalCnt);
        model.addAttribute("totalPage", totalPage);
        
     // 탭 제어 (핵심)
        model.addAttribute("tab", tab);

     // 현재 시간
        model.addAttribute("now", LocalDateTime.now());

        return "admin/advertisement/manageList";
    }

    // 광고 관리 상세
    @GetMapping("/manageDetail")
    public String manageDetail(
            @RequestParam int adId,
            Model model) {

        AdvertisementDto dto =
                advertisementService.selectAdvertisementOne(adId);

        if(dto == null) {
            return "redirect:/admin/advertisement/manageList";
        }

        model.addAttribute("dto", dto);

        model.addAttribute(
                "imageList",
                advertisementService.selectAdvertisementImageList(adId));

        return "admin/advertisement/manageDetail";
    }

    // 상태 변경
    @PostMapping("/status")
    public String status(
            @RequestParam int adId,
            @RequestParam String status,
            HttpSession session) {

        Integer loginMemberId =
                (Integer) session.getAttribute("loginMemberId");

        if (loginMemberId == null) {
            loginMemberId = 22;
            // return "redirect:/member/login";
        }

        AdvertisementDto dto = new AdvertisementDto();
        dto.setAdId(adId);
        dto.setStatus(status);
        dto.setStatusUpdatedBy(loginMemberId);
        dto.setStatusUpdatedAt(LocalDateTime.now());

        advertisementService.updateAdvertisementStatus(dto);

        return "redirect:/admin/advertisement/manageDetail?adId="
                + adId;
    }

}










