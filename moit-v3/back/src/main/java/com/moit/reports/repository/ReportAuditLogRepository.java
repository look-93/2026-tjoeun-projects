package com.moit.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.moit.reports.entity.ReportAuditLog;

@Repository
public interface ReportAuditLogRepository extends JpaRepository<ReportAuditLog, Long> {

}
