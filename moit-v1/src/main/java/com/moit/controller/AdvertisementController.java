package com.moit.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.moit.dao.AdvertisementMapper;
import com.moit.dto.AdvertisementDto;
import com.moit.dto.AdvertisementSearchDto;
import com.moit.service.AdvertisementService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/advertisement/admin")
public class AdvertisementController {
	
	@Autowired
    private AdvertisementService advertisementService;
	@Autowired
	private AdvertisementMapper advertisementMapper;

    @GetMapping("/adList.do")
    public String adList(AdvertisementSearchDto dto, Model model) {
    	
    	// 1. 페이지 기본값
    	int page = dto.getPage();
    	int size = dto.getSize();

    	if (page <= 0) page = 1;
        if (size <= 0) size = 10;
        
     // 2. offset 계산
        int offset = (page - 1) * size;

        dto.setPage(page);
        dto.setSize(size);
        dto.setOffset(offset);
        
     // 3. 리스트
        List<AdvertisementDto> list =
                advertisementService.searchByAdmin(dto);

        // 4. 전체 개수
        int totalCnt =
                advertisementService.selectAdvertisementTotalCnt(dto);

        // 5. 페이지 계산
        int totalPage =
                (totalCnt <= 0) ? 1 : (int) Math.ceil((double) totalCnt / size);

        model.addAttribute("list", list);
        model.addAttribute("totalCnt", totalCnt);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("dto", dto);
        
        // 통계
        model.addAttribute( "totalAdCnt", advertisementService.selectTotalAdvertisementCnt() );
    	model.addAttribute( "openCnt", advertisementService.selectOpenAdvertisementCnt() );
    	model.addAttribute( "pendingCnt", advertisementService.selectPendingAdvertisementCnt() );
    	model.addAttribute( "closedCnt", advertisementService.selectClosedAdvertisementCnt() );
    	
        return "advertisement/admin/adList";
    }
    
    @GetMapping("/adWrite.do")
    public String writePage() {
    	return "advertisement/admin/adWrite"; // JSP 경로
    }
    
    // 등록 처리
    @PostMapping("/adWriteAction.do")
    public String insertAdvertisement(
            AdvertisementDto dto,
            @RequestParam("imageFile") MultipartFile file,
            HttpSession session) {

        try {

        	// 로그인 관리자 ID
        	Integer loginUserId =
        	        (Integer) session.getAttribute("loginUserId");

        	// 테스트용
        	if (loginUserId == null) {
        	    loginUserId = 22;
        	}

        	dto.setAuthorId(loginUserId);

        	System.out.println("authorId = " + dto.getAuthorId());

            // 2. 파일 업로드 처리
        	if (file != null && !file.isEmpty()) {

        		String uploadPath = "D:/file/ad/";

                String originalName = file.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();
                String savedFileName = uuid + "_" + originalName;

                File dir = new File(uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File saveFile = new File(dir, savedFileName);

                file.transferTo(saveFile);
                System.out.println(uploadPath);
                System.out.println(saveFile.getAbsolutePath());
                System.out.println(saveFile.exists());
                // DB 저장용 경로
                dto.setImageUrl("/upload/ad/" + savedFileName);
            }
        	
            // 3. DB insert
            advertisementService.insertAdvertisement(dto);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return "redirect:/advertisement/admin/adList.do";
    }

    // 상세 페이지
    @GetMapping("/adDetail.do")
    public String detailPage(@RequestParam int adId, Model model) {

        AdvertisementDto dto = advertisementService.selectAdvertisementOne(adId);
        model.addAttribute("dto", dto);
        
        return "advertisement/admin/adDetail";
    }
    
 // 수정 페이지
    @GetMapping("/adUpdate.do")
    public String updatePage(@RequestParam(required = false) Integer adId, Model model) {
        if (adId == null) {
            return "redirect:/advertisement/admin/adList.do";
        }

        AdvertisementDto dto = advertisementService.selectAdvertisementOne(adId);
        model.addAttribute("dto", dto);

        return "advertisement/admin/adEdit";
    }

    // 수정 처리
    @PostMapping("/adEdit.do")
    public String updateAction(AdvertisementDto dto,
                               @RequestParam(value = "imageFile", required = false) MultipartFile file,
                               HttpSession session) {
    	System.out.println("🔥🔥🔥 adEdit 들어옴");
        try {

        	// 기존 광고 조회
            AdvertisementDto origin =
                    advertisementService.selectAdvertisementOne(dto.getAdId());

            // 광고주는 기존 값 유지
            dto.setAdvertiserId(origin.getAdvertiserId());

            // 로그인 관리자 ID
            Integer loginUserId =
                    (Integer) session.getAttribute("loginUserId");

            // 테스트용
            if(loginUserId == null) {
                loginUserId = 22;
            }

            dto.setAuthorId(loginUserId);
        	
            // 이미지 수정 시만 업로드
            if (file != null && !file.isEmpty()) {

            	String uploadPath = "D:/file/ad/";

                File dir = new File(uploadPath);

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String uuid = UUID.randomUUID().toString();
                String savedFileName = uuid + "_" + file.getOriginalFilename();

                file.transferTo(new File(dir, savedFileName));

                dto.setImageUrl("/upload/ad/" + savedFileName);
            }else {
                // 이미지 안 바꾸면 기존 이미지 유지
                dto.setImageUrl(origin.getImageUrl());
            }
            System.out.println("advertiserId = " + dto.getAdvertiserId());
            System.out.println("authorId = " + dto.getAuthorId());

            advertisementService.updateAdvertisement(dto);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println("imageUrl = " + dto.getImageUrl());
        return "redirect:/advertisement/admin/adDetail.do?adId=" + dto.getAdId();
    }
    
    // 목록에서 상태변경
    @PostMapping("/updateStatusAjax.do")
    @ResponseBody
    public Map<String, String> updateStatusAjax(@RequestBody AdvertisementDto dto) {

        Map<String, String> result = new HashMap<>();
        
        System.out.println("adId = " + dto.getAdId());
        System.out.println("status = " + dto.getStatus());

        try {
            advertisementService.updateAdvertisementStatus(dto);
            result.put("result", "OK");
        } catch (Exception e) {
        	e.printStackTrace();
            result.put("result", "FAIL");
        }

        return result;
    }
    
    // 삭제 기능
    @PostMapping("/adDelete.do")
    public String deleteAdvertisement(@RequestParam int adId,
                                      HttpSession session) {

        Integer loginUserId =
            (Integer) session.getAttribute("loginUserId");
        
        if(loginUserId == null){
            loginUserId = 22;
        }

        AdvertisementDto dto = new AdvertisementDto();
        dto.setAdId(adId);
        dto.setAuthorId(loginUserId);

        advertisementService.deleteAdvertisement(dto);

        return "redirect:/advertisement/admin/adList.do";
    }
    
    
    
    
    ////////// 클릭+노출수 테스트
    
    @GetMapping("/click.do")
    public String clickAdvertisement(@RequestParam int adId) {

        advertisementService.updateAdvertisementClick(adId);

        AdvertisementDto dto =
        		advertisementMapper.selectAdvertisementOne(adId);

        if (dto == null || dto.getLandingUrl() == null) {
            return "redirect:/";
        }
        
        return "redirect:" + dto.getLandingUrl();
    }
    
    @GetMapping("/test.do")
    public String adTest(Model model) {

        System.out.println("🔥 test.do 들어옴");

        AdvertisementDto ad =
                advertisementService.selectTopAdvertisement();
        
        if (ad != null) {
            advertisementService.updateImpressions(ad.getAdId());
        }

        System.out.println("🔥 서비스 호출 끝");
        System.out.println("🔥 ad 결과 = " + ad);
        System.out.println(ad.getImageUrl());

        model.addAttribute("ad", ad);

        return "main";
    }
    
    @ControllerAdvice
    @RequiredArgsConstructor
    public class GlobalModelAttribute {

        private final AdvertisementService advertisementService;

        @ModelAttribute("ad")
        public void addAd(Model model) {

            AdvertisementDto ad =
                    advertisementService.selectTopAdvertisement();
            
            if (ad != null) {
                advertisementService.updateImpressions(ad.getAdId());
            }

            model.addAttribute("ad", ad);
        }
    }
    
}