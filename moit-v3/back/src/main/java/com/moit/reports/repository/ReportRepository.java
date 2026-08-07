package com.moit.reports.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.moit.reports.entity.Report;
import com.moit.reports.entity.Status;
import com.moit.reports.entity.TargetType;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
	
	// 사용자 신고 목록 조회 + 페이징 (selectUserReport)
	Page<Report> findByMember_MemberIdAndDeleteYn(
    	Long memberId, Character deleteYn, Pageable pageable
    );
	
	// 사용자가 작성한 신고 건수 (selectUserCnt)
	long countByMember_MemberIdAndDeleteYn(
        Long memberId, Character deleteYn
    );
	
	// 사용자 신고 상세 조회 (selectUserReportDetail)
	Optional<Report> findByReportIdAndMember_MemberIdAndDeleteYn(
        Long reportId, Long memberId, Character deleteYn
    );
	
	// 사용자 신고 수정 조회
	Optional<Report> findByReportIdAndMember_MemberIdAndDeleteYnAndStatus(
        Long reportId, Long memberId, Character deleteYn, Status status
    );
	
	// 중복 신고 확인
	boolean existsByMember_MemberIdAndTargetTypeAndTargetIdAndDeleteYn(
        Long memberId, TargetType targetType, Long targetId, Character deleteYn
    );
	
	// 관리자 처리 상태 변경
	Optional<Report> findByReportIdAndStatus( Long reportId, Status status );
}

/*
save()
findById()
findAll()
deleteById()
existsById()
count()
*/