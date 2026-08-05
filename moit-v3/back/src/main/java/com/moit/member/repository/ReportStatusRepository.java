package com.moit.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moit.member.entity.ReportStatus;

@Repository
public interface ReportStatusRepository extends JpaRepository<ReportStatus, Long>{
	
	// 신고 상태 조회 (ACTIVE, WARNING ....)
	Optional<ReportStatus> findByStatusName(String statusName);
}
