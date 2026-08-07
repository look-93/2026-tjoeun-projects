package com.moit.reports.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.moit.reports.entity.MemberReportStatus;

@Repository
public interface MemberReportStatusRepository extends JpaRepository<MemberReportStatus, Long> {

	Optional<MemberReportStatus> findByStatusCode( String statusCode );
	
	boolean existsByStatusCode(String statusCode);
}
