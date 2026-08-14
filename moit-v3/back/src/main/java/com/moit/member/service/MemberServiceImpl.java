package com.moit.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moit.member.dto.UserDto;
import com.moit.member.entity.Interest;
import com.moit.member.entity.Member;
import com.moit.member.entity.MemberInfo;
import com.moit.member.entity.MemberInterest;
import com.moit.member.entity.MemberInterestId;
import com.moit.member.entity.MemberStatus;
import com.moit.member.entity.MemberType;
import com.moit.member.repository.InterestRepository;
import com.moit.member.repository.MemberInfoRepository;
import com.moit.member.repository.MemberInterestRepository;
import com.moit.member.repository.MemberRepository;
import com.moit.member.repository.MemberStatusRepository;
import com.moit.member.repository.MemberTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService{
	
	private final MemberRepository memberRepository;
	private final MemberInfoRepository memberInfoRepository;
	private final MemberTypeRepository memberTypeRepository;
	private final MemberStatusRepository memberStatusRepository;
	private final PasswordEncoder passwordEncoder;
	private final MemberInterestRepository memberInterestRepository;
	private final InterestRepository interestRepository;
	
	// 중복검사
	@Override
	public boolean existsByLoginId(String loginId) {
		return memberRepository.existsByLoginId(loginId);
	}
	@Override
	public boolean existsByEmail(String email) {
		return memberRepository.existsByEmail(email);
	}
	@Override
	public boolean existsByNickname(String nickname) {
		return memberRepository.existsByNickname(nickname);
	}
	@Override
	public boolean existsByMobile(String mobile) {
		return memberRepository.existsByMobile(mobile);
	}
	
	
	// 회원가입
	@Transactional
	@Override
	public UserDto signup(UserDto dto) {
		
		//회원 유형 조회
		MemberType memberType = memberTypeRepository.findById(dto.getMemberTypeId())
							.orElseThrow(()-> new IllegalArgumentException("존재하지 않는 회원 유형입니다."));
		
		Long statusId;
		
		if(dto.getMemberTypeId() == 1L) {
			// 일반회원
			statusId = 1L;
		}
		else if(dto.getMemberTypeId() == 2L) {
			// 제휴업체
			statusId = 2L;
		}
		else if(dto.getMemberTypeId() == 3L) {
			// 일반 관리자
			statusId = 3L;
		}
		else {
			throw new IllegalArgumentException("잘못된 회원 유형입니다.");
		}
		
		// 회원 상태 조회
		MemberStatus memberStatus = memberStatusRepository.findById(statusId)
							.orElseThrow(()-> new IllegalArgumentException("존재하지 않는 회원 상태입니다."));
		
		// 회원 생성
		Member member = new Member();
		
		member.setLoginId(dto.getLoginId());
		member.setMobile(dto.getMobile());
		member.setNickname(dto.getNickname());
		member.setEmail(dto.getEmail());
		// 비밀번호 암호화
		member.setPassword(passwordEncoder.encode(dto.getPassword()));
		// 프로필 이미지(기본 or 설정 이미지)
		if(dto.getProfileUrl() == null || dto.getProfileUrl().isBlank()) {
			member.setProfileUrl("/images/moit.png");
		}
		else {
			member.setProfileUrl(dto.getProfileUrl());
		}
		
		member.setMemberType(memberType);
		member.setMemberStatus(memberStatus);
		
		// 회원정보 저장
		memberRepository.save(member);
		
		// 회원 상세정보
		MemberInfo memberInfo = new MemberInfo();
		
		memberInfo.setMember(member);
		memberInfo.setGender(dto.getGender());
		memberInfo.setBirth(dto.getBirth());
		
		memberInfoRepository.save(memberInfo);
		
		// 회원 관심사
		if(dto.getInterestIds() != null) {
			for(Integer interestId  : dto.getInterestIds()) {
				Interest interest = interestRepository.findById(interestId.longValue())
									.orElseThrow(()-> new IllegalArgumentException("존재하지 않은 관심사입니다."));
				MemberInterest memberInterest = new MemberInterest();
				
				MemberInterestId id = new MemberInterestId(member.getId(),interest.getInterestId());
				
				memberInterest.setId(id);
				memberInterest.setMember(member);
				memberInterest.setInterest(interest);
				
				memberInterestRepository.save(memberInterest);
			}
		}
		
		// DTO에 반영
		dto.setMemberId(member.getId());
		dto.setStatusId(statusId);
		dto.setProfileUrl(member.getProfileUrl());
		
		return dto;
		
	}
	
	// 로그인
	@Override
	public UserDto findByLoginId(String loginId) {
		
		Member member = memberRepository.findByLoginId(loginId).orElse(null);
		
		if(member == null) { return null; }
		
		UserDto dto = new UserDto();
		
		dto.setMemberId(member.getId());
	    dto.setLoginId(member.getLoginId());
	    dto.setPassword(member.getPassword());
	    dto.setEmail(member.getEmail());
	    dto.setNickname(member.getNickname());
	    dto.setMobile(member.getMobile());
	    dto.setProfileUrl(member.getProfileUrl());

	    dto.setMemberTypeId(member.getMemberType().getMemberTypeId());
	    dto.setStatusId(member.getMemberStatus().getStatusId());
		
		return dto;
	}
	
	// JWT 회원조회
	@Override
	public UserDto findByMemberId(Long memberId) {
		
		Member member = memberRepository.findById(memberId).orElse(null);
		
		if (member == null) { return null; }
		
		MemberInfo memberInfo = memberInfoRepository.findById(memberId)
	            					.orElse(null);
		
		UserDto dto = new UserDto();
		
		dto.setMemberId(member.getId());
	    dto.setLoginId(member.getLoginId());
	    dto.setPassword(member.getPassword());
	    dto.setEmail(member.getEmail());
	    dto.setNickname(member.getNickname());
	    dto.setMobile(member.getMobile());
	    dto.setProfileUrl(member.getProfileUrl());

	    dto.setMemberTypeId(member.getMemberType().getMemberTypeId());
	    dto.setStatusId(member.getMemberStatus().getStatusId());
	    
	    if(memberInfo != null) {
	    	dto.setGender(memberInfo.getGender());
	        dto.setBirth(memberInfo.getBirth());
	    }
		
		return dto;
	}
	
	// 회원정보 수정
	@Transactional
	@Override
	public UserDto updateMember(Long memberId, UserDto dto) {
		
		//1. 회원조회
		Member member = memberRepository.findById(memberId)
							.orElseThrow(()-> new IllegalArgumentException("존재하지 않는 회원입니다."));
		
		//2. 회원 기본정보 수정
		member.setNickname(dto.getNickname());
		member.setMobile(dto.getMobile());
		
		// 프로필 이미지 수정
		if(dto.getProfileUrl() != null && !dto.getProfileUrl().isBlank()) {
			member.setProfileUrl(dto.getProfileUrl());
		}
		
		//3. 회원 상세정보 조회
		MemberInfo memberInfo = memberInfoRepository.findById(memberId)
									.orElseThrow(()-> new IllegalArgumentException("회원 상세정보가 존재하지 않습니다.") );
		
		//4. 상세정보 수정
		memberInfo.setGender(dto.getGender());
		memberInfo.setBirth(dto.getBirth());
		
		//5. DTO에 반영
		dto.setMemberId(member.getId());
		dto.setLoginId(member.getLoginId());
		dto.setEmail(member.getEmail());
		dto.setNickname(member.getNickname());
		dto.setMobile(member.getMobile());
		dto.setProfileUrl(member.getProfileUrl());
		
		dto.setMemberTypeId(member.getMemberType().getMemberTypeId());
		dto.setStatusId(member.getMemberStatus().getStatusId());
		
		return dto;
	}
	
	// 회원탈퇴(논리삭제)
	@Transactional
	@Override
	public void deleteMember(Long memberId) {
		//1. 회원조회
		Member member = memberRepository.findById(memberId)
							.orElseThrow(()->new IllegalArgumentException("존재하지 않는 회원입니다."));
		
		//2. delete 상태 조회
		MemberStatus deletedStatus = memberStatusRepository.findById(4L)
										.orElseThrow(()->new IllegalArgumentException("삭제 상태가 존재하지 않습니다."));
		
		//3. 상태변경
		member.setMemberStatus(deletedStatus);
		
		member.setDeleteYn('Y');
	}
	
	
	
}
