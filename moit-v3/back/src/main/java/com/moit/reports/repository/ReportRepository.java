package com.moit.reports.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.moit.reports.entity.Report;
import com.moit.reports.entity.Status;
import com.moit.reports.entity.TargetType;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
	// 사용자 신고 목록 조회 + 페이징
	Page<Report> findByMember_MemberIdAndDeleteYnOrderByReportIdDesc(
        Long memberId,
        Character deleteYn,
        Pageable pageable
    );
	
	// 사용자 신고 상세 조회
	Optional<Report> findByReport_IdAndMember_IdAndDeleteYn(
	    Long reportId,
	    Long memberId,
	    Character deleteYn
	);
	
	// 사용자 신고 수정
	Optional<Report> findByReport_IdAndMember_IdAndDeleteYnAndStatus(
	    Long reportId,
	    Long memberId,
	    Character deleteYn,
	    Status status
	);
	
	// 중복 신고 확인
	boolean existsByMember_IdAndTarget_TypeAndTarget_IdAndDeleteYn(
	    Long memberId,
	    TargetType targetType,
	    Long targetId,
	    Character deleteYn
	);
	
	// 관리자 처리 상태 변경
	Optional<Report> findByRepor_tIdAndStatus(
	    Long reportId,
	    Status status
	);
}

/*
save()
findById()
findAll()
deleteById()
existsById()
count()
*/