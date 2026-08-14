package com.moit.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moit.member.dto.UserDto;
import com.moit.member.dto.UserRequestDto;
import com.moit.member.dto.UserResponseDto;
import com.moit.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberRestController {
	
	private final MemberService memberService;
	
	//회원가입
	@Operation( summary = "회원가입", description = "새로운 회원을 등록합니다." )
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(
            @RequestBody UserRequestDto  request) {

		UserDto dto = request.toUserDto();

	    UserDto result = memberService.signup(dto);

	    return ResponseEntity
	    		.status(HttpStatus.CREATED)
	    		.body(UserResponseDto.from(result));
    }
	
	// 아이디 중복검사
    @Operation( summary = "아이디 중복검사", description = "사용 중인 아이디인지 확인합니다." )
    @GetMapping("/check-loginId")
    public ResponseEntity<Boolean> checkLoginId(
            @Parameter(description = "확인할 아이디")
            @RequestParam String loginId) {

        return ResponseEntity.ok( memberService.existsByLoginId(loginId) );
    }

    // 이메일 중복검사
    @Operation( summary = "이메일 중복검사", description = "사용 중인 이메일인지 확인합니다." )
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(
            @Parameter(description = "확인할 이메일")
            @RequestParam String email) {

        return ResponseEntity.ok( memberService.existsByEmail(email) );
    }

    // 닉네임 중복검사
    @Operation( summary = "닉네임 중복검사", description = "사용 중인 닉네임인지 확인합니다." )
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(
            @Parameter(description = "확인할 닉네임")
            @RequestParam String nickname) {

        return ResponseEntity.ok( memberService.existsByNickname(nickname) );
    }

    // 전화번호 중복검사
    @Operation( summary = "전화번호 중복검사", description = "사용 중인 전화번호인지 확인합니다." )
    @GetMapping("/check-mobile")
    public ResponseEntity<Boolean> checkMobile(
            @Parameter(description = "확인할 전화번호")
            @RequestParam String mobile) {

        return ResponseEntity.ok( memberService.existsByMobile(mobile) );
    }
}
