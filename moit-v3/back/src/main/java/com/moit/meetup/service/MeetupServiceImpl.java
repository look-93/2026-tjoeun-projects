package com.moit.meetup.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moit.meetup.dto.MeetupDto.MeetupListResponseDto;
import com.moit.meetup.dto.MeetupDto.MeetupRequestDto;
import com.moit.meetup.dto.MeetupDto.MeetupResponseDto;
import com.moit.meetup.entity.Meetup;
import com.moit.meetup.repository.MeetupRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetupServiceImpl implements MeetupService{
	
	private final MeetupRepository meetupRepository; 
	//private final MemberRepository memberRepository; 
	
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
		listResponse.setList(list);
		return listResponse;

	}

	
	@Override
	public MeetupResponseDto detail(Long id) {
		Meetup meetup = meetupRepository.findById(id)
										.orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시글입니다. ID: "+ id));
		
		if(meetup.getDeleteYn() == 'Y') {
			throw new IllegalArgumentException("삭제된 게시글 입니다.");
		}
		
		return MeetupResponseDto.detailFrom(meetup);
	}
	
	@Transactional
	@Override
	public void create(MeetupRequestDto meetupRequestDto, Long memberId) {
//		Member member = memberRepository.findById(memberId)
//										.orElseThrow(()-> new IllegalArgumentException("존재하지 않는 게시글입니다. MEMBERID" + memberId));
		
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
							  .status(meetupRequestDto.getStatus())
							  .latitude(meetupRequestDto.getLatitude())
							  .longitude(meetupRequestDto.getLongitude())
							  .nx(meetupRequestDto.getNx())
							  .ny(meetupRequestDto.getNy())
							  .build();
		meetupRepository.save(meetup);
	}
	
	@Transactional
	@Override
	public void update(MeetupRequestDto meetupRequestDto, Long id) {
		Meetup meetup = meetupRepository.findById(id)
										.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. MEETUPID" + id));
		
		if(meetup.getDeleteYn() == 'Y') {
			throw new IllegalArgumentException("삭제된 게시글 입니다. MEETUPID" + id);
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
		meetup.setStatus(meetupRequestDto.getStatus());
		meetup.setLatitude(meetupRequestDto.getLatitude());
		meetup.setLongitude(meetupRequestDto.getLongitude());
		meetup.setNx(meetupRequestDto.getNx());
		meetup.setNy(meetupRequestDto.getNy());		
	}
	
	@Transactional
	@Override
	public void delete(Long id) {
		Meetup meetup = meetupRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. MEETUPID" + id));
		
		meetup.setDeleteYn('Y'); //저장메서드를 따로 호출하지 않아도 delete 쿼리 반영 더티체킹(Dirty Checking)		
	}
	
}
