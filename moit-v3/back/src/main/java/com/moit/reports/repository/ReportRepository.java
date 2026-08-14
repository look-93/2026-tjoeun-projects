package com.moit.reports.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.moit.reports.entity.Report;
import com.moit.reports.enums.ReasonCode;
import com.moit.reports.enums.ReportStatus;
import com.moit.reports.enums.TargetType;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, JpaSpecificationExecutor<Report> {
	// save() findById() findAll() deleteById() existsById() count()
	
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
	
	// 관리자 검색 기능 조회 + 페이징
	// 전체
	Page<Report> findByAdminSearch(Character deleteYn, Pageable pageable);
	// status
	Page<Report> findByStatusAdminSearch(ReportStatus status, Character deleteYn, Pageable pageable);
	// targetType
	Page<Report> findByTargetTypeAdminSearch(TargetType targetType, Character deleteYn, Pageable pageable);
	// 작성자
	Page<Report> findByMemberIdAdminSearch(Long memberId, Character deleteYn, Pageable pageable);
	// 사유
	Page<Report> findByReasonCodeAdminSearch(ReasonCode reasonCode, Character deleteYn, Pageable pageable);
	// 날짜
	Page<Report> findByCreatedAtAdminSearch(LocalDate createdAt, Character deleteYn, Pageable pageable);
}