package com.moit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.moit.reports.dao.ReportsMapper;
import com.moit.reports.dto.ReportsDto;

@SpringBootTest
public class ReportApplicationTests {

	@Autowired
	private ReportsMapper dao;

	@Test
    public void testCalculateTrustScore() {
    	ReportsDto dto = new ReportsDto();
    	// 신고당한 대상 memberId
//    	dto.setTargetMemberId(1);
//    	dto.setStatus("APPROVED");
    	dto.setReportId(31);
    	
    	// 회원 정보 단순 조회용 쿼리 (너 누구야)
    	ReportsDto findMemberTrustInfo = dao.findMemberTrustInfo(dto);

    	// 신고당한 대상 아이디(정보) 불러오기
    	int targetMemberId = dao.selectTargetMemberId(dto);
    	
    	int approvedCnt = dao.selectApprovedCnt(targetMemberId);
    	int noshowCnt = dao.selectNoshowCnt(targetMemberId);
    	int reportCnt = dao.selectReportCnt(targetMemberId);
        //신뢰도 점수 test
        int calculatedScore = dao.calTrustScore(targetMemberId);
        
        System.out.println("==============================================");
        System.out.println("누구신지? " + findMemberTrustInfo);
        System.out.println("==============================================");
        System.out.println("테스트 유저 (" + targetMemberId + ")의 계산된 신뢰 점수: " + calculatedScore + "점");
        System.out.println("승인 횟수: " + approvedCnt);
        System.out.println("노쇼 횟수: " + noshowCnt);
        System.out.println("신고 횟수: " + reportCnt);
        System.out.println("==============================================");

    }
}
