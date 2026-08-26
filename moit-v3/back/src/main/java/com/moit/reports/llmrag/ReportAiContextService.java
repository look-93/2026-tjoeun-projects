package com.moit.reports.llmrag;

import org.springframework.stereotype.Service;

import com.moit.meetup.dto.MeetupDto.MeetupResponseDto;
import com.moit.meetup.entity.Meetup;
import com.moit.meetup.repository.MeetupRepository;
import com.moit.meetup.service.MeetupService;
import com.moit.reports.entity.Report;
import com.moit.reports.enums.TargetType;
import com.moit.reports.repository.ReportRepository;
import com.moit.review.dto.ReviewDto.ReviewResponseDto;
import com.moit.review.entity.Review;
import com.moit.review.repository.ReviewRepository;
import com.moit.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportAiContextService {

	private final ReportRepository reportRepository;
	private final MeetupRepository meetupRepository;
	private final ReviewRepository reviewRepository;

	// 현재 신고 + 신고 대상 원문을 AI 분석용 객체로 만들기
	public ReportAiContext getReportContext(Long reportId) {
		// 신고 조회
		Report report = reportRepository.findById(reportId)
				.orElseThrow(() -> new IllegalArgumentException("신고를 찾을 수 없습니다."));

		// 신고 대상 원문 조회
		String targetContent;

		if (report.getTargetType() == TargetType.MEETUP) {
			targetContent = getMeetupContent(report.getTargetId());

		} else if (report.getTargetType() == TargetType.REVIEW) {
			targetContent = getReviewContent(report.getTargetId());

		} else {
			targetContent = "신고 대상 원문을 확인할 수 없습니다.";
		}

		// AI 분석용 객체 반환
		return new ReportAiContext(
				report.getReportId(),
				report.getTargetType(),
				report.getTargetId(),
				report.getReasonCode(),
				report.getReasonDetail(),
				targetContent
		);
	}

	// 모임 원문 조회
	private String getMeetupContent(Long meetupId) {
		Meetup meetup = meetupRepository.findById(meetupId)
				.orElseThrow(() -> new IllegalArgumentException("신고 대상 모임을 찾을 수 없습니다."));

		return """
				[신고 대상 모임]

				제목:
				%s

				내용:
				%s

				장소:
				%s %s

				모임 일시:
				%s
				""".formatted(meetup.getTitle(), meetup.getContent(), meetup.getAddress(), meetup.getAddressDetail(),
				meetup.getMeetupAt());
	}

	// 리뷰 원문 조회
	// 조회수 안 올라감, 비공개 리뷰 차단 x
	private String getReviewContent(Long reviewId) {
		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new IllegalArgumentException("신고 대상 리뷰를 찾을 수 없습니다."));

		return """
				[신고 대상 리뷰]

				리뷰 내용:
				%s

				별점:
				%s
				""".formatted(review.getContent(), review.getRating());
	}
}