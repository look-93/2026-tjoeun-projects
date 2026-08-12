package com.moit.meetup.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moit.meetup.dto.MeetupDto.MeetupListResponseDto;
import com.moit.meetup.dto.MeetupDto.MeetupRequestDto;
import com.moit.meetup.dto.MeetupDto.MeetupResponseDto;
import com.moit.meetup.service.MeetupService;

import groovy.lang.Delegate;
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
	@GetMapping("/all") // 프론트에서 호출할때 /all?page=0&size=10 하면 pageable 에 저절로 들어감
	public ResponseEntity<MeetupListResponseDto> search(Pageable pageable){
		MeetupListResponseDto listResponseDto = meetupService.search(pageable);		
		return ResponseEntity.ok(listResponseDto); // 200 + data
	}
	
	@Operation(summary = "모임상세조회", description = "모임 상세를 조회합니다.")
	@GetMapping("/detail/{meetupId}")
	public ResponseEntity<MeetupResponseDto> detail(@PathVariable("meetupId") Long meetupId){
		Long memberId = 1L;
		MeetupResponseDto meetupResponseDto = meetupService.detail(meetupId, memberId);
		return ResponseEntity.ok(meetupResponseDto);
	}
	
	@Operation(summary = "모임등록", description = "모임을 등록합니다.")
	@PostMapping("/create/{memberId}") //  세션으로 수정
	public ResponseEntity<Void> create(@RequestBody MeetupRequestDto meetupRequestDto, @PathVariable("memberId") Long memberId){
		meetupService.create(meetupRequestDto, memberId);
		return ResponseEntity.status(HttpStatus.CREATED).build(); // 성공 응답 201
	}
	
	@Operation(summary = "모임수정", description = "모임을 수정합니다.")
	@PutMapping("/update/{id}")
	public ResponseEntity<Void> update(@RequestBody MeetupRequestDto meetupRequestDto, @PathVariable("id") Long id){
		meetupService.update(meetupRequestDto, id);
		return ResponseEntity.noContent().build(); // 성공 응답 204
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id){
		meetupService.delete(id);
		return ResponseEntity.noContent().build(); // 성공 응답 204
	}
	
	
}

//성공 응답
//조회(GET)	200 OK
//생성(POST)	201 Created
//수정(PUT/PATCH)	200 OK 또는 204 No Content(응답데이터없을때)
//삭제(DELETE)	204 No Content