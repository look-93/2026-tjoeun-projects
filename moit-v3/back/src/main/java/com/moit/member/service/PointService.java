package com.moit.member.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moit.member.entity.Member;
import com.moit.member.entity.PointHistory;
import com.moit.member.repository.MemberRepository;
import com.moit.member.repository.PointHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointService {

    private final PointHistoryRepository pointHistoryRepository;
    private final MemberRepository memberRepository;

    // 현재 보유 포인트 조회
    public Integer getCurrentPoint(Long memberId) {

        return pointHistoryRepository.findCurrentPoint(memberId);
    }

    // 포인트 내역 조회
    public List<PointHistory> getPointHistory(Long memberId) {

        return pointHistoryRepository
                .findByMember_IdOrderByCreatedAtDesc(memberId);
    }
    
    // 출석체크
    @Transactional
    public Integer checkAttendance(Long memberId) {

        String pointReason = "출석체크";

        LocalDateTime start = LocalDate.now().atStartOfDay();

        LocalDateTime end = start.plusDays(1);

        boolean alreadyChecked = pointHistoryRepository
                        .existsByMember_IdAndPointReasonAndCreatedAtBetween( memberId, pointReason, start, end );

        if (alreadyChecked) {
            throw new IllegalArgumentException( "오늘은 이미 출석체크를 완료했습니다." );
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException( "회원정보를 찾을 수 없습니다." ));

        PointHistory history = new PointHistory();

        history.setMember(member);
        history.setPointPm(10);
        history.setPointType("PLUS");
        history.setPointReason(pointReason);

        pointHistoryRepository.save(history);

        return pointHistoryRepository.findCurrentPoint(memberId);
    }
    
	// 월별 출석 기록 조회
    public List<PointHistory> getAttendanceHistory(
	         Long memberId,
	         int year,
	         int month) {
	
		LocalDate firstDay = LocalDate.of(year, month, 1);	
		LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());	
		LocalDateTime start = firstDay.atStartOfDay();	
		LocalDateTime end = lastDay.plusDays(1).atStartOfDay();
		
		return pointHistoryRepository
		         .findByMember_IdAndPointReasonAndCreatedAtBetween(
		                 memberId,
		                 "출석체크",
		                     start,
		                     end
		             );
	}
    
}