package com.moit.reports.llmrag;

import com.moit.reports.enums.ReasonCode;
import com.moit.reports.enums.TargetType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportAiContext {	// 현재 사건 정보를 담을 DTO

    // 신고 번호
    private Long reportId;

    // MEETUP / REVIEW
    private TargetType targetType;

    // 신고 대상 ID
    private Long targetId;

    // ABUSE / SPAM / FAKE_INFO / NOSHOW / ETC
    private ReasonCode reasonCode;

    // 신고자가 작성한 내용
    private String reasonDetail;

    // 신고 당한 실제 모임/리뷰 원문
    private String targetContent;
}