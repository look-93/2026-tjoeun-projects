package com.moit.advertisement.controller;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.moit.advertisement.dto.AdvertisementDto;
import com.moit.advertisement.dto.AdvertisementImageDto;
import com.moit.advertisement.dto.AdvertisementPaymentDto;
import com.moit.advertisement.dto.AdvertisementSearchDto;
import com.moit.advertisement.dto.PaymentConfirmRequestDto;
import com.moit.advertisement.enums.AdPosition;
import com.moit.advertisement.enums.PaymentType;
import com.moit.advertisement.repository.AdvertisementPaymentRepository;
import com.moit.advertisement.service.AdvertisementCalculationService;
import com.moit.advertisement.service.AdvertisementService;
import com.moit.advertisement.service.TossPaymentService;
import com.moit.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
    private final AdvertisementCalculationService calculationService;
    private final TossPaymentService tossPaymentService;
    private final AdvertisementPaymentRepository advertisementPaymentRepository;

    private static final String UPLOAD_PATH = "C:/upload/ad/";
    
    
    // 사용자 id
    private Long getLoginMemberId(Authentication authentication) {
    	if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal()
                        instanceof CustomUserDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return user.getUser().getMemberId(); 
    }

    // 내 광고 목록
    @Operation(
	    summary = "내 광고 목록 조회",
	    description = "로그인한 광고주의 광고 목록을 페이지 단위로 조회합니다."
	)
    @GetMapping
    public ResponseEntity<AdvertisementDto.AdvertisementPageResponseDto> list(
            AdvertisementSearchDto dto,
            Authentication authentication) {

    	Long memberId = getLoginMemberId(authentication);

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
    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
	public AdvertisementDto writeAction(

		@ModelAttribute("dto")
	    AdvertisementDto.AdvertisementRequestDto dto,

	    @RequestPart( value = "imageFiles", required = false )
	    List<MultipartFile> imageFiles,

	    @RequestParam(value = "imageTypes", required = false)
	    List<String> imageTypes,
	    Authentication authentication) {

        try {
        	Long memberId = getLoginMemberId(authentication);

            // 광고 등록
        	List<AdPosition> positions = new ArrayList<>();
            if (imageTypes != null && !imageTypes.isEmpty()) {
                positions = imageTypes.stream()
                        .map(String::toUpperCase)
                        .map(AdPosition::valueOf)
                        .collect(Collectors.toList());
            }

            // 백엔드 로직으로 총 예산(Total Budget) 계산
            BigDecimal calculatedBudget = calculationService.calculateTotalAmount(
                    dto.getStartDatetime(),
                    dto.getEndDatetime(),
                    dto.getAdGrade(),
                    PaymentType.INITIAL, // 최초 등록
                    positions
            );

            // DTO에 계산된 예산 꽂아넣기
            dto.setTotalBudget(calculatedBudget);

            // 광고 DB 등록
            Long adId = advertisementService.insertAdvertisement(dto, memberId);

            // 이미지 등록
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

                    AdvertisementImageDto imageDto = new AdvertisementImageDto();
                    
                    imageDto.setAdId(adId);
                    imageDto.setImageType(imageTypes.get(i));
                    imageDto.setImageUrl("/upload/ad/" + saveName);

                    advertisementService.insertAdvertisementImage(imageDto);
                }
            }
            return advertisementService.selectAdvertisementOne(adId);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 상세
    @Operation(
	    summary = "광고 상세 조회",
	    description = "광고주의 광고 상세 정보를 조회합니다."
	)
	@GetMapping("/{adId}")
	public ResponseEntity<AdvertisementDto> detail(
			@PathVariable("adId") Long adId,
            Authentication authentication) {

    	Long memberId = getLoginMemberId(authentication);
	    AdvertisementDto dto = advertisementService.selectAdvertisementOne(adId);

	    if (dto == null) {
	        throw new ResponseStatusException(
	                HttpStatus.NOT_FOUND,
	                "광고를 찾을 수 없습니다."
	        );
	    }

	    if (!Objects.equals(memberId, dto.getAdvertiserId())) {
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
    		
    		@PathVariable("adId") Long adId,
    		
    		@ModelAttribute("dto")
    		AdvertisementDto.AdvertisementUpdateRequestDto dto,

    	    @RequestPart( value = "imageFiles", required = false )
            List<MultipartFile> imageFiles,

            @RequestParam(value = "imageTypes", required = false)
            List<String> imageTypes,
            
            Authentication authentication
        ) {

    	Long memberId = getLoginMemberId(authentication);
        AdvertisementDto origin = advertisementService.selectAdvertisementOne(adId);

		if (origin == null) {
		    throw new ResponseStatusException(
		            HttpStatus.NOT_FOUND,
		            "광고를 찾을 수 없습니다."
		    );
		}

		// 광고주 본인 확인
		if (!Objects.equals(memberId, origin.getAdvertiserId())) {
		    throw new ResponseStatusException(
		            HttpStatus.FORBIDDEN,
		            "본인의 광고만 수정할 수 있습니다."
		    );
		}

		// 수정 시에도 금액이 변동될 수 있으므로 다시 계산
        List<AdPosition> positions = new ArrayList<>();
        if (imageTypes != null && !imageTypes.isEmpty()) {
            positions = imageTypes.stream()
                    .map(String::toUpperCase)
                    .map(AdPosition::valueOf)
                    .collect(Collectors.toList());
        }

        BigDecimal calculatedBudget = calculationService.calculateTotalAmount(
                dto.getStartDatetime(),
                dto.getEndDatetime(),
                dto.getAdGrade(),
                PaymentType.INITIAL, // 결제 전 수정이므로 여전히 INITIAL 성격
                positions
        );

        dto.setTotalBudget(calculatedBudget); // 수정된 금액 세팅

        // DB 업데이트 처리
		advertisementService.updateAdvertisement(
	            adId,
	            memberId,
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
    		@PathVariable("adId") Long adId,
            Authentication authentication
        ) {
    	
    	Long memberId = getLoginMemberId(authentication);
        AdvertisementDto dto = advertisementService.selectAdvertisementOne(adId);

        // 권한 체크
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        if (!Objects.equals(memberId, dto.getAdvertiserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // 서비스에서 파일 + 이미지DB + 광고 삭제 모두 처리
        advertisementService.deleteAdvertisement(adId, memberId);

        return ResponseEntity.noContent().build();
    }
    
	 // =========================================================
	 // 최초 결제 생성
	 // =========================================================
	 @Operation(
	     summary = "광고 최초 결제 생성",
	     description = "결제하기 요청 시 광고 결제 정보를 생성하고 Toss 결제에 사용할 주문번호를 반환합니다."
	 )
	 @PostMapping("/payment/initial/{adId}")
	 public ResponseEntity<AdvertisementPaymentDto> createInitialPayment(
	         @PathVariable("adId") Long adId,
	         Authentication authentication) {
	
	     Long memberId = getLoginMemberId(authentication);
	
	     AdvertisementPaymentDto payment =
	             advertisementService.createInitialPayment(
	                     adId,
	                     memberId
	             );
	
	     return ResponseEntity.ok(payment);
	 }
	 
	 @GetMapping("/extension-prices")
	 public ResponseEntity<?> getExtensionPrices(
	         @RequestParam Long adId
	 ) {

	     return ResponseEntity.ok(
	             advertisementService.getExtensionPrices(adId)
	     );
	 }
    
    // =========================================================
    // 토스 결제 최종 승인
    // =========================================================
    @Operation(summary = "결제 승인 (Confirm)", description = "프론트엔드 결제 성공 후 토스 서버에 최종 승인을 요청합니다.")
    @PostMapping("/payment/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody PaymentConfirmRequestDto requestDto) {
        try {
            // 결제 승인 서비스 호출
            tossPaymentService.confirmPayment(requestDto);
            return ResponseEntity.ok().body("결제가 성공적으로 완료되었습니다.");
            
        } catch (IllegalArgumentException e) {
            // 금액 불일치 등 클라이언트 측 예외
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // 토스 서버 응답 실패 등 서버 측 예외
            return ResponseEntity.internalServerError().body("결제 승인 실패: " + e.getMessage());
        }
    }


    // 광고 클릭
    @PostMapping("/click")
    public ResponseEntity<Void> increaseClick(
            @RequestParam("adId") Long adId,
            @RequestParam(name = "position") String position,
            Authentication authentication,
            HttpServletRequest request) {

        Long memberId = null;

        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal()
                        instanceof CustomUserDetails) {

            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

            memberId = user.getUser().getMemberId();
        }

        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String referrer = request.getHeader("Referer");

        advertisementService.insertClickLog(
                adId,
                position,
                memberId,
                ip,
                userAgent,
                referrer
        );

        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/impression")
    public ResponseEntity<Void> increaseImpression(
    		@RequestParam(name = "adId") Long adId,
            @RequestParam(name = "position") String position,
            Authentication authentication,
            HttpServletRequest request) {

        Long memberId = null;

        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof CustomUserDetails) {

            CustomUserDetails user =
                    (CustomUserDetails) authentication.getPrincipal();

            memberId = user.getUser().getMemberId();
        }

        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        advertisementService.insertImpressionLog(
                adId,
                position,
                memberId,
                ip,
                userAgent
        );

        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/top")
    public ResponseEntity<AdvertisementDto> getTopAdvertisement(
            @RequestParam(name = "position") String position,
            Authentication authentication,
            HttpServletRequest request) {

        Long memberId = null;

        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof CustomUserDetails) {

            CustomUserDetails user =
                    (CustomUserDetails) authentication.getPrincipal();

            memberId = user.getUser().getMemberId();
        }

        // 비로그인 사용자도 광고 피로도를 계산할 수 있도록 세션 사용
        String sessionId = request.getSession().getId();

        AdvertisementDto advertisement =
                advertisementService.selectAdvertisement(
                        position,
                        memberId,
                        sessionId
                );

        if (advertisement == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(advertisement);
    }
}