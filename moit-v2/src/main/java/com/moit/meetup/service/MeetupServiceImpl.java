package com.moit.meetup.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.moit.meetup.client.OpenAiService;
import com.moit.meetup.dao.MeetupMapper;
import com.moit.meetup.dto.MeetupApplicationDto;
import com.moit.meetup.dto.MeetupDto;
import com.moit.meetup.dto.MeetupImageDto;
import com.moit.meetup.dto.MeetupLikeDto;
import com.moit.meetup.dto.MeetupSearchDto;
import com.moit.meetup.dto.TrustScoreDto;
import com.moit.meetup.dto.common.CategoryDto;
import com.moit.meetup.dto.common.SidoDto;
import com.moit.meetup.dto.common.SigunguDto;
import com.moit.util.UtilUpload;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MeetupServiceImpl implements MeetupService{
	@Autowired MeetupMapper meetupMapper; 
	@Autowired UtilUpload upload;
	@Autowired OpenAiService openAiService;
	
	private static final String UPLOAD_PATH = "C:/upload/meetup";

	//사용자 - 목록 조회 + paging
	@Override
	public List<MeetupDto> findAllMeetupBy(int pstartno, MeetupSearchDto meetupSearchDto) {
		int pageSize = 10;
		meetupSearchDto.setEnd(pageSize);
		meetupSearchDto.setStart((pstartno-1)*10);
		//System.out.println(pstartno);
		return meetupMapper.findAllMeetupBy(meetupSearchDto);
	}
	@Override
	public int findAllMeetupCountBy(MeetupSearchDto meetupSearchDto) {
		return meetupMapper.findAllMeetupCountBy(meetupSearchDto);
	}
	
	//모집신청 정보조회
	@Override
	public MeetupApplicationDto findApplyInfo(MeetupApplicationDto meetupApplicationDto) {
		return meetupMapper.findApplyInfo(meetupApplicationDto);
	}	
	
	//모집상세조회
	@Override
	public MeetupDto selectMeetupDetail(int meetupId) {
		MeetupDto dto = meetupMapper.selectMeetupDetail(meetupId);
		return dto;
	}
	
	//모임 상세조회 - 이미지 조회
	@Override
	public List<MeetupImageDto> findMeetupImage(int MeetupId) {
		return meetupMapper.findMeetupImage(MeetupId);
	}
	
	//모집 - 신청
	@Override
	@Transactional
	public int insertApplication(MeetupApplicationDto meetupApplicationsDto) {
		//System.out.println(meetupApplicationsDto.getMemberId());
		//System.out.println(meetupApplicationsDto.getMeetupId());
		String aiSummary = "신뢰도가 높은 회원입니다.";
		MeetupApplicationDto find = this.findApplyInfo(meetupApplicationsDto);
		
		// 1. 신뢰점수 계산
		TrustScoreDto trustScoreDto = meetupMapper.calculatedScore(meetupApplicationsDto.getMemberId());

		// 2. [내가 직접 포맷을 짜서 만드는 프롬프트 문구]
		String aiPrompt = "[대상 유저 이력 정보]\n"
		        + "- 최근 3개월 내 무단 노쇼(NOSHOW): " + trustScoreDto.getNoshowCount() + "회\n"
		        + "- 모임 시작 24시간 이내 직전 취소: " + trustScoreDto.getCancelCount() + "회\n"
		        + "- 3월 내 신고 당한 횟수: " + trustScoreDto.getReportCount() + "회\n"
		        + "- 신뢰도 점수 100점 만점에 : " + trustScoreDto.getFinalTrustScore() + "점\n"
		        + "위 이력을 바탕으로 모임 개설자가 주의할 수 있게 20자 내외의 경고성 한 줄 요약문을 만들어줘.";
		
		if(trustScoreDto.getFinalTrustScore() < 60) {
			aiSummary = openAiService.getAIResponse(aiPrompt);
		}
		//System.out.println(aiPrompt);
		//System.out.println(trustScoreDto.getFinalTrustScore());
		trustScoreDto.setAiSummary(aiSummary);
		trustScoreDto.setMemberId(meetupApplicationsDto.getMemberId());
		meetupMapper.updateAiSummaryAndTrustScore(trustScoreDto);
		
		if(find != null) {
			return meetupMapper.updateApplication(find);	
		}else {
			return meetupMapper.insertApplication(meetupApplicationsDto);
		}
	}
	
	//모집 - 신청취소
	@Override
	public int cancelApplyMeetup(MeetupApplicationDto meetupApplicationDto) {
		return meetupMapper.cancelApplyMeetup(meetupApplicationDto);
	}
	
	//모집글 등록
	@Override
	@Transactional
	public int insertMeetup(MeetupDto meetupDto, List<MultipartFile> files) {
	    // 1. 모집글 데이터 insert
	    int result = meetupMapper.insertMeetup(meetupDto);
	    
	    // 2. 업로드 된 파일이 존재하면 저장 실행
	    if (files != null && !files.isEmpty()) {
	        List<MeetupImageDto> imageList = new ArrayList<>();         
	        try {
	            for (MultipartFile file : files) {
	                if (!file.isEmpty()) {
	                    String savedFileName = upload.fileUpload(file, UPLOAD_PATH);
	                    MeetupImageDto meetupImageDto = new MeetupImageDto();
	                    meetupImageDto.setImagePath(savedFileName);
	                    imageList.add(meetupImageDto);
	                } 
	            }
	            if (!imageList.isEmpty()) {
	                // [연결 1단계] images 테이블에 다중 인서트 (UNION ALL 방식)
	                meetupMapper.insertImages(imageList);
	                
	                // [연결 2단계] Oracle 시퀀스 번호들을 역산해서 imageIds 리스트 생성
	                // Oracle에서 복합 다중 인서트를 성공시키기 위한 핵심 자바 로직입니다.
	                List<Integer> imageIds = new ArrayList<>();
	                int totalCount = imageList.size();
	                
	                // 현재 시퀀스의 가장 마지막 값(CURRVAL)을 매퍼에서 가져옵니다.
	                int lastSequence = meetupMapper.getLastImageSequence(); 
	                
	                // 들어간 개수만큼 역산하여 각각의 고유 ID를 리스트에 담아줍니다.
	                for (int i = totalCount - 1; i >= 0; i--) {
	                    imageIds.add(lastSequence - i);
	                }
	                
	                // [연결 3단계] meetup_images 매핑 테이블용 Map 조립
	                Map<String, Object> map = new HashMap<>();
	                map.put("meetupId", meetupDto.getMeetupId());
	                map.put("imageIds", imageIds); // 역산해서 알아낸 고유 ID 리스트 주입
	                
	                // [연결 4단계] meetup_images 매핑 테이블 다중 인서트 호출 (UNION ALL 방식)
	                meetupMapper.insertMeetupImages(map);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            throw new RuntimeException("파일 저장 실패", e);
	        }       
	    }   
	    return result;
	}
	
	//모집글 등록 - 이이지 저장
	@Override
	public int insertImages(List<MeetupImageDto> list) {
		return meetupMapper.insertImages(list);
	}
	@Override
	public int insertMeetupImages(Map<String, Object> map) {
		return meetupMapper.insertMeetupImages(map);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class) // 모든 예외에 대해 롤백 보장
	public int updateMeetup(MeetupDto meetupDto, List<MultipartFile> files) {
	    
	    // 1. 새 파일이 실제로 존재하는지 엄격하게 검증
	    boolean hasNewFiles = files != null && files.stream().anyMatch(file -> !file.isEmpty());
	    
	    if (hasNewFiles) {
	        // 기존 이미지 정보 미리 조회 (삭제는 새 파일 저장이 성공한 뒤에 진행)
	        List<MeetupImageDto> oldImages = meetupMapper.findMeetupImage(meetupDto.getMeetupId());
	        
	        List<MeetupImageDto> imageList = new ArrayList<>();         
	        List<String> savedFileNames = new ArrayList<>(); // 롤백 대비용 기록장
	        
	        try {
	            // 2. 새 파일들을 서버에 먼저 저장
	            for (MultipartFile file : files) {
	                if (!file.isEmpty()) {
	                    String savedFileName = upload.fileUpload(file, UPLOAD_PATH);
	                    savedFileNames.add(savedFileName); // 저장된 파일명 기록
	                    
	                    MeetupImageDto meetupImageDto = new MeetupImageDto();
	                    meetupImageDto.setImagePath(savedFileName);
	                    imageList.add(meetupImageDto);
	                } 
	            }
	            
	            // 3. DB 작업 수행
	            if (!imageList.isEmpty()) {
	                // 기존 DB 매핑 및 이미지 삭제
	                meetupMapper.deleteMeetupImages(meetupDto.getMeetupId());
	                if (!oldImages.isEmpty()) {
	                    meetupMapper.deleteImages(oldImages);
	                }
	                
	                // [연결 1단계] 새 이미지 다중 인서트
	                meetupMapper.insertImages(imageList);
	                
	                // [연결 2~4단계] 시퀀스 번호 역산 및 매핑 테이블 저장
	                List<Integer> imageIds = new ArrayList<>();
	                int totalCount = imageList.size();
	                int lastSequence = meetupMapper.getLastImageSequence(); 
	                
	                for (int i = totalCount - 1; i >= 0; i--) {
	                    imageIds.add(lastSequence - i);
	                }
	                
	                Map<String, Object> map = new HashMap<>();
	                map.put("meetupId", meetupDto.getMeetupId());
	                map.put("imageIds", imageIds);
	                
	                meetupMapper.insertMeetupImages(map);
	            }
	            
	            // 4. DB 작업까지 모두 성공했다면, 그때 기존의 진짜 '옛날 파일'들을 서버에서 지움
	            for (MeetupImageDto img : oldImages) {
	                try {
	                    Files.deleteIfExists(Path.of(UPLOAD_PATH, img.getImagePath()));
	                } catch (IOException e) {
	                    // 구 파일 삭제 실패는 비즈니스 로직을 중단할 만큼 치명적이지 않으므로 로그만 남김
	                    log.error("기존 파일 삭제 실패: {}", img.getImagePath(), e);
	                }
	            }
	            
	        } catch (Exception e) {
	            // DB 작업 중 에러 발생 시, 방금 서버에 저장했던 새 파일들을 지워주는 롤백 처리
	            for (String fileName : savedFileNames) {
	                try {
	                    Files.deleteIfExists(Path.of(UPLOAD_PATH, fileName));
	                } catch (IOException ex) {
	                    log.error("새로 업로드된 파일 롤백 실패: {}", fileName, ex);
	                }
	            }
	            throw new RuntimeException("모집글 수정 중 오류가 발생하여 작업을 취소합니다.", e);
	        }       
	    }   
	    
	    // 5. 최종 모집글 정보 수정
	    return meetupMapper.updateMeetup(meetupDto);
	}
	
	/*이미지 삭제*/
	@Override
	@Transactional(rollbackFor = Exception.class) // 모든 예외에 대해 롤백 보장
	public int deleteMeetupImages(int meetupId) {
		return meetupMapper.deleteMeetupImages(meetupId);
	}
	@Override
	public int deleteImages(List<MeetupImageDto> files) {
		return meetupMapper.deleteImages(files);
	}
	/*이미지 삭제*/
	
	//모집글 삭제
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateMeetupDeleteYn(int meetupId) {

	    // 기존 이미지 조회
	    List<MeetupImageDto> oldImages = meetupMapper.findMeetupImage(meetupId);

	    // 모집글 삭제 처리
	    int result = meetupMapper.updateMeetupDeleteYn(meetupId);

	    // meetup_images  삭제
	    meetupMapper.deleteMeetupImages(meetupId);

	    // images 삭제
	    if(oldImages != null && !oldImages.isEmpty()) {
	        meetupMapper.deleteImages(oldImages);
	    }

	    // 실제 파일 삭제
	    for(MeetupImageDto img : oldImages) {
	        try {
	            Files.deleteIfExists(
	                Path.of(UPLOAD_PATH, img.getImagePath())
	            );
	        } catch(IOException e) {
	            log.error("기존 파일 삭제 실패 : {}", img.getImagePath(), e);
	        }
	    }
	    return result;
	}
	
	//마이페이지 내 모집글 조회 + paging
	@Override
	public List<MeetupDto> selectMyMeetup(int pstartno,MeetupDto meetupDto) {
		meetupDto.setEnd(10);
		meetupDto.setStart((pstartno-1)*10);		
		return meetupMapper.selectMyMeetup(meetupDto);
	}	
	
	//마이페이지 내 모집글 조회 + paging
	@Override
	public int selectMyMeetupTotalCnt(MeetupDto meetupDto) {
		return meetupMapper.selectMyMeetupTotalCnt(meetupDto);
	}

	//마이페이지 내 모집글 통계
	@Override
	public MeetupDto selectMyPageStats(int memberId) {
		return meetupMapper.selectMyPageStats(memberId);
	}
	
	//마이페이지 내 신청글 조회 + paging
	@Override
	public List<MeetupDto> selectMyMeetupApply(int pstartno, MeetupDto meetupDto) {
		meetupDto.setEnd(10);
		meetupDto.setStart((pstartno-1)*10);
		return meetupMapper.selectMyMeetupApply(meetupDto);
	}
	
	//마이페이지 내 신청글 조회 + paging
	@Override
	public int selectMyMeetupApplyTotalCnt(MeetupDto meetupDto) {
		return meetupMapper.selectMyMeetupApplyTotalCnt(meetupDto);
	}
	
	//마이페이지 내모집글 - 신청자목록조회
	@Override
	public List<MeetupDto> selectMeetupApplyMember(int meetupId) {
		return meetupMapper.selectMeetupApplyMember(meetupId);
	}

	//마이페이지 내모집글 - 신청자목록조회 - 신청,거절
	@Override
	public int changeMeetupApplyStatus(MeetupApplicationDto meetupApplicationDto) {
		return meetupMapper.changeMeetupApplyStatus(meetupApplicationDto);
	}
	
	
	//좋아요기능
	@Override
	public boolean insertMeetupLike(MeetupLikeDto meetupLikeDto) {
		MeetupLikeDto find = this.selectMeetupLike(meetupLikeDto);
		if(find != null) {
			meetupMapper.deleteMeetupLike(find);	
			return false;
		}else {
			meetupMapper.insertMeetupLike(meetupLikeDto);
			return true;
		}
	}
	
	//좋아요갯수조회
	@Override
	public int countMeetupLike(MeetupLikeDto meetupLikeDto) {
		return meetupMapper.countMeetupLike(meetupLikeDto);
	}

	@Override
	public int deleteMeetupLike(MeetupLikeDto meetupLikeDto) {
		return meetupMapper.deleteMeetupLike(meetupLikeDto);
	}

	@Override
	public MeetupLikeDto selectMeetupLike(MeetupLikeDto meetupLikeDto) {
		return meetupMapper.selectMeetupLike(meetupLikeDto);
	}
	
	// 시도
	@Override
	public List<SidoDto> findAllSido() {
		return meetupMapper.findAllSido();
	}
	
	//시군구
	@Override
	public List<SigunguDto> findAllSigungu() {
		return meetupMapper.findAllSigungu();
	}
	
	//부모카테고리
	@Override
	public List<CategoryDto> findAllCategory() {
		return meetupMapper.findAllCategory();
	}
	
	//자식카테고리
	@Override
	public List<CategoryDto> findAllChildCategory() {
		return meetupMapper.findAllChildCategory();
	}	

}
