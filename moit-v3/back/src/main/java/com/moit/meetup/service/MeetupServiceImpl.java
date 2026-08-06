package com.moit.meetup.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moit.exception.ResourceNotFoundException;
import com.moit.meetup.dto.MeetupApplicationDto.MeetupApplicantResponseDto;
import com.moit.meetup.dto.MeetupApplicationDto.MeetupApplicationRequestDto;
import com.moit.meetup.dto.MeetupApplicationDto.MeetupApplicationResponseDto;
import com.moit.meetup.dto.MeetupApplicationDto.MeetupApplyMemberListResponseDto;
import com.moit.meetup.dto.MeetupApplicationDto.MyApplicationListResponseDto;
import com.moit.meetup.dto.MeetupDto.MeetupListResponseDto;
import com.moit.meetup.dto.MeetupDto.MeetupRequestDto;
import com.moit.meetup.dto.MeetupDto.MeetupResponseDto;
import com.moit.meetup.entity.Meetup;
import com.moit.meetup.entity.MeetupApplication;
import com.moit.meetup.entity.MeetupLike;
import com.moit.meetup.enums.ApplyStatus;
import com.moit.meetup.repository.MeetupApplicationRepository;
import com.moit.meetup.repository.MeetupLikesRepository;
import com.moit.meetup.repository.MeetupRepository;
import com.moit.member.entity.Member;
import com.moit.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetupServiceImpl implements MeetupService{
	
	private final MeetupRepository meetupRepository;
	private final MeetupApplicationRepository meetupApplicationRepository;
	private final MeetupLikesRepository meetupLikesRepository;
	private final MemberRepository memberRepository; 
	
	//목록조회
	@Override
	public MeetupListResponseDto search(Pageable pageable) {
		Page<Meetup> page = meetupRepository.findAll(pageable);
//		page.getTotalPages(); // 전체페이지수 100개라면 10개
//		page.getNumberOfElements(); // 전체갯수 100개
//		page.getContent(); // 0번째 페이지의 10개가 들어있음

		MeetupListResponseDto listResponse = new MeetupListResponseDto();
		listResponse.setTotalCount(page.getTotalElements()); // Page클레스에서 제공하는 getTotalElement
		listResponse.setTotalPage((long)page.getTotalPages()); //Page클레스에서 제공하는  getTotalPages
		
		List<Meetup> contents = page.getContent(); //조회한갯수만큼 나옴 - 10개씩
		List<MeetupResponseDto> list = new ArrayList<>(); 
		for(int i=0; i < contents.size(); i++) {// entity -> dto로 변환
			Meetup meetup = contents.get(i);			
			list.add(MeetupResponseDto.listFrom(meetup));
		}
		listResponse.setMeetups(list);
		return listResponse;

	}
	
	//상세조회
	@Override
	public MeetupResponseDto detail(Long meetupId, Long memberId) {		
		Meetup meetup = meetupRepository.findById(meetupId)
										.orElseThrow(()->new ResourceNotFoundException("존재하지 않는 게시글입니다. ID: "+ meetupId));
		
		MeetupApplication meetupApplication = meetupApplicationRepository.findByMeetup_IdAndMember_Id(meetupId, memberId).orElse(null);
		

		if(meetup.getDeleteYn() == 'Y') {
			throw new IllegalArgumentException("삭제된 게시글 입니다.");
		}
		
		
		MeetupResponseDto response = MeetupResponseDto.detailFrom(meetup);
		if(meetupApplication != null) {
			response.setApplyStatus(meetupApplication.getApplyStatus());
		}
		
		return response;
	}
	
	//생성
	@Transactional
	@Override
	public void create(MeetupRequestDto meetupRequestDto, Long memberId) {
		Member member = memberRepository.findById(memberId)
										.orElseThrow(()-> new ResourceNotFoundException("존재하지 않는 회원입니다. MEMBERID" + memberId));
		
	    Meetup meetup = Meetup.builder()
							  .title(meetupRequestDto.getTitle())
							  .content(meetupRequestDto.getContent())
							  .maxParticipants(meetupRequestDto.getMaxParticipants())
							  .minParticipants(meetupRequestDto.getMinParticipants())
							  .sigunguId(meetupRequestDto.getSigunguId())
							  .categoryId(meetupRequestDto.getCategoryId())
							  .address(meetupRequestDto.getAddress())
							  .addressDetail(meetupRequestDto.getAddressDetail())
							  .meetupAt(meetupRequestDto.getMeetupAt())
							  .meetupStatus(meetupRequestDto.getMeetupStatus())
							  .latitude(meetupRequestDto.getLatitude())
							  .longitude(meetupRequestDto.getLongitude())
							  .nx(meetupRequestDto.getNx())
							  .ny(meetupRequestDto.getNy())
							  .member(member)
							  .build();
		meetupRepository.save(meetup);
	}
	
	//수정
	@Transactional
	@Override
	public void update(MeetupRequestDto meetupRequestDto, Long meetupId) {
		Meetup meetup = meetupRepository.findById(meetupId)
										.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시글입니다. MEETUPID" + meetupId));
		
		if(meetup.getDeleteYn() == 'Y') {
			throw new ResourceNotFoundException("삭제된 게시글 입니다. MEETUPID" + meetupId);
		}
		
		// 저장메서드를 따로 호출하지 않아도 update 쿼리 반영 더티체킹(Dirty Checking)
		meetup.setTitle(meetupRequestDto.getTitle());
		meetup.setContent(meetupRequestDto.getContent());
		meetup.setMaxParticipants(meetupRequestDto.getMaxParticipants());
		meetup.setMinParticipants(meetupRequestDto.getMinParticipants());
		meetup.setSigunguId(meetupRequestDto.getSigunguId());
		meetup.setCategoryId(meetupRequestDto.getCategoryId());
		meetup.setAddress(meetupRequestDto.getAddress());
		meetup.setAddressDetail(meetupRequestDto.getAddressDetail());
		meetup.setMeetupAt(meetupRequestDto.getMeetupAt());
		meetup.setMeetupStatus(meetupRequestDto.getMeetupStatus());
		meetup.setLatitude(meetupRequestDto.getLatitude());
		meetup.setLongitude(meetupRequestDto.getLongitude());
		meetup.setNx(meetupRequestDto.getNx());
		meetup.setNy(meetupRequestDto.getNy());		
	}
	
	//삭제
	@Transactional
	@Override
	public void delete(Long meetupId) {
		Meetup meetup = meetupRepository.findById(meetupId)
				.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시글입니다. MEETUPID" + meetupId));
		
		meetup.setDeleteYn('Y'); //저장메서드를 따로 호출하지 않아도 delete 쿼리 반영 더티체킹(Dirty Checking)		
	}
	
	//모임신청
	@Transactional
	@Override	
	public void meetupApply(Long memberId, Long meetupId) {
		Meetup meetup = meetupRepository.findById(meetupId)
										.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시글입니다. MEETUPID" + meetupId));
		
		Member member = memberRepository.findById(memberId)
										.orElseThrow(()-> new ResourceNotFoundException("존재하지 않는 회원입니다.. MEMBERID" + memberId));
		
		MeetupApplication meetupApplication = MeetupApplication.builder()
															   .applyStatus(ApplyStatus.PENDING)
															   .meetup(meetup)
															   .member(member)
															   .build();
		
		meetupApplicationRepository.save(meetupApplication);
	}
	
	//좋아요
	@Override
	public void meetupLike(Long meetupId, Long memberId) {
		
		boolean exists = meetupLikesRepository.existsByMember_IdAndMeetup_Id(memberId, meetupId);
		
		if(exists) {
			meetupLikesRepository.deleteByMember_IdAndMeetup_Id(memberId, meetupId);
			return;
		}		
		
		Meetup meetup = meetupRepository.findById(meetupId)
				.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시글입니다. MEETUPID" + meetupId));

		Member member = memberRepository.findById(memberId)
				.orElseThrow(()-> new ResourceNotFoundException("존재하지 않는 회원입니다. MEMBERID" + memberId));

		
		MeetupLike meetupLike = MeetupLike.builder()
										  .meetup(meetup)
										  .member(member)
										  .build();
		
		meetupLikesRepository.save(meetupLike);	
	}
	
	//모집글 비공개(관리자)
	@Transactional
	@Override	
	public void disableMeetup(Long meetupId) {
		
		Meetup meetup = meetupRepository.findById(meetupId)
				.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시글입니다. MEETUPID" + meetupId));
		
	    meetup.setHidden(true);
	}
	
	//마이페이지 내가 신청한 모집글 목록 조회(페이징)
	@Override
	public MyApplicationListResponseDto getMyApplications(Long memberId, Pageable pageable) {
		
	    Page<MeetupApplication> page = meetupApplicationRepository.findByMember_Id(memberId, pageable);

	    MyApplicationListResponseDto response =
	            new MyApplicationListResponseDto();

	    response.setTotalCount(page.getTotalElements());
	    response.setTotalPage((long) page.getTotalPages());

	    List<MeetupApplicationResponseDto> applications =
	            page.getContent()
	                    .stream()
	                    .map(MeetupApplicationResponseDto::fromEntity)
	                    .toList();

	    response.setApplications(applications);

	    return response;
	}

	//마이페이지 내 모집글 신청자 리스트(페이징)
	@Override
	public MeetupApplyMemberListResponseDto getMyMeetupApplicants(Long meetupId, Long memberId, Pageable pageable) {

		Page<MeetupApplication> page = meetupApplicationRepository.findByMeetup_IdAndMeetup_Member_Id(meetupId, memberId, pageable);
		
		MeetupApplyMemberListResponseDto response = new MeetupApplyMemberListResponseDto();
		
	    response.setTotalCount(page.getTotalElements());
	    response.setTotalPage((long) page.getTotalPages());
	    
	    List<MeetupApplicantResponseDto> list =
	            page.getContent()
	                .stream()
	                .map(MeetupApplicantResponseDto::fromEntity)
	                .toList();
	    
	    response.setApplicants(list);

		return response;
	}

	//마이페이지 승인, 거절(거절사유), 노쇼 처리
	@Transactional
	@Override
	public void updateApplicationStatus(MeetupApplicationRequestDto requestDto) {
		
		MeetupApplication meetupApplication = meetupApplicationRepository.findById(requestDto.getApplicationId())
																		.orElseThrow(() ->
													                    new ResourceNotFoundException(
													                            "존재하지 않는 신청입니다. APPLICATION ID : " 
													                            + requestDto.getApplicationId()
													                        ));
		meetupApplication.setApplyStatus(requestDto.getApplyStatus());
		
		//거절일 경우 거절 사유 저장
		if(requestDto.getApplyStatus() == ApplyStatus.REJECTED) {
			meetupApplication.setRejectReason(requestDto.getRejectReason());
		}else{
			meetupApplication.setRejectReason(null);
		}
	}
	
	//마이페이지 내가 모집한 모집글 조회(페이징)
	@Override
	public MeetupListResponseDto getMyMeetups(Long memberId, Pageable pageable) {
		Page<Meetup> page = meetupRepository.findByMember_Id(memberId, pageable);
		MeetupListResponseDto response = new MeetupListResponseDto();
		
		response.setTotalCount(page.getTotalElements());
		response.setTotalPage((long) page.getTotalPages());
		
		List<MeetupResponseDto> meetups = page.getContent()
											.stream()
											.map(MeetupResponseDto::listFrom)
											.toList();
		response.setMeetups(meetups);
		return response;
	}
}
