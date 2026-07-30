package com.moit.advertisement.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.moit.advertisement.dto.AdvertisementDto;
import com.moit.advertisement.dto.AdvertisementImageDto;
import com.moit.advertisement.dto.AdvertisementSearchDto;
import com.moit.advertisement.dto.ExtensionRequestDto;
import com.moit.advertisement.service.AdvertisementService;
import com.moit.member.dto.UserDto;
import com.moit.security.CustomUserDetails;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user/advertisement")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    private static final String UPLOAD_PATH = "C:/upload/ad/";

    // 내 광고 목록
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String list(
            AdvertisementSearchDto dto,
            HttpSession session,
            Authentication authentication,
            Model model) {
    	
		String loginId     = null, provider = null;
		UserDto user=null;
		Object principal = authentication.getPrincipal();
		Integer memberId = null;
		//1. local
		if(   principal   instanceof CustomUserDetails ) {
			CustomUserDetails  users = (CustomUserDetails)principal;
			user=users.getUser();
			loginId    =  users.getUser().getLoginId();
			memberId = users.getUser().getMemberId();
		} 

        dto.setAdvertiserId(memberId);

        int page = dto.getPage() <= 0 ? 1 : dto.getPage();
        int size = dto.getSize() <= 0 ? 10 : dto.getSize();

        dto.setPage(page);
        dto.setSize(size);

        List<AdvertisementDto> list =
                advertisementService.searchMyAdvertisement(dto);

        System.out.println("광고 개수 = " + list.size());

        for(AdvertisementDto ad : list){
            System.out.println(
                "adId=" + ad.getAdId()
                + ", title=" + ad.getTitle()
                + ", end=" + ad.getEndDatetime()
                + ", extension=" + ad.getExtensionStatus()
            );
        }

        int totalCnt =
                advertisementService.selectMyAdvertisementTotalCnt(dto);

        int totalPage =
                (int)Math.ceil((double)totalCnt / size);
        
     // 데이터가 없어서 totalPage가 0이 나오더라도 최소 1페이지로 고정
        if (totalPage == 0) { totalPage = 1; }

        model.addAttribute("list", list);
        model.addAttribute("search", dto);
        model.addAttribute("dto" , user); 
        model.addAttribute("totalCnt", totalCnt);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("menu", "advertisement");

        return "user/advertisement/adList";
    }

    // 등록 화면
    @GetMapping("/write")
    public String write(Model model) {

        model.addAttribute("dto", new AdvertisementDto());
        model.addAttribute("mode", "write");

        return "user/advertisement/adForm";
    }

    // 등록
    @PostMapping("/write")
    public String writeAction(

            AdvertisementDto dto,

            @RequestParam(value = "imageFiles", required = false)
            List<MultipartFile> imageFiles,

            @RequestParam(value = "imageTypes", required = false)
            List<String> imageTypes,

            Authentication authentication) {

        try {

        	String loginId     = null, provider = null;
    		UserDto user=null;
    		Object principal = authentication.getPrincipal();
    		Integer memberId = null;
    		//1. local
    		if(   principal   instanceof CustomUserDetails ) {
    			CustomUserDetails  users = (CustomUserDetails)principal;
    			user=users.getUser();
    			loginId    =  users.getUser().getLoginId();
    			memberId = users.getUser().getMemberId();
    		}

            dto.setAdvertiserId(memberId);

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

        return "redirect:/user/advertisement/list";
    }

    // 상세
    @GetMapping("/detail")
    public String detail(
            @RequestParam int adId,
            Authentication authentication,
            Model model) {

        AdvertisementDto dto =
                advertisementService.selectAdvertisementOne(adId);

        String loginId     = null, provider = null;
		UserDto user=null;
		Object principal = authentication.getPrincipal();
		Integer memberId = null;
		//1. local
		if(   principal   instanceof CustomUserDetails ) {
			CustomUserDetails  users = (CustomUserDetails)principal;
			user=users.getUser();
			loginId    =  users.getUser().getLoginId();
			memberId = users.getUser().getMemberId();
		}

        if (dto == null) {
            return "redirect:/user/advertisement/list";
        }

        if (dto.getAdvertiserId() != memberId) {
            return "redirect:/user/advertisement/list";
        }

        model.addAttribute("dto", dto);

        return "user/advertisement/adDetail";
    }
    
    // 수정 화면
    @GetMapping("/edit")
    public String edit(
            @RequestParam int adId,
            Authentication authentication,
            Model model) {

        AdvertisementDto dto =
                advertisementService.selectAdvertisementOne(adId);

        String loginId     = null, provider = null;
		UserDto user=null;
		Object principal = authentication.getPrincipal();
		Integer memberId = null;
		//1. local
		if(   principal   instanceof CustomUserDetails ) {
			CustomUserDetails  users = (CustomUserDetails)principal;
			user=users.getUser();
			loginId    =  users.getUser().getLoginId();
			memberId = users.getUser().getMemberId();
		}

        if (dto == null) {
            return "redirect:/user/advertisement/list";
        }

        if (dto.getAdvertiserId() != memberId) {
            return "redirect:/user/advertisement/list";
        }

        model.addAttribute("dto", dto);
        model.addAttribute("mode", "edit");
        
        setImageModel(dto, model);

        return "user/advertisement/adForm";
    }

    // 수정
    @PostMapping("/edit")
    public String editAction(

            AdvertisementDto dto,

            @RequestParam(value = "imageFiles", required = false)
            List<MultipartFile> imageFiles,

            @RequestParam(value = "imageTypes", required = false)
            List<String> imageTypes,

            Authentication authentication) {

    	String loginId     = null, provider = null;
		UserDto user=null;
		Object principal = authentication.getPrincipal();
		Integer memberId = null;
		//1. local
		if(   principal   instanceof CustomUserDetails ) {
			CustomUserDetails  users = (CustomUserDetails)principal;
			user=users.getUser();
			loginId    =  users.getUser().getLoginId();
			memberId = users.getUser().getMemberId();
		}

        AdvertisementDto origin =
                advertisementService.selectAdvertisementOne(dto.getAdId());

        if (origin == null) {
            return "redirect:/user/advertisement/list";
        }

        if (origin.getAdvertiserId() != memberId) {
            return "redirect:/user/advertisement/list";
        }

        dto.setAdvertiserId(memberId);

        advertisementService.updateAdvertisement(
                dto,
                imageFiles,
                imageTypes);

        return "redirect:/user/advertisement/detail?adId=" + dto.getAdId();
    }

    private void setImageModel(
            AdvertisementDto dto,
            Model model) {

        String mainImage = "";
        String bannerImage = "";
        String listSidebarImage = "";
        String detailSidebarImage = "";

        if (dto.getImageList() != null) {

            for (AdvertisementImageDto image : dto.getImageList()) {

                switch (image.getImageType()) {

                case "MAIN":
                    mainImage = image.getImageUrl();
                    break;

                case "MEETUP_LIST_BANNER":
                    bannerImage = image.getImageUrl();
                    break;

                case "MEETUP_LIST_SIDEBAR":
                    listSidebarImage = image.getImageUrl();
                    break;

                case "MEETUP_DETAIL_SIDEBAR":
                    detailSidebarImage = image.getImageUrl();
                    break;
                }
            }
        }

        model.addAttribute("mainImage", mainImage);
        model.addAttribute("bannerImage", bannerImage);
        model.addAttribute("listSidebarImage", listSidebarImage);
        model.addAttribute("detailSidebarImage", detailSidebarImage);
    }
    
 // 삭제
    @PostMapping("/delete")
    public String delete(
            @RequestParam int adId,
            Authentication authentication) {

        AdvertisementDto dto =
                advertisementService.selectAdvertisementOne(adId);

        	String loginId     = null, provider = null;
    		UserDto user=null;
    		Object principal = authentication.getPrincipal();
    		Integer memberId = null;
    		//1. local
    		if(   principal   instanceof CustomUserDetails ) {
    			CustomUserDetails  users = (CustomUserDetails)principal;
    			user=users.getUser();
    			loginId    =  users.getUser().getLoginId();
    			memberId = users.getUser().getMemberId();
    		}

        // 권한 체크
        if (dto == null) {
            return "redirect:/user/advertisement/list";
        }

        if (dto.getAdvertiserId() != memberId) {
            return "redirect:/user/advertisement/list";
        }

        // 서비스에서 파일 + 이미지DB + 광고 삭제 모두 처리
        advertisementService.deleteAdvertisement(adId);

        return "redirect:/user/advertisement/list";
    }


    // 광고 클릭
    @GetMapping("/click")
    public String click(
            @RequestParam int adId,
            @RequestParam String position,
            HttpServletRequest request,
            HttpSession session) {


    	// 클릭 로그 확인 (1시간에 한번만 +1 인정)
    	boolean counted = advertisementService.insertClickLog(
    	        adId,
    	        position,
    	        request,
    	        session
    	);
    	// 한시간 내에 기록 x면 증가
    	if (counted) {
    	    advertisementService.updateAdvertisementClick(adId);
    	}

        AdvertisementDto dto =
                advertisementService.selectAdvertisementOne(adId);


        if(dto == null || dto.getLandingUrl() == null){
            return "redirect:/";
        }


        return "redirect:" + dto.getLandingUrl();
    }
    
    // 광고 기간 연장 신청
    @PostMapping("/extensionRequest")
    @ResponseBody
    public ResponseEntity<?> extensionRequest(
            @RequestBody ExtensionRequestDto dto,
            Authentication authentication){


        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();


        dto.setAdvertiserId(user.getAppUserId());


        advertisementService.requestExtension(dto);


        return ResponseEntity.ok().build();

    }
}