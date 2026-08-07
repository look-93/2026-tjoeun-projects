package com.moit.reports.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.moit.reports.entity.MemberReportStatus;

@Repository
public interface MemberReportStatusRepository extends JpaRepository<MemberReportStatus, Long> {

	// 사용자 상태 조회 (신뢰도점수, 뱃지 등)
	Optional<MemberReportStatus> findByStatusCode( String statusCode );
}
