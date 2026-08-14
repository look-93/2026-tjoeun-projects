package com.moit.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.moit.member.dto.LoginRequestDto;
import com.moit.member.dto.LoginResponseDto;
import com.moit.member.dto.RefreshRequestDto;
import com.moit.member.dto.RefreshResponseDto;
import com.moit.member.dto.UserDto;
import com.moit.member.dto.UserRequestDto;
import com.moit.member.dto.UserResponseDto;
import com.moit.member.service.MemberService;
import com.moit.security.CustomUserDetails;
import com.moit.security.JwtTokenProvider;
import com.moit.security.RefreshTokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberRestController {
	
	private final MemberService service;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenService refreshTokenService;
	
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
	
	// 아이디 중복검사
    @Operation( summary = "아이디 중복검사", description = "사용 중인 아이디인지 확인합니다." )
    @GetMapping("/check-loginId")
    public ResponseEntity<Boolean> checkLoginId(
            @Parameter(description = "확인할 아이디")
            @RequestParam("loginId") String loginId) {

        return ResponseEntity.ok( service.existsByLoginId(loginId) );
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
    @Operation( summary = "로그인", description = "아이디와 비밀번호를 확인하고 JWT 발급" )
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login( @RequestBody LoginRequestDto request) {
    	
    	//1. 아이디로 회원조회
    	UserDto user = service.findByLoginId(request.getLoginId());
    	
    	//2. 회원이 존재하지 않는 경우
    	if(user == null) {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    	}
    	
    	// 회원 탈퇴/정지 상태 확인
    	if (user.getStatusId() == null || !user.getStatusId().equals(1L)) {
    	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    	}
    	
    	//3. 비밀번호 확인
    	boolean passwordMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
    	
    	//4. 비밀번호 틀린경우
    	if(!passwordMatch) {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    	}
    	
    	//5. Access Token 생성
    	String accessToken = jwtTokenProvider.createAccessToken(user.getMemberId(), user.getLoginId());
    	
    	//6. refresh Token 생성
    	String refreshToken = jwtTokenProvider.createRefreshToken(user.getMemberId());
    	
    	// Refresh Token Redis 저장
    	refreshTokenService.saveRefreshToken(
    	        user.getMemberId(),
    	        refreshToken,
    	        jwtTokenProvider.getRefreshTokenExpiration());
    	
    	//7. 로그인 응답
    	LoginResponseDto response = new LoginResponseDto(accessToken,
    													refreshToken,
    													user.getMemberId(),
    													user.getLoginId(),
    													user.getMemberTypeId());
    	
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
    @Operation( summary = "회원정보 수정", description = "로그인한 회원정보 수정" )
    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateMyInfo(
    		@RequestBody UserRequestDto request,
    		Authentication authentication
    		) {
    	//1. JWT에서 로그인 회원정보 가져오기
    	CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    	
    	Long memberId = userDetails.getAppUserId();
    	
    	//2. 수정할 데이터 DTO
    	UserDto dto = request.toUserDto();
    	
    	dto.setMemberId(memberId);
    	
    	//3. 회원정보 수정
    	UserDto result = service.updateMember(memberId,dto);
    	
    	return ResponseEntity.ok(UserResponseDto.from(result));
    	
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
    
    // 테스트
    @GetMapping("/member-test")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<String> memberTest() {
        return ResponseEntity.ok("일반 회원 접근 성공");
    }
    
    @GetMapping("/partner-test")
    @PreAuthorize("hasRole('PARTNER')")
    public ResponseEntity<String> partnerTest() {
        return ResponseEntity.ok("제휴 업체 접근 성공");
    }
    
    
}
