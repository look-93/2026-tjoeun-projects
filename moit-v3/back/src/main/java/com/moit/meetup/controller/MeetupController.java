package com.moit.meetup.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moit.common.dto.SigunguDto;
import com.moit.meetup.dto.MeetupApplicationDto.MeetupApplicationRequestDto;
import com.moit.meetup.dto.MeetupApplicationDto.MeetupApplyMemberListResponseDto;
import com.moit.meetup.dto.MeetupApplicationDto.MyApplicationListResponseDto;
import com.moit.meetup.dto.MeetupCategoryDto;
import com.moit.meetup.dto.MeetupDto.MeetupListResponseDto;
import com.moit.meetup.dto.MeetupDto.MeetupRequestDto;
import com.moit.meetup.dto.MeetupDto.MeetupResponseDto;
import com.moit.meetup.dto.openapi.RecommendMeetupRequestDto;
import com.moit.meetup.dto.openapi.RecommendMeetupResponseDto;
import com.moit.meetup.service.MeetupService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

// http://localhost:8080/swagger-ui/index.html - Swagger test주손

@Tag(name = "Meetup Api", description = "게시글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetups")
//@CrossOrigin(origins = "*")
public class MeetupController {

	private final MeetupService meetupService;
	
	@Operation(summary = "모임리스트조회", description = "모임리스트를 조회합니다.")
	@GetMapping // 프론트에서 호출할때 /all?page=0&size=10 하면 pageable 에 저절로 들어감
	public ResponseEntity<MeetupListResponseDto> search(Pageable pageable){
		Long memberId = 2L; // jwt토큰
		MeetupListResponseDto listResponseDto = meetupService.search(pageable, memberId);		
		return ResponseEntity.ok(listResponseDto); // 200 + data
	}
	
	@Operation(summary = "모임상세조회", description = "모임 상세를 조회합니다.")
	@GetMapping("/{meetupId}")
	public ResponseEntity<MeetupResponseDto> detail(@PathVariable("meetupId") Long meetupId){		
		MeetupResponseDto meetupResponseDto = meetupService.detail(meetupId);
		return ResponseEntity.ok(meetupResponseDto);
	}
	
	@Operation(summary = "모임등록", description = "모임을 등록합니다.")
	@PostMapping //  세션으로 수정
	public ResponseEntity<Void> create(@RequestBody MeetupRequestDto meetupRequestDto){
		Long memberId = 1L;  //세션으로 수정		
		meetupService.create(meetupRequestDto, memberId);
		return ResponseEntity.status(HttpStatus.CREATED).build(); // 성공 응답 201
	}
	
	@Operation(summary = "모임수정", description = "모임을 수정합니다.")
	@PutMapping("/{meetupId}")
	public ResponseEntity<Void> update(@RequestBody MeetupRequestDto meetupRequestDto, @PathVariable("meetupId") Long meetupId){
		meetupService.update(meetupRequestDto, meetupId);
		return ResponseEntity.noContent().build(); // 성공 응답 204
	}
	
	@Operation(summary = "관리자/개설자 모임삭제", description = "모임을 삭제합니다.")
	@DeleteMapping("/{meetupId}")
	public ResponseEntity<Void> delete(@PathVariable("meetupId") Long meetupId){
		meetupService.delete(meetupId);
		return ResponseEntity.noContent().build(); // 성공 응답 204
	}
	
	@Operation(summary = "모임신청", description = "모임을 신청합니다.")
	@PostMapping("/{meetupId}/apply")
	public ResponseEntity<Void> apply(@PathVariable("meetupId") Long meetupId){
		Long memberId = 2L; //세션으로 수정		
		meetupService.apply(memberId, meetupId);
		return ResponseEntity.ok().build(); // 성공 응답 200
	}
	
	@Operation(summary = "좋아요", description = "모임 좋아요.")
	@PatchMapping("/{meetupId}/like")
	public ResponseEntity<Void> meetupLike(@PathVariable("meetupId") Long meetupId){
		Long memberId = 2L; //세션으로 수정
		meetupService.meetupLike(memberId, meetupId);
		return ResponseEntity.ok().build(); // 성공 응답 200
	}	
	
	@Operation(summary = "관리자 모집글 공개/비공개 전환",  description = "관리자가 모집글의 공개 여부를 변경합니다.")
	@PatchMapping("/{meetupId}/visibility")
	public ResponseEntity<Void> changeMeetupVisibility(@PathVariable("meetupId") Long meetupId){
		meetupService.changeMeetupVisibility(meetupId);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "마이페이지 내 신청 조회", description = "내가 신청한 모집글 목록을 조회합니다.")
	@GetMapping("/applications")
	public ResponseEntity<MyApplicationListResponseDto> getMyApplications(Pageable pageable){
		Long memberId = 2L; //세션으로 수정
		MyApplicationListResponseDto  response = meetupService.getMyApplications(memberId, pageable);
		
		return ResponseEntity.ok(response);
	}	
	
	@Operation(summary = "마이페이지 내 모집글 - 신청자 리스트 조회", description = "모집글 신청자 리스트를 조회합니다.")
	@GetMapping("/{meetupId}/applicants")
	public ResponseEntity<MeetupApplyMemberListResponseDto> getMyMeetupApplicants(@PathVariable("meetupId") Long meetupId, Pageable pageable){
		Long memberId = 2L; //세션으로 수정
		MeetupApplyMemberListResponseDto  response = meetupService.getMyMeetupApplicants(meetupId, memberId, pageable);
		
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "마이페이지 내 모집글 조회", description = "내가 모집한 모집글 목록을 조회합니다.")
	@GetMapping("/my")
	public ResponseEntity<MeetupListResponseDto> getMyMeetups(Pageable pageable){
		Long memberId = 2L; //세션으로 수정
		MeetupListResponseDto  response = meetupService.getMyMeetups(memberId, pageable);
		
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "모집글 신청 상태 변경", description = "모집자가 신청자의 신청 상태를 승인, 거절 또는 노쇼로 변경합니다.")
	@PatchMapping("/applications/status")
	public ResponseEntity<Void> updateApplicationStatus(@RequestBody MeetupApplicationRequestDto requestDto){
		
		meetupService.updateApplicationStatus(requestDto);
		
		return ResponseEntity.ok().build();
	}	
	
	@Operation(summary = "카테고리조회", description = "카테고리를 조회합니다.")
	@GetMapping("/category")
	public ResponseEntity<List<MeetupCategoryDto>> getCategory(){		
		
		return ResponseEntity.ok(meetupService.getCategory());
	}
	
	@Operation(summary = "시군구조회", description = "시군구를 조회합니다.")
	@GetMapping("/sigungu")
	public ResponseEntity<List<SigunguDto>> getSigungu(){		
		
		return ResponseEntity.ok(meetupService.getSigungu());
	}
	
	// ################### open api ###################

	@Operation(summary = "AI 모임 제목/카테고리/내용 추천", description = "사용자가 입력한 키워드를 기반으로 AI가 모임 제목, 카테고리, 내용을 추천합니다.")
	@PostMapping("/write/ai/recommended")
	public ResponseEntity<RecommendMeetupResponseDto> meetupWriteAiRecommended(@RequestBody RecommendMeetupRequestDto request){
		return ResponseEntity.ok(meetupService.meetupWriteAiRecommended(request));
	}
}

//성공 응답
//조회(GET)	200 OK
//생성(POST)	201 Created
//수정(PUT/PATCH)	200 OK 또는 204 No Content(응답데이터없을때)
//삭제(DELETE)	204 No Content