package com.moit.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.moit.advertisement.dto.AdvertisementDto;
import com.moit.advertisement.service.AdvertisementService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class BasicController {
	@Autowired AdvertisementService advertisementService;
	
	/* 메인페이지 광고 */
	@GetMapping("/main")
    public String main(Model model,
		            HttpServletRequest request,
		            HttpSession session) {

		Integer memberId =
				 (Integer)session.getAttribute("loginMemberId");
		
		String sessionId =
		        session.getId();
		
        AdvertisementDto mainAd =
                advertisementService.selectTopAdvertisement("MAIN", memberId, sessionId);
        // 광고가 존재하면 노출 증가
        if(mainAd != null) {

            boolean counted =
                advertisementService.insertImpressionLog(
                    mainAd.getAdId(),
                    "MAIN",
                    request,
                    session
                );

            if(counted){
                advertisementService.updateImpressions(mainAd.getAdId());
            }
        }
        
        model.addAttribute("mainAd", mainAd);
        

        return "user/main";
    }
}
