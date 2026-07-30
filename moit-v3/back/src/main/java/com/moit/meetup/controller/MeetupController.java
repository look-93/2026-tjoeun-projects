package com.moit.meetup.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moit.meetup.dto.MeetupDto.MeetupListResponseDto;
import com.moit.meetup.service.MeetupService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Meetup Api", description = "게시글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetups")
@CrossOrigin(origins = "*")
public class MeetupController {

	private final MeetupService meetupService;
	
	@Operation(summary = "모임리스트조회", description = "모임리스트를 조회합니다.")
	@GetMapping("/all") // 프론트에서 호출할때 /all?page=0&size=10 하면 pageable 에 저절로 들어감
	public ResponseEntity<MeetupListResponseDto> search(Pageable pageable){
		MeetupListResponseDto listResponseDto = meetupService.search(pageable);		
		return ResponseEntity.ok(listResponseDto); // 200 + data
	}
	
}
