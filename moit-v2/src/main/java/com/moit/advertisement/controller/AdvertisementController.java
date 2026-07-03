package com.moit.advertisement.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.moit.advertisement.dto.AdvertisementDto;
import com.moit.advertisement.dto.AdvertisementSearchDto;
import com.moit.advertisement.service.AdvertisementService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/advertisement")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    // 내 광고 목록
    @GetMapping("/list")
    public String list(
            AdvertisementSearchDto dto,
            HttpSession session,
            Model model) {

        Integer loginMemberId =
                (Integer) session.getAttribute("loginMemberId");

        if (loginMemberId == null) {
            loginMemberId = 22;
            //  return "redirect:/member/login"; // 로그인 연동시 변경
        }

        dto.setAdvertiserId(loginMemberId);

        int page = dto.getPage() <= 0 ? 1 : dto.getPage();
        int size = dto.getSize() <= 0 ? 10 : dto.getSize();

        dto.setPage(page);
        dto.setSize(size);

        List<AdvertisementDto> list =
                advertisementService.searchMyAdvertisement(dto);

        int totalCnt =
                advertisementService.selectMyAdvertisementTotalCnt(dto);

        int totalPage =
                (int)Math.ceil((double)totalCnt / size);

        model.addAttribute("list", list);
        model.addAttribute("dto", dto);
        model.addAttribute("totalCnt", totalCnt);
        model.addAttribute("totalPage", totalPage);

        return "advertisement/list";
    }

    // 등록 화면
    @GetMapping("/write")
    public String write() {

        return "advertisement/write";

    }

    // 등록
    @PostMapping("/write")
    public String writeAction(

            AdvertisementDto dto,

            @RequestParam("imageFile")
            MultipartFile imageFile,

            HttpSession session) {

        try {

            Integer loginMemberId =
                    (Integer) session.getAttribute("loginMemberId");

            if(loginMemberId == null) {
                loginMemberId = 22;
                //  return "redirect:/member/login";
            }

            dto.setAdvertiserId(loginMemberId);

            if(imageFile != null && !imageFile.isEmpty()) {

                String uploadPath =
                        "D:/file/ad/";

                File dir = new File(uploadPath);

                if(!dir.exists()) {
                    dir.mkdirs();
                }

                String saveName =
                        UUID.randomUUID()
                        + "_"
                        + imageFile.getOriginalFilename();

                File saveFile =
                        new File(dir, saveName);

                imageFile.transferTo(saveFile);

                dto.setImageUrl(
                        "/upload/ad/" + saveName);

            }

            advertisementService.insertAdvertisement(dto);

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

        return "redirect:/advertisement/list";

    }

    // 상세 조회
    @GetMapping("/detail")
    public String detail(

            @RequestParam int adId,

            HttpSession session,

            Model model) {

        AdvertisementDto dto =
                advertisementService.selectAdvertisementOne(adId);

        Integer loginMemberId =
                (Integer) session.getAttribute("loginMemberId");

        if(loginMemberId == null) {
            loginMemberId = 22;
            // return "redirect:/member/login";
        }

        if(dto == null) {
            return "redirect:/advertisement/list";
        }

        if(dto.getAdvertiserId() != loginMemberId) {
            return "redirect:/advertisement/list";
        }

        model.addAttribute("dto", dto);

        return "advertisement/detail";

    }
    
    // 수정 화면
    @GetMapping("/edit")
    public String edit(
            @RequestParam int adId,
            HttpSession session,
            Model model) {

        AdvertisementDto dto =
                advertisementService.selectAdvertisementOne(adId);

        Integer loginMemberId =
                (Integer) session.getAttribute("loginMemberId");

        if (loginMemberId == null) {
            loginMemberId = 22;
            //  return "redirect:/member/login";
        }

        if (dto == null || dto.getAdvertiserId() != loginMemberId) {
            return "redirect:/advertisement/list";
        }

        model.addAttribute("dto", dto);

        return "advertisement/edit";
    }

    // 수정
    @PostMapping("/edit")
    public String editAction(
            AdvertisementDto dto,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            HttpSession session) {

        try {

            AdvertisementDto origin =
                    advertisementService.selectAdvertisementOne(dto.getAdId());

            Integer loginMemberId =
                    (Integer) session.getAttribute("loginMemberId");

            if (loginMemberId == null) {
                loginMemberId = 22;
                //  return "redirect:/member/login";
            }

            if(origin == null) {
                return "redirect:/advertisement/list";
            }

            if(origin.getAdvertiserId() != loginMemberId) {
                return "redirect:/advertisement/list";
            }

            dto.setAdvertiserId(loginMemberId);

            if (imageFile != null && !imageFile.isEmpty()) {

                String uploadPath = "D:/file/ad/";

                // 기존 이미지 삭제
                if (origin.getImageUrl() != null && !origin.getImageUrl().isBlank()) {

                    String oldFileName =
                            origin.getImageUrl().replace("/upload/ad/", "");

                    File oldFile =
                            new File(uploadPath, oldFileName);

                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }

                // 새 파일 저장
                File dir = new File(uploadPath);

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String originalName = imageFile.getOriginalFilename();

                String saveName =
                        UUID.randomUUID() + "_" + originalName;

                File saveFile =
                        new File(dir, saveName);

                imageFile.transferTo(saveFile);

                dto.setImageUrl("/upload/ad/" + saveName);

            } else {

                dto.setImageUrl(origin.getImageUrl());

            }

            advertisementService.updateAdvertisement(dto);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "redirect:/advertisement/detail?adId=" + dto.getAdId();
    }

    // 삭제
    @PostMapping("/delete")
    public String delete(
            @RequestParam int adId,
            HttpSession session) {

        AdvertisementDto dto =
                advertisementService.selectAdvertisementOne(adId);

        Integer loginMemberId =
                (Integer) session.getAttribute("loginMemberId");

        if (loginMemberId == null) {
            loginMemberId = 22;
            //  return "redirect:/member/login";
        }

        if(dto == null) {
            return "redirect:/advertisement/list";
        }

        if(dto.getAdvertiserId() != loginMemberId) {
            return "redirect:/advertisement/list";
        }

        advertisementService.deleteAdvertisement(adId);

        return "redirect:/advertisement/list";
    }

    // 광고 클릭
    @GetMapping("/click")
    public String click(@RequestParam int adId) {

        advertisementService.updateAdvertisementClick(adId);

        AdvertisementDto dto =
                advertisementService.selectAdvertisementOne(adId);

        if (dto == null || dto.getLandingUrl() == null) {
            return "redirect:/";
        }

        return "redirect:" + dto.getLandingUrl();
    }

}