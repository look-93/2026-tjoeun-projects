package com.moit.member.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.moit.member.dto.LoginRequestDto;
import com.moit.member.dto.LoginResponseDto;
import com.moit.member.dto.MyPageDto;
import com.moit.member.dto.RefreshRequestDto;
import com.moit.member.dto.RefreshResponseDto;
import com.moit.member.dto.ResetPasswordDto;
import com.moit.member.dto.UserDto;
import com.moit.member.dto.UserRequestDto;
import com.moit.member.dto.UserResponseDto;
import com.moit.member.dto.UserUpdateRequestDto;
import com.moit.member.service.MemberService;
import com.moit.member.service.VerificationService;
import com.moit.security.CustomUserDetails;
import com.moit.security.JwtTokenProvider;
import com.moit.security.PasswordLeakService;
import com.moit.security.RefreshTokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberRestController {
	
	private final MemberService service;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenService refreshTokenService;
	private final PasswordLeakService passwordLeakService;
	private final VerificationService verificationService;
	
	//회원가입
	@Operation( summary = "회원가입", description = "새로운 회원을 등록합니다." )
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(
            @RequestBody UserRequestDto  request) {

		UserDto dto = request.toUserDto();

	    UserDto result = service.signup(dto);

	    return ResponseEntity
	    		.status(HttpStatus.CREATED)
	    		.body(UserResponseDto.from(result));
    }
	// 소셜 로그인 추가정보 조회
	@Operation( summary = "소셜 회원 추가정보 조회", description = "OAuth2 로그인 후 세션에 저장된 소셜 회원정보를 조회합니다." )
	@GetMapping("/social-info")
	public ResponseEntity<?> getSocialInfo(HttpSession session) {
		
		System.out.println("===== SOCIAL INFO API =====");
		
	    UserDto socialUser = (UserDto) session.getAttribute("socialUser");

	    if (socialUser == null) {
	    	System.out.println("socialUser 없음");
	    	
	        return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .body(Map.of(
	                        "message",
	                        "소셜 회원가입 정보가 없습니다."
	                ));
	    }
	    
	    System.out.println("socialUser 있음");
	    System.out.println("email : " + socialUser.getEmail());
	    System.out.println("provider : " + socialUser.getProvider());

	    return ResponseEntity.ok(
	            Map.of(
	            		"email", socialUser.getEmail(),
	                    "nickname", socialUser.getNickname(),
	                    "provider", socialUser.getProvider(),
	                    "profileUrl",
	                    socialUser.getProfileUrl() == null
	                            ? ""
	                            : socialUser.getProfileUrl()
	            )
	    );
	}
	
	// 소셜 회원가입 추가정보 저장
	@Operation( summary = "소셜 회원가입 완료", description = "OAuth2 로그인 후 추가정보를 입력받아 회원가입을 완료하고 JWT를 발급합니다." )
	@PostMapping("/social-info")
	public ResponseEntity<?> socialSignup(
	        @RequestBody UserRequestDto request,
	        HttpSession session) {

	    // 1. 세션에서 OAuth2 회원정보 가져오기
	    UserDto socialUser = (UserDto) session.getAttribute("socialUser");

	    if (socialUser == null) {
	        return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .body(Map.of(
	                        "message",
	                        "소셜 회원가입 정보가 만료되었거나 존재하지 않습니다."
	                ));
	    }

	    // 2. 프론트에서 입력한 추가정보
	    UserDto dto = request.toUserDto();

	    // 3. OAuth2 로그인 정보 세팅
	    dto.setEmail(socialUser.getEmail());
	    dto.setProvider(socialUser.getProvider());
	    dto.setProviderId(socialUser.getProviderId());
	    dto.setProfileUrl(socialUser.getProfileUrl());

	    // 4. 소셜 회원가입
	    UserDto result = service.socialSignup(dto);

	    // 5. 실제 회원 ID
	    Long memberId = result.getMemberId();

	    // 6. Access Token 발급
	    String accessToken =
	            jwtTokenProvider.createAccessToken(
	                    memberId,
	                    result.getLoginId()
	            );

	    // 7. Refresh Token 발급
	    String refreshToken = jwtTokenProvider.createRefreshToken(memberId);

	    // 8. Redis에 Refresh Token 저장
	    refreshTokenService.saveRefreshToken(
	            memberId,
	            refreshToken,
	            jwtTokenProvider.getRefreshTokenExpiration()
	    );

	    // 9. 소셜 회원가입용 세션 삭제
	    session.removeAttribute("socialUser");

	    // 10. 응답
	    return ResponseEntity.ok(
	            new LoginResponseDto(
	                    accessToken,
	                    refreshToken,
	                    result.getMemberId(),
	                    result.getLoginId(),
	                    result.getMemberTypeId()
	            )
	    );
	}
		
	// 아이디 중복검사
    @Operation( summary = "아이디 중복검사", description = "사용 중인 아이디인지 확인합니다." )
    @GetMapping("/check-loginId")
    public ResponseEntity<Boolean> checkLoginId(
            @Parameter(description = "확인할 아이디")
            @RequestParam("loginId") String loginId) {

        return ResponseEntity.ok( service.existsByLoginId(loginId) );
    }
    
    // 비밀번호 유출 여부 확인
    @Operation( summary = "비밀번호 유출 여부 확인", description = "HIBP API를 이용하여 비밀번호 유출 여부를 확인합니다." )
    @PostMapping("/check-password")
    public ResponseEntity<Map<String, Object>> checkPassword(
            @RequestBody Map<String, String> request) {

        String password = request.get("password");

        if (password == null || password.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "비밀번호를 입력해주세요."));
        }

        int leakCount = passwordLeakService.getLeakCount(password);

        if (leakCount == -1) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "비밀번호 보안 검증에 실패했습니다."));
        }

        return ResponseEntity.ok(
                Map.of("leaked", leakCount > 0,
                       "count", leakCount)
        );
    }

    // 이메일 중복검사
    @Operation( summary = "이메일 중복검사", description = "사용 중인 이메일인지 확인합니다." )
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(
            @Parameter(description = "확인할 이메일")
            @RequestParam("email") String email) {

        return ResponseEntity.ok( service.existsByEmail(email) );
    }

    // 닉네임 중복검사
    @Operation( summary = "닉네임 중복검사", description = "사용 중인 닉네임인지 확인합니다." )
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(
            @Parameter(description = "확인할 닉네임")
            @RequestParam("nickname") String nickname) {

        return ResponseEntity.ok( service.existsByNickname(nickname) );
    }

    // 전화번호 중복검사
    @Operation( summary = "전화번호 중복검사", description = "사용 중인 전화번호인지 확인합니다." )
    @GetMapping("/check-mobile")
    public ResponseEntity<Boolean> checkMobile(
            @Parameter(description = "확인할 전화번호")
            @RequestParam("mobile") String mobile) {

        return ResponseEntity.ok( service.existsByMobile(mobile) );
    }
    
    // 로그인
    @Operation(
            summary = "로그인",
            description = "아이디, 비밀번호, 회원유형을 확인하고 JWT 발급"
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequestDto request) {

        // 1. 아이디로 회원조회
        UserDto user = service.findByLoginId(request.getLoginId());

        // 2. 회원이 존재하지 않는 경우
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
        
        // 3. 비밀번호 확인
        boolean passwordMatch = passwordEncoder.matches( request.getPassword(), user.getPassword() );

        // 4. 비밀번호 틀린 경우
        if (!passwordMatch) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

	    // 5. 회원 상태 확인
	    // 승인 대기
	    if (user.getStatusId() != null && user.getStatusId().equals(2L)) {
	
	        return ResponseEntity
	                 .status(HttpStatus.FORBIDDEN)
	                 .body(Map.of( "message", "관리자 승인 대기중입니다." ));
	    }
	
	    // 탈퇴 / 정지 / 기타 비활성 상태
	    if (user.getStatusId() == null || !user.getStatusId().equals(1L)) {
	
	        return ResponseEntity
	                 .status(HttpStatus.UNAUTHORIZED)
	                 .body(Map.of( "message", "로그인할 수 없는 회원입니다." ));
	    }
       
        // 6. 회원 유형 확인
        if (user.getMemberTypeId() == null || !user.getMemberTypeId().equals(request.getMemberTypeId())) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of( "message", "회원유형이 맞지 않습니다." ));
        }

        // 7. Access Token 생성
        String accessToken =
                jwtTokenProvider.createAccessToken(
                        user.getMemberId(),
                        user.getLoginId()
                );

        // 8. Refresh Token 생성
        String refreshToken =
                jwtTokenProvider.createRefreshToken(
                        user.getMemberId()
                );

        // 9. Refresh Token Redis 저장
        refreshTokenService.saveRefreshToken(
                user.getMemberId(),
                refreshToken,
                jwtTokenProvider.getRefreshTokenExpiration()
        );

        // 10. 로그인 응답
        LoginResponseDto response =
                new LoginResponseDto(
                        accessToken,
                        refreshToken,
                        user.getMemberId(),
                        user.getLoginId(),
                        user.getMemberTypeId()
                );

        return ResponseEntity.ok(response);
    }
    
    // Access Token 재발급
    @Operation( summary = "Access Token 재발급", description = "새로운 Access Token을 발급합니다." )
    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponseDto> refresh(
    		 @RequestBody RefreshRequestDto request) {
    	String refreshToken = request.getRefreshToken();
    	
    	//1. Refresh Token 검증
    	if(!jwtTokenProvider.validateToken(refreshToken)) {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    	}
    	
    	// refresh Token 검사
    	if(!"REFRESH".equals(jwtTokenProvider.getTokenType(refreshToken))) {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    	}
    	
    	//2. refresh Token에서 회원ID 추출
    	Long memberId = jwtTokenProvider.getMemberId(refreshToken);
    	
    	//3. Redis에 저장된 Refresh Token과 비교
    	if(!refreshTokenService.validateRefreshToken(memberId, refreshToken)) {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    	}
    	
    	//4. 회원 조회
    	UserDto user = service.findByMemberId(memberId);
    	
    	if(user == null) {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    	}
    	
    	//5. 새로운 access Token 발급
    	String accessToken = jwtTokenProvider.createAccessToken(user.getMemberId(), user.getLoginId());
    	
    	//6. 새로운 refresh Token 발급
    	String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getMemberId());
    	
    	//7. redis의 refresh Token 교체
    	refreshTokenService.saveRefreshToken(user.getMemberId(), 
    										 newRefreshToken, 
    										 jwtTokenProvider.getRefreshTokenExpiration());
    	
    	return ResponseEntity.ok(new RefreshResponseDto(accessToken,newRefreshToken));
    }
    
    // 로그아웃
    @Operation( summary = "로그아웃", description = "Redis에 저장된 Token 삭제" )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication) {
    	
    	CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    	
    	Long memberId = userDetails.getAppUserId();
    	
    	refreshTokenService.deleteRefreshToken(memberId);
    	
    	return ResponseEntity.ok().build();
    }
    
    // 회원정보 수정
 // 회원정보 수정
    @Operation(
            summary = "회원정보 수정",
            description = "로그인한 회원의 회원정보와 프로필 이미지를 수정합니다."
    )
    @PutMapping(value = "/me", consumes = "multipart/form-data")
    public ResponseEntity<?> updateMyInfo(
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String mobile,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String birth,
            @RequestParam(required = false) List<Integer> interestIds,
            @RequestParam(required = false) MultipartFile profileImage,
            Authentication authentication
    ) {

        try {

            // 1. 로그인 회원 ID
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            Long memberId = userDetails.getAppUserId();

            // 2. DTO 생성
            UserDto dto = new UserDto();

            dto.setNickname(nickname);
            dto.setMobile(mobile);
            dto.setGender(gender);

            // birth가 들어온 경우에만 변환
            if (birth != null && !birth.isBlank()) {
                dto.setBirth(java.time.LocalDate.parse(birth));
            }

            dto.setInterestIds(interestIds);

            // 3. 프로필 이미지 처리
            if (profileImage != null && !profileImage.isEmpty()) {

                String contentType = profileImage.getContentType();

                if (contentType == null || !contentType.startsWith("image/")) {

                    return ResponseEntity
                            .badRequest().body(Map.of( "message", "이미지 파일만 업로드할 수 있습니다." ));
                }

                String originalFilename = profileImage.getOriginalFilename();

                String extension = "";

                if (originalFilename != null && originalFilename.contains(".")) {

                    extension = originalFilename.substring( originalFilename.lastIndexOf(".") );
                }

                String savedFilename = UUID.randomUUID() + extension;

                // 업로드 폴더
                Path uploadPath = Paths.get("uploads/profile");

                Files.createDirectories(uploadPath);

                // 실제 파일 저장
                Path filePath = uploadPath.resolve(savedFilename);

                Files.write( filePath, profileImage.getBytes() );

                // DB 저장 URL
                String profileUrl = "/images/profile/" + savedFilename;  
                
                dto.setProfileUrl(profileUrl);
            }

            // 4. 회원정보 + 이미지 수정
            UserDto result = service.updateMember(memberId, dto);

            // 5. 응답
            return ResponseEntity.ok( UserResponseDto.from(result) );

        } catch (java.time.format.DateTimeParseException e) {

            return ResponseEntity
                    .badRequest().body(Map.of( "message", "생년월일 형식이 올바르지 않습니다." ));

        } catch (IOException e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of( "message", "프로필 이미지 업로드에 실패했습니다." ));

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest().body(Map.of( "message", e.getMessage() ));
        }
    }
    
    //회원탈퇴(논리삭제)
    @Operation( summary = "회원 탈퇴", description = "로그인한 회원 탈퇴처리" )
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount(Authentication authentication) {
    	
    	//1. JWT 인증 정보에서 회원 ID 가져오기
    	CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    	
    	Long memberId = userDetails.getAppUserId();
    	
    	//2. 회원 탈퇴 처리
    	service.deleteMember(memberId);
    	
    	//3. redis refresh Token 삭제
    	refreshTokenService.deleteRefreshToken(memberId);
    	
    	return ResponseEntity.ok().build();    	
    }
    
    // 회원 전체 조회
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAllMembers() {

        List<UserDto> members = service.findAllMembers();

        List<UserResponseDto> response = members.stream()
                .map(UserResponseDto::from)
                .toList();

        return ResponseEntity.ok(response);
    }
    
    // 현재 로그인한 회원정보 조회
    @Operation(
        summary = "내 회원정보 조회",
        description = "JWT 인증된 현재 로그인 회원의 정보를 조회합니다."
    )
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyInfo(
            Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Long memberId = userDetails.getAppUserId();

        UserDto user = service.findByMemberId(memberId);

        if (user == null) {
            return ResponseEntity .status(HttpStatus.UNAUTHORIZED) .build();
        }
        return ResponseEntity.ok( UserResponseDto.from(user) );
    }
    
    // 마이페이지 조회
    @Operation( summary = "마이페이지 조회", description = "JWT 인증된 현재 로그인 회원의 마이페이지 정보를 조회합니다." )
    @GetMapping("/mypage")
    public ResponseEntity<MyPageDto> getMyPage( Authentication authentication) {

        // 1. JWT 인증 정보에서 회원 ID 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Long memberId = userDetails.getAppUserId();

        // 2. 마이페이지 정보 조회
        MyPageDto myPage = service.getMyPage(memberId);

        // 3. 회원이 없는 경우
        if (myPage == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(myPage);
    }
    
    // 아이디 찾기
    @Operation( summary = "아이디 찾기", description = "이메일 인증이 완료된 회원의 아이디를 조회합니다." )
    @PostMapping("/find-id")
    public ResponseEntity<?> findLoginId( @RequestBody Map<String, String> request) {

        String email = request.get("email");

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of( "message", "이메일을 입력해주세요." ));
        }

        try {
            String loginId = service.findLoginIdByEmail(email);

            return ResponseEntity.ok( Map.of( "loginId", loginId ) );
            
        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(Map.of( "message", e.getMessage() ));
        }
    }
    
    // 비밀번호 찾기
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword( @RequestBody ResetPasswordDto dto) {

        try {
            String email = dto.getEmail();
            String password = dto.getPassword();

            // 1. 이메일 인증 여부 확인
            if (!verificationService.isEmailVerified(email)) {
                return ResponseEntity .status(HttpStatus.FORBIDDEN) .body(Map.of( "message", "이메일 인증이 필요합니다." ));
            }

            // 2. 비밀번호 변경
            service.resetPassword(email, password);

            // 3. 인증 완료 상태 삭제
            verificationService.removeEmailVerified(email);

            return ResponseEntity.ok( Map.of( "message", "비밀번호가 변경되었습니다." ) );

        } catch (IllegalArgumentException e) {

            return ResponseEntity .badRequest() .body(Map.of( "message", e.getMessage() ));
        }
    }
    
    // 비밀번호 변경(로그인한 유저)
    @PutMapping("/me/password")
    public ResponseEntity<?> changePassword(
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        try {

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            Long memberId = userDetails.getAppUserId();

            String currentPassword = request.get("currentPassword");
            String newPassword = request.get("newPassword");

            if (currentPassword == null || currentPassword.isBlank()) {
                return ResponseEntity.badRequest() .body(Map.of( "message", "현재 비밀번호를 입력해주세요." ));
            }

            if (newPassword == null || newPassword.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of( "message", "새 비밀번호를 입력해주세요." ));
            }

            service.changePassword( memberId, currentPassword, newPassword );

            return ResponseEntity.ok( Map.of( "message", "비밀번호가 변경되었습니다." ) );

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest() .body(Map.of( "message", e.getMessage() ));
        }
    }
    
    // 프로필 이미지 수정
//    @Operation( summary = "프로필 이미지 수정", description = "로그인한 회원의 프로필 이미지를 업로드하고 변경합니다." )
//    @PostMapping("/me/profile-image")
//    public ResponseEntity<?> updateProfileImage(
//            @RequestParam("file") MultipartFile file,
//            Authentication authentication) {
//
//        try {
//
//            // 1. 로그인 회원 ID
//            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//
//            Long memberId = userDetails.getAppUserId();
//
//            // 2. 파일 존재 여부 확인
//            if (file == null || file.isEmpty()) {
//                return ResponseEntity
//                        .badRequest()
//                        .body(Map.of( "message", "프로필 이미지를 선택해주세요." ));
//            }
//
//            // 3. 이미지 파일인지 확인
//            String contentType = file.getContentType();
//
//            if (contentType == null || !contentType.startsWith("image/")) {
//                return ResponseEntity
//                        .badRequest()
//                        .body(Map.of( "message", "이미지 파일만 업로드할 수 있습니다." ));
//            }
//
//            // 4. 확장자 추출
//            String originalFilename = file.getOriginalFilename();
//
//            String extension = "";
//
//            if (originalFilename != null && originalFilename.contains(".")) {
//                extension = originalFilename.substring( originalFilename.lastIndexOf(".") );
//            }
//
//            // 5. UUID로 파일명 생성
//            String savedFilename = UUID.randomUUID() + extension;
//
//            // 6. 저장 경로
//            Path uploadPath = Paths.get("uploads/profile");
//
//            Files.createDirectories(uploadPath);
//
//            // 7. 실제 파일 저장
//            Path filePath = uploadPath.resolve(savedFilename);
//
//            Files.write( filePath, file.getBytes() );
//
//            // 8. DB에 저장할 URL
//            String profileUrl = "/images/profile/" + savedFilename;
//
//            // 9. 회원정보 업데이트
//            service.updateProfileImage( memberId, profileUrl );
//
//            // 10. 응답
//            return ResponseEntity.ok(
//                    Map.of( "message", "프로필 이미지가 변경되었습니다.", "profileUrl", profileUrl ) );
//
//        } catch (IOException e) {
//
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of( "message", "프로필 이미지 업로드에 실패했습니다." )); }
//    }
    
    
}
