package com.moit.reports.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.moit.reports.entity.ReportAuditLog;

@Repository
public interface ReportAuditLogRepository extends JpaRepository<ReportAuditLog, Long> {
	
	// 관리자 처리 로그 조회
	List<ReportAuditLog> findByReport_ReportIdOrderByProcessedAtDesc(Long reportId);
}
