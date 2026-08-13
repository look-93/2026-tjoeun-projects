package com.moit.advertisement.controller;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.moit.advertisement.dto.AdvertisementDto;
import com.moit.advertisement.dto.AdvertisementImageDto;
import com.moit.advertisement.dto.AdvertisementSearchDto;
import com.moit.advertisement.service.AdvertisementService;
//import com.moit.member.dto.UserDto;
//import com.moit.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/advertisement")
@Tag(
    name = "Advertisement",
    description = "광고주 광고 관리 API"
)
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    private static final String UPLOAD_PATH = "C:/upload/ad/";
    
    private static final Long LOGIN_MEMBER_ID = 1L; // 로그인끼면 변경 (하드로 박는중)
    
    // 사용자 id
//    private Long getLoginMemberId(Authentication authentication) {
//
//        CustomUserDetails user =
//                (CustomUserDetails) authentication.getPrincipal();
//
//        return user.getUser().getMemberId();
//    }

    // 내 광고 목록
    @Operation(
	    summary = "내 광고 목록 조회",
	    description = "로그인한 광고주의 광고 목록을 페이지 단위로 조회합니다."
	)
    public ResponseEntity<AdvertisementDto.AdvertisementPageResponseDto> list(
            AdvertisementSearchDto dto) {

        Long memberId = LOGIN_MEMBER_ID;

        dto.setAdvertiserId(memberId);

        int page = dto.getPage() <= 0 ? 1 : dto.getPage();
        int size = dto.getSize() <= 0 ? 10 : dto.getSize();

        dto.setPage(page);
        dto.setSize(size);

        List<AdvertisementDto> list =
                advertisementService.searchMyAdvertisement(dto);

        int totalCnt =
                advertisementService.selectMyAdvertisementTotalCnt(dto);

        AdvertisementDto.AdvertisementPageResponseDto response =
                new AdvertisementDto.AdvertisementPageResponseDto(
                        list,
                        totalCnt,
                        page,
                        size
                );

        return ResponseEntity.ok(response);
    }

    // 등록
    @Operation(
	    summary = "광고 등록",
	    description = "광고 정보를 등록하고 광고 이미지를 함께 업로드합니다."
	)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)	
    public AdvertisementDto writeAction(

            AdvertisementDto dto,

            @RequestParam(value = "imageFiles", required = false)
            List<MultipartFile> imageFiles,

            @RequestParam(value = "imageTypes", required = false)
            List<String> imageTypes

            // ,Authentication authentication
            ) {

        try {

        	Long memberId = LOGIN_MEMBER_ID;
    		
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

        return advertisementService.selectAdvertisementOne(dto.getAdId());
    }

    // 상세
    @Operation(
	    summary = "광고 상세 조회",
	    description = "광고주의 광고 상세 정보를 조회합니다."
	)
	@GetMapping("/{adId}")
	public ResponseEntity<AdvertisementDto> detail(
	        @PathVariable Long adId) {

	    AdvertisementDto dto =
	            advertisementService.selectAdvertisementOne(adId);

	    if (dto == null) {
	        throw new ResponseStatusException(
	                HttpStatus.NOT_FOUND,
	                "광고를 찾을 수 없습니다."
	        );
	    }

	    if (!LOGIN_MEMBER_ID.equals(dto.getAdvertiserId())) {
	        throw new ResponseStatusException(
	                HttpStatus.FORBIDDEN,
	                "본인의 광고만 조회할 수 있습니다."
	        );
	    }

	    return ResponseEntity.ok(dto);
	}

    // 수정
    @Operation(
	    summary = "광고 수정",
	    description = "광고 정보를 수정하고 필요한 경우 광고 이미지를 변경합니다."
	)
    @PutMapping(
	    value = "/{adId}",
	    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
	)
    public ResponseEntity<AdvertisementDto> editAction(

    		@PathVariable Long adId,

            @ModelAttribute AdvertisementDto dto,

            @RequestParam(value = "imageFiles", required = false)
            List<MultipartFile> imageFiles,

            @RequestParam(value = "imageTypes", required = false)
            List<String> imageTypes
        ) {
    	
    	dto.setAdId(adId);

    	Long memberId = LOGIN_MEMBER_ID;

		AdvertisementDto origin =
		        advertisementService.selectAdvertisementOne(dto.getAdId());

		if (origin == null) {
		    throw new ResponseStatusException(
		            HttpStatus.NOT_FOUND,
		            "광고를 찾을 수 없습니다."
		    );
		}

		if (!Objects.equals(memberId, origin.getAdvertiserId())) {
		    throw new ResponseStatusException(
		            HttpStatus.FORBIDDEN,
		            "본인의 광고만 수정할 수 있습니다."
		    );
		}

		// 수정 DTO에는 서버에서 직접 주입
		dto.setAdvertiserId(memberId);
		dto.setAdvertiserId(LOGIN_MEMBER_ID);

		advertisementService.updateAdvertisement(
		        dto,
		        imageFiles,
		        imageTypes
		);

		return ResponseEntity.ok(
	            advertisementService.selectAdvertisementOne(adId)
	    );
    }

    // 삭제
    @Operation(
	    summary = "광고 삭제",
	    description = "광고와 연결된 이미지 정보를 삭제합니다."
	)
    @DeleteMapping("/{adId}")
    public ResponseEntity<Void> delete(
    		@PathVariable Long adId
        ) {

        AdvertisementDto dto =
                advertisementService.selectAdvertisementOne(adId);

        // 권한 체크
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        if (!LOGIN_MEMBER_ID.equals(dto.getAdvertiserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // 서비스에서 파일 + 이미지DB + 광고 삭제 모두 처리
        advertisementService.deleteAdvertisement(adId);

        return ResponseEntity.noContent().build();
    }


//    // 광고 클릭
//    @GetMapping("/click")
//    public String click(
//            @RequestParam Long adId,
//            @RequestParam String position,
//            HttpServletRequest request,
//            HttpSession session) {
//
//
//    	// 클릭 로그 확인 (1시간에 한번만 +1 인정)
//    	boolean counted = advertisementService.insertClickLog(
//    	        adId,
//    	        position,
//    	        request,
//    	        session
//    	);
//    	// 한시간 내에 기록 x면 증가
//    	if (counted) {
//    	    advertisementService.updateAdvertisementClick(adId);
//    	}
//
//        AdvertisementDto dto =
//                advertisementService.selectAdvertisementOne(adId);
//
//
//        if(dto == null || dto.getLandingUrl() == null){
//            return "redirect:/";
//        }
//
//
//        return "redirect:" + dto.getLandingUrl();
//    }
    
    // 광고 기간 연장 신청
//    @PostMapping("/extensionRequest")
//    @ResponseBody
//    public ResponseEntity<?> extensionRequest(
//            @RequestBody AdvertisementExtensionRequestDto dto,
//            Authentication authentication) {
//
//        Long memberId = getLoginMemberId(authentication);
//			
//			advertisementService.requestExtension(
//			    dto,
//			    memberId
//			);
//
//        return ResponseEntity.ok().build();
//    }
}