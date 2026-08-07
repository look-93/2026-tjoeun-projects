package com.moit.report.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.moit.reports.dao.ReportsMapper;
import com.moit.reports.dto.ReportsDto;
import com.moit.reports.repository.MemberReportStatusRepository;
import com.moit.reports.repository.ReportAuditLogRepository;
import com.moit.reports.repository.ReportRepository;

// @SpringBootTest
// @Transactional
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableJpaRepositories(
    basePackageClasses = {
    		ReportRepository.class,
    		MemberReportStatusRepository.class,
    		ReportAuditLogRepository.class,
    }
)
@ImportAutoConfiguration(exclude = {
    JpaRepositoriesAutoConfiguration.class
})
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
