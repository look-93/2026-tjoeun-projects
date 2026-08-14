package com.moit.reports.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.moit.reports.dto.ReportSearchDto;
import com.moit.reports.dto.ReportsDto.ReportListResponseDto;
import com.moit.reports.entity.Report;
import com.moit.reports.enums.ReportStatus;
import com.moit.reports.enums.TargetType;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, JpaSpecificationExecutor<Report> {
	// 사용자 신고 수정
	Optional<Report> findByReportIdAndMember_IdAndDeleteYnAndStatus(
        Long reportId, Long memberId, Character deleteYn, ReportStatus status
    );
	
	// 사용자 신고 목록 조회 + 페이징
	Page<Report> findByMember_IdAndDeleteYnOrderByReportIdDesc(
        Long memberId, Character deleteYn, Pageable pageable
    );
	
	// 사용자 신고 상세 조회
	Optional<Report> findByReportIdAndMember_IdAndDeleteYn(
        Long reportId, Long memberId, Character deleteYn
    );

	// 중복 신고 확인
	boolean existsByMember_IdAndTargetTypeAndTargetIdAndDeleteYn(
        Long memberId, TargetType targetType, Long targetId, Character deleteYn
    );

	// 관리자 (승인/반려) 처리를 위한 신고 조회
	Optional<Report> findByReportIdAndStatus(Long reportId, ReportStatus status);
}

/*
 * save() findById() findAll() deleteById() existsById() count()
 */