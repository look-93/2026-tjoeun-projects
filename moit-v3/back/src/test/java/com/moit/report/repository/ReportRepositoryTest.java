package com.moit.report.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.moit.reports.repository.MemberReportStatusRepository;
import com.moit.reports.repository.ReportAuditLogRepository;
import com.moit.reports.repository.ReportRepository;

// @SpringBootTest
// @Transactional
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReportRepositoryTest {

	@Autowired ReportRepository reportRepository;
	@Autowired MemberReportStatusRepository memberReportStatusRepository;
	@Autowired ReportAuditLogRepository reportAuditLogRepository;
	
	@Test
	@DisplayName("신고 Repository 전체 Bean 생성 테스트")
	void repositoryBeanTest() {
		
		assertThat(reportRepository).isNotNull();
		assertThat(memberReportStatusRepository).isNotNull();
		assertThat(reportAuditLogRepository).isNotNull();
	}

}
