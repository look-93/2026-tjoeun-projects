package com.moit.report.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.moit.reports.dao.ReportsMapper;
import com.moit.reports.dto.ReportsDto;

@SpringBootTest
@Transactional
class ReportsMapperTest {

    @Autowired private ReportsMapper reportsMapper;

    @Test
    @DisplayName("ReportsMapper Bean 생성 테스트")
    void mapperBeanTest() {
        assertThat(reportsMapper).isNotNull();
    }
    
    @Test
    @DisplayName("회원이 작성한 신고 개수 조회")
    void selectUserCntTest() {
        Long memberId = 1L;
        int count = reportsMapper.selectUserCnt(memberId);
        
        assertThat(count).isEqualTo(0);
        
        System.out.println("회원 신고 개수 = " + count);
    }
    
    @Test
    @DisplayName("관리자 전체 신고 목록 조회")
    void selectAdminReportsTest() {
        HashMap<String, Object> map = new HashMap<>();
        
        map.put("deleteYn", "N");
        // 조건 없이 전체 조회
        map.put("status", null);
        map.put("targetType", null);
        map.put("reportId", null);
        // 검색
        map.put("searchType", null);
        map.put("keyword", null);
        // 페이징
        map.put("start", 0);
        map.put("end", 10);

        map.put("memberId", null);
        map.put("reasonCode", null);
        map.put("createdAt", null);

        List<ReportsDto> reports = reportsMapper.selectAdminReports(map);
        assertThat(reports).isNotNull();
        
        reports.forEach(report ->
            System.out.println(
                "reportId = " + report.getReportId()
                + ", targetType = " + report.getTargetType()
                + ", reasonCode = " + report.getReasonCode()
                + ", status = " + report.getStatus()
            )
        );
    }
}