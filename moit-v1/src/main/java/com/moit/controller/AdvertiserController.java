package com.moit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.moit.dto.AdvertiserDto;
import com.moit.service.AdvertiserService;

@RestController
@RequestMapping("/advertisers")
public class AdvertiserController {

    @Autowired
    private AdvertiserService advertiserService;

    // 자동완성 API
//    @GetMapping("/search")
//    public List<AdvertiserDto> searchAdvertiser(@RequestParam String keyword) {
//        return advertiserService.searchAdvertiser(keyword);
//    }
   
    @ResponseBody
    @RequestMapping(value="/search", method=RequestMethod.GET)
    public List<AdvertiserDto> getUserList(@RequestParam String keyword) {
    	 return advertiserService.searchAdvertiser(keyword);
    }

    
    /*
	@ResponseBody
	@RequestMapping(value="/search" , method=RequestMethod.GET)
	public Map<String, Object> doubleNickname(  @RequestParam("nickname")  String nickname ){
		Map<String, Object> result = new HashMap<>();
		
		//db에서 이메일 존재여부 확인
		UserDto find = service.findByNickname(nickname);
		
		if(find != null) { result.put(  "exists"  , true);  }
		else             { result.put(  "exists"  , false); }
		return result;
	}	*/
}