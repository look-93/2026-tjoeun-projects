package com.moit.meetup.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moit.common.dto.SigunguDto;
import com.moit.common.entity.Image;
import com.moit.common.entity.Sigungu;
import com.moit.common.repository.ImageRepository;
import com.moit.exception.ResourceNotFoundException;
import com.moit.meetup.client.OpenAiApiClient;
import com.moit.meetup.dto.MeetupApplicationDto.MeetupApplicantResponseDto;
import com.moit.meetup.dto.MeetupApplicationDto.MeetupApplicationRequestDto;
import com.moit.meetup.dto.MeetupApplicationDto.MeetupApplicationResponseDto;
import com.moit.meetup.dto.MeetupApplicationDto.MeetupApplyMemberListResponseDto;
import com.moit.meetup.dto.MeetupApplicationDto.MyApplicationListResponseDto;
import com.moit.meetup.dto.MeetupCategoryDto;
import com.moit.meetup.dto.MeetupCountResponseDto;
import com.moit.meetup.dto.MeetupDto.MeetupListResponseDto;
import com.moit.meetup.dto.MeetupDto.MeetupRequestDto;
import com.moit.meetup.dto.MeetupDto.MeetupResponseDto;
import com.moit.meetup.dto.MeetupLikeCountDto;
import com.moit.meetup.dto.MeetupLikeDto;
import com.moit.meetup.dto.MeetupParticipantCountDto;
import com.moit.meetup.dto.MyMeetupCountResponseDto;
import com.moit.meetup.dto.openapi.RecommendMeetupRequestDto;
import com.moit.meetup.dto.openapi.RecommendMeetupResponseDto;
import com.moit.meetup.entity.Meetup;
import com.moit.meetup.entity.MeetupApplication;
import com.moit.meetup.entity.MeetupCategory;
import com.moit.meetup.entity.MeetupImage;
import com.moit.meetup.entity.MeetupLike;
import com.moit.meetup.enums.ApplyStatus;
import com.moit.meetup.enums.MeetupStatus;
import com.moit.meetup.repository.MeetupApplicationRepository;
import com.moit.meetup.repository.MeetupCategoryRepository;
import com.moit.meetup.repository.MeetupImageRepository;
import com.moit.meetup.repository.MeetupLikesRepository;
import com.moit.meetup.repository.MeetupRepository;
import com.moit.meetup.repository.MeetupSigunguRepository;
import com.moit.member.entity.Member;
import com.moit.member.entity.MemberInfo;
import com.moit.member.repository.MemberRepository;
import com.moit.util.UtilUpload;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetupServiceImpl implements MeetupService{
	
	private final MeetupRepository meetupRepository;
	private final MeetupApplicationRepository meetupApplicationRepository;
	private final MeetupLikesRepository meetupLikesRepository;
	private final MemberRepository memberRepository; 
	private final MeetupCategoryRepository meetupCategoryRepository;
	private final MeetupSigunguRepository meetupSigunguRepository;
	private final UtilUpload utilUpload;
	private final MeetupImageRepository meetupImageRepository;
	private final ImageRepository imageRepository;
	private final OpenAiApiClient openAiApiClient;
		
	//모임리스트조회
	@Override
	public MeetupListResponseDto search(
	        Pageable pageable,
	        Long memberId,
	        MeetupStatus status,
	        String searchType,
	        String searchText,
	        Long sidoId,
	        Long categoryId,
	        String orderType
	) {

	    Page<Meetup> page = meetupRepository.findByDeleteYn(
	            'N',
	            status,
	            searchType,
	            searchText,
	            sidoId,
	            categoryId,
	            orderType,
	            pageable
	    );
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
		
		//list에서 id꺼내기
		List<Long> ids = list.stream().map(item -> item.getId()).toList();
		
		//쿼리통해서 ids에 해당하는 total_participants 를 구함
		List<MeetupParticipantCountDto> result = meetupApplicationRepository.countByMeetup_IdInAndApplyStatusAndDeleteYn(ids, ApplyStatus.APPROVED, 'N');
		
		// 모임 신청 인원 추출
		list.forEach(item->{
			
			result.forEach(cnt->{
				
				if(item.getId().equals(cnt.getMeetupId())) {
					item.setTotalParticipants(cnt.getTotalParticipants());
					return;
				}
			});
			
		});
		
		//쿼리통해서 ids에 해당하는 like 를 구함
		List<MeetupLikeDto> likeResult =
		        meetupLikesRepository.findLikedMeetups(ids, memberId);
			
		//내가 좋아요 눌렀는지 추출
		list.forEach(meetup ->{
			likeResult.forEach(like->{
				if(like.getMeetupId().equals(meetup.getId())) {
					meetup.setHasLike(true);
				}
			});
		});
		
		
		List<MeetupLikeCountDto> likeCount =
		        meetupLikesRepository.countByMeetup_Id(ids);	
		
		//조회된 모임의 좋아요 수
		list.forEach(meetup ->{
			likeCount.forEach(cnt->{
				if(cnt.getMeetupId().equals(meetup.getId())) {
					meetup.setLikeCount(cnt.getLikeCount());
				}
			});
		});	

		listResponse.setMeetups(list);
		return listResponse;

	}
	
	//상세조회
	@Override
	public MeetupResponseDto detail(Long meetupId, Long memberId) {		
		Meetup meetup = meetupRepository.findById(meetupId)
										.orElseThrow(()->new ResourceNotFoundException("존재하지 않는 게시글입니다. ID: "+ meetupId));
		
		if(meetup.getDeleteYn() == 'Y') {
			throw new IllegalArgumentException("삭제된 게시글 입니다.");
		}		
		
		MeetupResponseDto response = MeetupResponseDto.detailFrom(meetup, memberId);
		
		return response;
	}
	
	//모임등록
	@Transactional
	@Override
	public void create(MeetupRequestDto meetupRequestDto, Long memberId, List<MultipartFile> files){
		Member member = memberRepository.findById(memberId)
										.orElseThrow(()-> new ResourceNotFoundException("존재하지 않는 회원입니다. MEMBERID" + memberId));
		
		Sigungu sigungu = meetupSigunguRepository.findById(meetupRequestDto.getSigunguId()).orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 지역입니다. SigunguId" + meetupRequestDto.getSigunguId()));
		MeetupCategory meetupCategory = meetupCategoryRepository.findById(meetupRequestDto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 카테고리입니다. meetupCategory" + meetupRequestDto.getCategoryId()));
		
	    Meetup meetup = Meetup.builder()
							  .title(meetupRequestDto.getTitle())
							  .content(meetupRequestDto.getContent())
							  .maxParticipants(meetupRequestDto.getMaxParticipants())
							  .minParticipants(meetupRequestDto.getMinParticipants())
							  .sigungu(sigungu)
							  .meetupCategory(meetupCategory)
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
		try {
			if(files != null && !files.isEmpty()) {
				for(MultipartFile file : files) {
					String savedFileName = utilUpload.fileUpload(file, "meetup");
					
					Image image = Image.builder()
					        .imagePath(savedFileName)
					        .build();
					
					MeetupImage meetupImage = MeetupImage.builder()
					        .meetup(meetup)
					        .image(image)
					        .build();
					imageRepository.save(image);
					meetupImageRepository.save(meetupImage);
				}
			}
		}catch(IOException e) {
			throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
		}		
	}
	
	// 모임 수정
	@Transactional
	@Override
	public void update(
	        MeetupRequestDto meetupRequestDto,
	        Long meetupId,
	        List<MultipartFile> files,
	        List<String> existingImagePaths
	) {

	    Meetup meetup = meetupRepository.findById(meetupId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "존재하지 않는 게시글입니다. MEETUPID" + meetupId
	                    )
	            );

	    Sigungu sigungu = meetupSigunguRepository.findById(
	            meetupRequestDto.getSigunguId()
	    ).orElseThrow(() ->
	            new ResourceNotFoundException(
	                    "존재하지 않는 지역입니다. SigunguId"
	                            + meetupRequestDto.getSigunguId()
	            )
	    );

	    MeetupCategory meetupCategory = meetupCategoryRepository.findById(
	            meetupRequestDto.getCategoryId()
	    ).orElseThrow(() ->
	            new ResourceNotFoundException(
	                    "존재하지 않는 카테고리입니다. meetupCategory"
	                            + meetupRequestDto.getCategoryId()
	            )
	    );

	    if (meetup.getDeleteYn() == 'Y') {
	        throw new ResourceNotFoundException(
	                "삭제된 게시글 입니다. MEETUPID" + meetupId
	        );
	    }

	    // =========================
	    // 모임 정보 수정
	    // =========================

	    meetup.setTitle(meetupRequestDto.getTitle());
	    meetup.setContent(meetupRequestDto.getContent());
	    meetup.setMaxParticipants(meetupRequestDto.getMaxParticipants());
	    meetup.setMinParticipants(meetupRequestDto.getMinParticipants());
	    meetup.setSigungu(sigungu);
	    meetup.setMeetupCategory(meetupCategory);
	    meetup.setAddress(meetupRequestDto.getAddress());
	    meetup.setAddressDetail(meetupRequestDto.getAddressDetail());
	    meetup.setMeetupAt(meetupRequestDto.getMeetupAt());
	    meetup.setMeetupStatus(meetupRequestDto.getMeetupStatus());
	    meetup.setLatitude(meetupRequestDto.getLatitude());
	    meetup.setLongitude(meetupRequestDto.getLongitude());
	    meetup.setNx(meetupRequestDto.getNx());
	    meetup.setNy(meetupRequestDto.getNy());
	    
		 // =========================
		 // 기존 이미지 삭제
		 // =========================
	
		 List<String> keepImagePaths = (existingImagePaths != null) ? existingImagePaths : new ArrayList<>();
	
		 // 삭제해야 할 MeetupImage 추출
		 List<MeetupImage> removeMeetupImages = meetup.getMeetupImages()
		         .stream()
		         .filter(meetupImage -> !keepImagePaths.contains(meetupImage.getImage().getImagePath()))
		         .toList();
	
		 for (MeetupImage meetupImage : removeMeetupImages) {
		     Image image = meetupImage.getImage();
	
		     // 1. 실제 로컬/S3 파일 삭제
		     utilUpload.fileDelete(image.getImagePath(), "meetup");
	
		     // 2. 부모 자식 관계 명시적 제거 (메모리동기화)
		     meetup.getMeetupImages().remove(meetupImage);
	
		     // 3. DB 삭제 (MeetupImage 우선 삭제 -> Image 삭제)
		     meetupImageRepository.delete(meetupImage);
		     imageRepository.delete(image);
		 }


	    // =========================
	    // 새 이미지 추가
	    // =========================

	    if (files != null && !files.isEmpty()) {

	        try {

	            for (MultipartFile file : files) {

	                String savedFileName =
	                        utilUpload.fileUpload(file, "meetup");

	                Image image = Image.builder()
	                        .imagePath(savedFileName)
	                        .build();

	                imageRepository.save(image);

	                MeetupImage meetupImage =
	                        MeetupImage.builder()
	                                .meetup(meetup)
	                                .image(image)
	                                .build();

	                meetupImageRepository.save(meetupImage);
	            }

	        } catch (IOException e) {

	            throw new RuntimeException(
	                    "이미지 업로드 중 오류가 발생했습니다.",
	                    e
	            );
	        }
	    }
	}
	
	//모임삭제
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
	public void apply(Long memberId, Long meetupId) {
		Meetup meetup = meetupRepository.findById(meetupId)
										.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시글입니다. MEETUPID" + meetupId));
		
		Member member = memberRepository.findById(memberId)
										.orElseThrow(()-> new ResourceNotFoundException("존재하지 않는 회원입니다.. MEMBERID" + memberId));
			
		Optional<MeetupApplication> apply = meetupApplicationRepository.findByMeetup_IdAndMember_Id(meetupId, memberId);
		
	    // 이미 신청했으면 → 신청 취소
	    if (apply.isPresent()) {	    	
	    	MeetupApplication meetupApplication = apply.get();

	        // 신청 중이면 → 취소
	        if (meetupApplication.getApplyStatus() == ApplyStatus.PENDING) {
	        	
	        	//오늘 신청한 경우
	        	if(meetupApplication.getCreatedAt().toLocalDate().isEqual(LocalDate.now())) {
	        		
		        	// 신청 1시간 이후 취소 시 신뢰도 점수 차감 - -5점
		        	if(meetupApplication.getCreatedAt().plusHours(1).isBefore(LocalDateTime.now())) {
		        	    changeTrustScore(member, -5);
		        	}
	        	}
	        	
	            meetupApplication.setApplyStatus(ApplyStatus.CANCELED);
	            return;
	        }

	        // 취소된 상태면 → 다시 신청
	        if (meetupApplication.getApplyStatus() == ApplyStatus.CANCELED) {	 
	            
	        	// ⭐ 다시 신청할 때 정원 
	            long applicantCount = meetupApplicationRepository.countByMeetupIdAndApplyStatus(meetupId, ApplyStatus.PENDING);

	            if (applicantCount >= meetup.getMaxParticipants()) {
	                throw new IllegalStateException( "모임 정원이 가득 찼습니다.");
	            }
	        	
	            meetupApplication.setApplyStatus(ApplyStatus.PENDING);
	            return;
	        }
	    }
	    
	    // 신규 신청 → 정원 확인
	    long applicantCount = meetupApplicationRepository.countByMeetupIdAndApplyStatus( meetupId, ApplyStatus.PENDING);
	    
	    if (applicantCount >= meetup.getMaxParticipants()) {
	        throw new IllegalStateException("모임 정원이 가득 찼습니다.");
	    }
	    
	    // AI 한줄평이 없는 경우 최초 생성
	    if (member.getMemberInfo().getAiSummary() == null) {
	    	//System.out.println("⭐ AI Summary 최초 생성");
	        updateAiSummary(member);
	    }
	    
	    // 신청 내역 자체가 없으면 → 신규 신청
		MeetupApplication meetupApplication = MeetupApplication.builder()
															   .applyStatus(ApplyStatus.PENDING)
															   .meetup(meetup)
															   .member(member)
															   .build();
		
		meetupApplicationRepository.save(meetupApplication);
	}
	
	//좋아요
	@Override
	public void meetupLike(Long memberId, Long meetupId) {
		
		boolean exists = meetupLikesRepository.existsByMember_IdAndMeetup_Id(memberId, meetupId);
		
		//이미 좋아요 했으면 삭제
		if(exists) {
			meetupLikesRepository.deleteByMember_IdAndMeetup_Id(memberId, meetupId);
			return;
		}		
		
		Meetup meetup = meetupRepository.findById(meetupId)
				.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시글입니다. MEETUPID" + meetupId));

		Member member = memberRepository.findById(memberId)
				.orElseThrow(()-> new ResourceNotFoundException("존재하지 않는 회원입니다. MEMBERID" + memberId));
		
		//없으면 저장
		MeetupLike meetupLike = MeetupLike.builder()
										  .meetup(meetup)
										  .member(member)
										  .build();
		
		meetupLikesRepository.save(meetupLike);	
	}
	
	//모집글 비공개(관리자)
	@Transactional
	@Override	
	public void changeMeetupVisibility(Long meetupId) {
		
		Meetup meetup = meetupRepository.findById(meetupId)
				.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시글입니다. MEETUPID" + meetupId));

			meetup.setHidden(!meetup.getHidden());
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
	    //System.out.println("신청 상태 = " + applications.get(0).getMeetupStatus());
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
		Member member = meetupApplication.getMember();
		
		//기존 상태
		ApplyStatus beforeStatus = meetupApplication.getApplyStatus();
		
		//받아온 상태
		ApplyStatus afterStatus = requestDto.getApplyStatus();
		
		meetupApplication.setApplyStatus(requestDto.getApplyStatus());
		
		//거절일 경우 거절 사유 저장
		if(requestDto.getApplyStatus() == ApplyStatus.REJECTED) {
			meetupApplication.setRejectReason(requestDto.getRejectReason());
		}else{
			meetupApplication.setRejectReason(null);
		}
		
		if(beforeStatus != ApplyStatus.NOSHOW && afterStatus == ApplyStatus.NOSHOW) {
			//기존 상태가 NOSHOW가 아니고 → 새 상태가 NOSHOW일 때만 -5
    		changeTrustScore(member, -5);
		}else if(beforeStatus == ApplyStatus.NOSHOW && afterStatus != ApplyStatus.NOSHOW) {
			//NOSHOW에서 다른 상태로 변경 (실수로 누른경우)
			changeTrustScore(member, 5);
		}		
	}
	
	//카테고리 조회
	@Override
	public List<MeetupCategoryDto> getCategory() {
		List<MeetupCategory> meetupCategory =  meetupCategoryRepository.findAll();
		
//		List<CategoryDto> result = meetupCategory.stream()
//				.map(cate->{
//			CategoryDto dto = CategoryDto.from(cate);
//			return dto;
//		}).toList();
		
		
		//아이디만 담으려고하면
//		List<Long> result = meetupCategory.stream()
//				.map(cate->{
//			return cate.getId();
//		}).toList();
		
//		List<CategoryDto> result =   meetupCategory.stream()
//		.filter(cate->{
//			return cate.getId() != 1L;
//		})
//		.map(CategoryDto::from)
//		.toList();

		return meetupCategory.stream().map(MeetupCategoryDto::from).toList();	
	}
	
	//시군구 조회
	@Override
	public List<SigunguDto> getSigungu() {
		
		List<Sigungu> sigungu = meetupSigunguRepository.findAll();
		
		return sigungu.stream().map(SigunguDto::from).toList();
	}
	
	//마이페이지 - 통계
	@Override
	public MyMeetupCountResponseDto getMyMeetupCount(Long memberId) {

	    return meetupRepository.getMyMeetupCount(memberId);
	}
	
	//관리자 - 통계
	@Override
	public MeetupCountResponseDto getMeetupCount() {
		return meetupRepository.getMeetupCount();
	}
	
	// ################### open api ###################

	//ai 제목/카테고리/컨텐츠 추가
	@Override
	public RecommendMeetupResponseDto meetupWriteAiRecommended(RecommendMeetupRequestDto request){
		String keyword = request.getKeyword();
		String aiPrompt = """
				사용자가 입력한 키워드를 바탕으로 많이 모을수 있는, 재미있는, 사용자들이 클릭하고 싶은 모임 정보를 생성해.
				category는 웬만해서 입력한 키워드로 해줘. 
				
				키워드: %s

				아래 JSON 형식으로만 응답해.

				{
				  "title": "",
				  "category": "",
				  "content": ""
				}

				JSON 외의 다른 설명은 절대 하지 마.
				""".formatted(keyword);
	    try {
	    	// 1. AI 호출
			String result = openAiApiClient.getAIResponse(aiPrompt);
			
			// 2. AI 응답 JSON → DTO
			ObjectMapper mapper = new ObjectMapper();
			RecommendMeetupResponseDto dto =
			        mapper.readValue(result, RecommendMeetupResponseDto.class);
			
			// 3. 카테고리 이름 → 카테고리 ID
			Long categoryId = getCategory()
								.stream()
								.filter(category -> category.getCategoryName().equals(dto.getCategory()))
								.map(MeetupCategoryDto::getId)
								.findFirst()
								.orElse(0L);
			
			dto.setCategoryId(categoryId == null ? 0 : categoryId);
	
	
	        return dto;
	
	    } catch (Exception e) {
	        throw new RuntimeException("AI 모임 추천 생성 중 오류가 발생했습니다.", e);
	    }
	}
	
	// 점수가 실제로 변경된 경우에만 AI 요약 갱신
	private void changeTrustScore(Member member, int amount) {

	    MemberInfo memberInfo = member.getMemberInfo();

	    int currentScore = memberInfo.getTrustScore();

	    int newScore = currentScore + amount;

	    memberInfo.setTrustScore(newScore);

	    // 점수가 실제로 변경된 경우에만 AI 요약 갱신
	    if (currentScore != newScore) {
	        updateAiSummary(member);
	    }
	}
	
	// 신뢰도 점수 AI 요약 갱신
	private void updateAiSummary(Member member) {

	    Integer trustScore = member.getMemberInfo().getTrustScore();
	    //System.out.println("⭐ updateAiSummary 실행");
	    //System.out.println("⭐ trustScore = " + trustScore);
	    String aiSummary;

	    if (trustScore < 60) {
	        //System.out.println("⭐ 60점 미만 → AI 호출");

	        String aiPrompt = "[대상 유저 이력 정보]\n"
	                + "- 최근 3개월 내 무단 노쇼(NOSHOW), 당일 모임 신청 후 1시간 이내 취소 등의 이력을 종합\n"
	                + "- 총 신뢰도 점수: " + trustScore + "점\n"
	                + "- 신뢰도 점수가 60점 이하면 주의가 필요한 회원\n"
	                + "위 이력을 바탕으로 모임 개설자가 주의할 수 있게 "
	                + "20자 내외의 경고성 한 줄 요약문을 만들어줘.";

	        aiSummary = openAiApiClient.getAIResponse(aiPrompt);
	        //System.out.println("⭐ AI 응답 = " + aiSummary);
	    } else {
	    	//System.out.println("⭐ 60점 이상 → 기본 문구");
	        aiSummary = "신뢰도가 높은 회원입니다.";
	    }

	    member.getMemberInfo().setAiSummary(aiSummary);

	}	
}
