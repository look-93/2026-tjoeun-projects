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
import com.moit.advertisement.dto.AdvertisementImageDto;
import com.moit.advertisement.dto.AdvertisementSearchDto;
import com.moit.advertisement.service.AdvertisementService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/advertisement")
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    // 파일 공통경로
    private static final String UPLOAD_PATH = "D:/file/ad/";

    // 내 광고 목록
    @GetMapping("/list")
    public String list(
            AdvertisementSearchDto dto,
            HttpSession session,
            Model model) {

        Integer loginMemberId =
                (Integer) session.getAttribute("loginMemberId");

        if (loginMemberId == null) {
            loginMemberId = 3;
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
            @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @RequestParam(value = "imageTypes", required = false) List<String> imageTypes,
            HttpSession session) {

        try {

            Integer loginMemberId =
                    (Integer) session.getAttribute("loginMemberId");

            if (loginMemberId == null) {
                loginMemberId = 3;
                // return "redirect:/member/login";
            }

            dto.setAdvertiserId(loginMemberId);

            // 광고 등록
            advertisementService.insertAdvertisement(dto);

            if (imageFiles != null && imageTypes != null) {

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

                    file.transferTo(new File(dir, saveName));

                    AdvertisementImageDto imageDto =
                            new AdvertisementImageDto();

                    imageDto.setAdId(dto.getAdId());
                    imageDto.setImageType(imageTypes.get(i));
                    imageDto.setImageUrl("/upload/ad/" + saveName);

                    advertisementService.insertAdvertisementImage(imageDto);
                }
            }

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

        if (loginMemberId == null) {
            loginMemberId = 3;
            // return "redirect:/member/login";
        }

        if (dto == null) {
            return "redirect:/advertisement/list";
        }

        if (dto.getAdvertiserId() != loginMemberId) {
            return "redirect:/advertisement/list";
        }

        model.addAttribute("dto", dto);

        model.addAttribute(
                "imageList",
                advertisementService.selectAdvertisementImageList(adId));

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
            loginMemberId = 3;
            // return "redirect:/member/login";
        }

        if (dto == null) {
            return "redirect:/advertisement/list";
        }

        if (dto.getAdvertiserId() != loginMemberId) {
            return "redirect:/advertisement/list";
        }

        model.addAttribute("dto", dto);

        model.addAttribute(
                "imageList",
                advertisementService.selectAdvertisementImageList(adId));

        return "advertisement/edit";
    }

    // 수정
    @PostMapping("/edit")
    public String editAction(

            AdvertisementDto dto,

            @RequestParam(value = "imageFiles", required = false)
            List<MultipartFile> imageFiles,

            @RequestParam(value = "imageTypes", required = false)
            List<String> imageTypes,

            HttpSession session) {

        try {

            AdvertisementDto origin =
                    advertisementService.selectAdvertisementOne(dto.getAdId());

            Integer loginMemberId =
                    (Integer) session.getAttribute("loginMemberId");

            if (loginMemberId == null) {
                loginMemberId = 3;
                // return "redirect:/member/login";
            }

            if (origin == null) {
                return "redirect:/advertisement/list";
            }

            if (origin.getAdvertiserId() != loginMemberId) {
                return "redirect:/advertisement/list";
            }

            dto.setAdvertiserId(loginMemberId);

            // 광고 정보 수정
            advertisementService.updateAdvertisement(dto);

            // 이미지 수정
            if (imageFiles != null && imageTypes != null) {

                // 기존 이미지 조회
                List<AdvertisementImageDto> oldImages =
                        advertisementService.selectAdvertisementImageList(dto.getAdId());

                // 실제 파일 삭제
                for (AdvertisementImageDto image : oldImages) {

                    if (image.getImageUrl() == null) {
                        continue;
                    }

                    String fileName =
                            image.getImageUrl().replace("/upload/ad/", "");

                    File oldFile =
                            new File(UPLOAD_PATH, fileName);

                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }

                // DB 이미지 삭제
                advertisementService.deleteAdvertisementImage(dto.getAdId());

                File dir = new File(UPLOAD_PATH);

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // 새 이미지 등록
                for (int i = 0; i < imageFiles.size(); i++) {

                    MultipartFile file = imageFiles.get(i);

                    if (file == null || file.isEmpty()) {
                        continue;
                    }

                    String saveName =
                            UUID.randomUUID()
                            + "_"
                            + file.getOriginalFilename();

                    file.transferTo(new File(dir, saveName));

                    AdvertisementImageDto imageDto =
                            new AdvertisementImageDto();

                    imageDto.setAdId(dto.getAdId());
                    imageDto.setImageType(imageTypes.get(i));
                    imageDto.setImageUrl("/upload/ad/" + saveName);

                    advertisementService.insertAdvertisementImage(imageDto);
                }
            }

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
            loginMemberId = 3;
            // return "redirect:/member/login";
        }

        // 권한 체크
        if (dto == null) {
            return "redirect:/advertisement/list";
        }

        if (dto.getAdvertiserId() != loginMemberId) {
            return "redirect:/advertisement/list";
        }

        // 기존 이미지 조회
        List<AdvertisementImageDto> imageList =
                advertisementService.selectAdvertisementImageList(adId);

        // 실제 파일 삭제
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

        // 이미지 DB 삭제
        advertisementService.deleteAdvertisementImage(adId);

        // 광고 삭제(논리삭제)
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