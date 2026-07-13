package com.moit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.moit.reports.api.ApiScheduledTask;
import com.moit.reports.dao.ReportsMapper;
import com.moit.reports.dto.ReportsDto;

@SpringBootTest
public class ReportApplicationTests {

	@Autowired
	private ReportsMapper dao;
	@Autowired
	private ApiScheduledTask apiScheduledTask;
	
	@Disabled
	@Test
    public void testCalculateTrustScore() {
		
    	ReportsDto dto = new ReportsDto();	// 신고당한 대상 memberId
//    	dto.setTargetMemberId(1);			// dto.setStatus("APPROVED");
    	dto.setReportId(34);		// 신고 고유 번호
    	dto.setTargetMemberId(31);	// 신고당한 대상
    	
    	// 회원 정보 단순 조회용 쿼리 (뱃지 등...)
    	ReportsDto findMemberTrustInfo = dao.findMemberTrustInfo(dto);

    	// 신고당한 대상 고유 m.member_id 불러오기
    	int targetMemberId = dao.selectTargetMemberId(dto);
    	// 신고당한 대상 닉네임(m.nickname) 불러오기
    	String targetMemberNickname = dao.selectTargetMemberNickname(dto);
    	
    	int approvedCnt = dao.selectApprovedCnt(targetMemberId);
    	int noshowCnt = dao.selectNoshowCnt(targetMemberId);
    	int reportCnt = dao.selectReportCnt(targetMemberId);
        //신뢰도 점수 test
        int calTrustScore = dao.calTrustScore(targetMemberId);
        
        
        System.out.println("==============================================");
        System.out.println("신고당한 사람 누구신지? (dto) " + findMemberTrustInfo);
        System.out.println("==============================================");
        System.out.println("신고당한 테스트 유저: " + targetMemberId);
        System.out.println("신고당한 테스트 유저(닉네임): " + targetMemberNickname);
        
        System.out.println("\n신뢰도 점수(쿼리1, mi.trust_score) : " + calTrustScore);	// 쿼리 1개
        System.out.println("신뢰도 점수(쿼리3, 100): " + findMemberTrustInfo.getTrustScore());
        
        System.out.println("\n뱃지: " + findMemberTrustInfo.getStatusName());
        System.out.println("승인 횟수: " + approvedCnt);
        System.out.println("노쇼 횟수: " + noshowCnt);
        System.out.println("신고 횟수: " + reportCnt);
        System.out.println("==============================================");

    }
	
	
	
	@Test
	public void schedule() {
		
		//3일 후 메일
//		apiScheduledTask.threeSendEmail();
		
		//새벽배치 cal 계산(90일 이력)
		apiScheduledTask.yesterdayMembersCal();
	}
}
