package com.moit.reports.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.moit.reports.entity.MemberReportStatus;

@Repository
public interface MemberReportStatusRepository extends JpaRepository<MemberReportStatus, Long> {
	// 상태 코드 조회
	Optional<MemberReportStatus> findByStatusCode( String statusCode );
	
	// 상태 코드 존재 여부 확인
	boolean existsByStatusCode(String statusCode);
}
