package com.moit.reports.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moit.reports.api.ApiOpenAi;
import com.moit.reports.dto.AiReportsDto;

import com.moit.reports.dto.MemberTrustInfoDto;
import com.moit.reports.dto.ReportAuditLogDto;
import com.moit.reports.dto.ReportSearchDto;
import com.moit.reports.dto.ReportsDto.ReportListResponseDto;
import com.moit.reports.dto.ReportsDto.ReportProcessDto;
import com.moit.reports.dto.ReportsDto.ReportRequestDto;
import com.moit.reports.dto.ReportsDto.ReportResponseDto;
import com.moit.reports.enums.ReasonCode;
import com.moit.reports.enums.ReportStatus;
import com.moit.reports.enums.TargetType;
import com.moit.reports.service.ReportsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Reports Api", description = "신고 관련 API")
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
   
   private final ReportsService reportsService;

   private final ReportsService reportsService;
   private final ApiOpenAi apiOpenAi;
//   private final ApiEmail apiEmail;
   

   
   // test button
//   @RequestMapping("/user/meetup/report/button")
//    public String reportButton() {
//        return "user/meetup/report/button";
//    }
   
   // 사용자 로그인 헬퍼
//   private Long getLoginMemberId(Authentication authentication) {
//      Integer id = (Integer) session.getAttribute("loginMemberId");
//      return (id != null) ? id : 1; // 일반회원 테스트용
//   }
//   
//   // 내 신고내역 조회 (사용자 신고 목록 조회 + 페이징)
//   @Operation(summary = "내 신고내역 조회", description = "")
//   @GetMapping
//   public ResponseEntity<ReportListResponseDto> getReportsMylist (
//         Authentication authentication, 
//         @Parameter(description = "작성자 ID") @RequestParam("memberId") Long memberId,
//         @PageableDefault(size = 10) Pageable pageable ) {
//      
//      // 로그인한 memberId 꺼내오기
////      Long memberId = authUserJwtService.getCurrentMemberId(authentication);
//      
//      ReportListResponseDto response = reportsService.getUserReports(memberId, pageable);
//      return ResponseEntity.ok(response);
//   }
//   
//   // 사용자 신고 상세 조회
//   @Operation(summary = "내 신고내역 상세조회", description = "")
//   @GetMapping("/{reportId}")
//   public ResponseEntity<ReportResponseDto> getReportMylistDetail (
//         @PathVariable("reportId") Long reportId) {
//      
//      // 로그인 하드코딩
//      Long memberId = 2L;
//      
//      // 로그인한 memberId 꺼내오기
////      Long memberId = authUserJwtService.getCurrentMemberId(authentication);
//      
//      ReportResponseDto report = reportsService.getUserReportDetail(reportId, memberId);
//      return ResponseEntity.ok(report);
//   }
   
   //   @PathVariable
   //   → URL 경로 중간에 있는 값
   //
   //   @RequestParam
   //   → URL 뒤 ?key=value 값
   //
   //   @RequestBody
   //   → body의 JSON 데이터
   //
   //   @ModelAttribute
   //   → 여러 파라미터를 DTO로 묶어서 받기
   
   // 신고 작성
   @Operation(summary = "사용자 신고 작성", description = "")
   @PostMapping
   public ResponseEntity<ReportResponseDto> createReport(
         Authentication authentication,
         @Parameter(description = "작성자 ID") @RequestParam("memberId") Long memberId,
         @RequestBody ReportRequestDto requestDto ) {
      
      // 로그인한 memberId 꺼내오기
//      Long memberId = authUserJwtService.getCurrentMemberId(authentication);
      
      return ResponseEntity.ok( reportsService.createUserReport(memberId, requestDto));
   };
   
   // 신고 수정
   @Operation(summary = "사용자 신고 수정", description = "")
   @PatchMapping(value = "/{reportId}")
   public ResponseEntity<ReportResponseDto> updateReport(
         Authentication authentication,
         @Parameter(description = "수정할 신고글 ID") @PathVariable(name = "reportId") Long reportId,
         @RequestBody ReportRequestDto requestDto ) {
      
      // 로그인 하드코딩
      Long memberId = 2L;
      
      // 로그인한 memberId 꺼내오기
//      Long memberId =  authUserJwtService.getCurrentMemberId(authentication);
      
      ReportResponseDto response = reportsService.updateUserReport(reportId, memberId, requestDto);
      
      return ResponseEntity.ok(response);
   };
   
   // 신고 삭제
   @Operation(summary = "사용자 신고 삭제", description = "")
   @DeleteMapping("/{reportId}")
   public ResponseEntity<Long> deleteReport(
         Authentication   authentication,
         @PathVariable("reportId") Long reportId ) {
      
      // 로그인 하드코딩
      Long memberId = 2L;
      
      // 로그인한 memberId 꺼내오기
//      Long memberId = authUserJwtService.getCurrentMemberId(authentication);
      
      reportsService.deleteUserReport(reportId, memberId);
      return ResponseEntity.ok(reportId);
   }
   
   // 내 신고내역 조회 (사용자 신고 목록 조회 + 페이징)
   @Operation(summary = "내 신고내역 조회", description = "")
   @GetMapping
   public ResponseEntity<ReportListResponseDto> getReportsMylist (
         Authentication authentication, 
         @Parameter(description = "작성자 ID") @RequestParam("memberId") Long memberId,
         @PageableDefault(size = 10) Pageable pageable ) {
      
      // 로그인한 memberId 꺼내오기
//      Long memberId = authUserJwtService.getCurrentMemberId(authentication);
      
      ReportListResponseDto response = reportsService.getUserReports(memberId, pageable);
      return ResponseEntity.ok(response);
   }
   
   // 사용자 신고 상세 조회
   @Operation(summary = "내 신고내역 상세조회", description = "")
   @GetMapping("/{reportId}")
   public ResponseEntity<ReportResponseDto> getReportMylistDetail (
         @PathVariable("reportId") Long reportId) {
      
      // 로그인 하드코딩
      Long memberId = 2L;
      
      // 로그인한 memberId 꺼내오기
//      Long memberId = authUserJwtService.getCurrentMemberId(authentication);
      
      ReportResponseDto report = reportsService.getUserReportDetail(reportId, memberId);
      return ResponseEntity.ok(report);
   }
   
   // 중복 신고 확인 (true = 중복 신고, false = 신고 가능)
   @Operation(summary = "사용자 중복 신고 확인", description = "같은 사용자가 같은 모임/리뷰를 이미 신고했는지 확인합니다.")
   @GetMapping("/checkDoubleReport")
   public ResponseEntity<Boolean> checkDoubleReport (
           @RequestParam("memberId") Long memberId,
           @RequestParam("targetType") TargetType targetType,
           @RequestParam("targetId") Long targetId ) {

       boolean response = reportsService.checkDoubleReport(memberId, targetType, targetId);
       return ResponseEntity.ok(response);
   }
   
   
   
   ////////////////////////////////////////////////////////////////
   // 관리자 신고 수정
   @Operation(summary = "관리자 신고 처리 (승인/반려)", description = "")
   @PatchMapping(value = "/admin/{reportId}")
   public ResponseEntity<ReportResponseDto> updateAdminReport(
         Authentication authentication,
         @Parameter(description = "처리할 신고글 ID") @PathVariable(name = "reportId") Long reportId,
         @RequestBody ReportProcessDto processDto ) {
      
      // 관리자 로그인 하드코딩
      Long MemberId = 99L;
      
      // 로그인한 adminMemberId 꺼내오기
//      Long adminMemberId =  authUserJwtService.getCurrentMemberId(authentication);
      
      ReportResponseDto response = reportsService.updateAdminReport(reportId, MemberId, processDto);
      return ResponseEntity.ok(response);
   };
   
   // 관리자 신고 삭제
   @Operation(summary = "관리자 신고 삭제", description = "")
   @DeleteMapping("/admin/{reportId}")
   public ResponseEntity<Long> deleteAdminReport(
         Authentication   authentication,
         @PathVariable("reportId") Long reportId,
         @RequestParam("processReason") String processReason ) {
      
      // 로그인 하드코딩
      Long adminMemberId = 99L;
      
      // 로그인한 adminMemberId 꺼내오기
//      Long adminMemberId = authUserJwtService.getCurrentMemberId(authentication);
      
      reportsService.deleteAdminReport(reportId, adminMemberId, processReason);
      return ResponseEntity.ok(reportId);
   }
   
   // 관리자 신고 목록 조회 + 검색 + 페이징
   @Operation(summary = "관리자 신고 목록 조회", description = "필터(버튼)이랑 서치(키워드)를 혼합하여 검색합니다.")
   @GetMapping("/admin/adminReportsList")
   public ResponseEntity<ReportListResponseDto> getReportsAdmin (
         Authentication   authentication,
         @ModelAttribute ReportSearchDto searchDto,   // 검색조건
         @PageableDefault(size = 10) Pageable pageable ) {
      
      // 로그인 하드코딩
      Long adminMemberId = 99L;
      
      // 로그인한 adminMemberId 꺼내오기
//      Long adminMemberId = authUserJwtService.getCurrentMemberId(authentication);
      
      searchDto.setTargetType(null);
      searchDto.setStatus(null);
      searchDto.setDeleteYn(null);
      searchDto.setMemberNickname(null);
      searchDto.setReasonCode(null);
      
      // 필터(버튼) 기능
      String filter = searchDto.getFilter();
      // 서치(키워드) 기능
      String search = searchDto.getSearch();
      String keyword = searchDto.getKeyword();
      
      // 전체
      if ("ALL".equals(filter) || filter == null || filter.isEmpty() || filter.isBlank()) {
         searchDto.setDeleteYn('N');
      }
      // 신고 상태
      if ("MEETUP".equals(filter)) {
         searchDto.setTargetType(TargetType.MEETUP);
         searchDto.setDeleteYn('N');
      }
      if ("REVIEW".equals(filter)) {
         searchDto.setTargetType(TargetType.REVIEW);
         searchDto.setDeleteYn('N');
      }
      // 처리 상태
      if ("PENDING".equals(filter)) {
         searchDto.setStatus(ReportStatus.PENDING);
         searchDto.setDeleteYn('N');
      }
      // 삭제 여부
      if ("DELETE".equals(filter)) {
         searchDto.setDeleteYn('Y');
      }

      if (keyword != null && !keyword.isBlank()) {
         // 작성자(닉네임)
         if ("MEMBER_NICKNAME".equals(search) ) {
            searchDto.setMemberNickname(keyword.trim());
         }
         // 신고 사유
         if ("REASONCODE".equals(search) ) {
            searchDto.setReasonCode( ReasonCode.valueOf(keyword.trim().toUpperCase()) );
         }
      }
      ReportListResponseDto response = reportsService.getAdminReports(searchDto, pageable);
      return ResponseEntity.ok(response);
   }
   
   // 관리자 리스트 상세보기
   @Operation(summary = "관리자 리스트 상세 조회", description = "관리자 리스트를 상세 조회합니다.")
   @GetMapping("/admin/{reportId}")
   public ResponseEntity<ReportResponseDto> getReportAdminDetail(
         @PathVariable("reportId") Long reportId) {
      
      ReportResponseDto report = reportsService.getAdminReportDetail(reportId);
      return ResponseEntity.ok(report);
   }

   
   
   //////////////////////////////////////////////////////////
   // 관리자 신고 처리 로그 조회
   @Operation(summary = "관리자 신고 처리 로그 조회", description = "신고별 관리자 처리 이력을 조회합니다.")
   @GetMapping("/admin/{reportId}/auditLogs")
   public ResponseEntity<List<ReportAuditLogDto>> getReportAuditLogs (
           @PathVariable("reportId") Long reportId ) {

       List<ReportAuditLogDto> response = reportsService.getReportAuditLogs(reportId);
       return ResponseEntity.ok(response);
   }
   
   // 신고당한 회원 신뢰도 점수 / 뱃지 조회
   @Operation(summary = "신고 대상 회원 신뢰도 정보 조회", description = "신고 대상 회원의 신뢰도 점수와 신고 상태 뱃지를 조회합니다.")
   @GetMapping("/admin/member/{targetMemberId}/trustInfo")
   public ResponseEntity<MemberTrustInfoDto> getMemberTrustInfo (
           @PathVariable("targetMemberId") Long targetMemberId ) {

       MemberTrustInfoDto response =reportsService.getMemberTrustInfo(targetMemberId);
       return ResponseEntity.ok(response);
   }
   
   
   //////////////////////////////////////////////////////////
   // ApiOpenAi
   @Operation(summary = "AI 신고 내용 작성", description = "키워드, 사유, 타겟타입 기반으로 AI가 신고 내용을 작성합니다.")
   @PostMapping("/openai")
   public ResponseEntity<String> createReportApiOpenAi (
         @RequestBody AiReportsDto dto ) {
      
      String response = apiOpenAi.getAIResponse(dto);
      return ResponseEntity.ok(response);
   }

   //////////////////////////////////////////////////////////
   // ApiEmail
//   @Operation(summary = "AI 신고 내용 작성", description = "사용자가 입력한 키워드를 기반으로 AI가 신고 내용을 작성합니다.")
//   @PostMapping("/openai")
//   public ResponseEntity<String> createReportApiOpenAi (
//         @RequestBody Map<String, String> request ) {
//      
//      String keywords = request.get("keywords");
//      return ResponseEntity.ok(apiOpenAi.getAIResponse(keywords));
//   }
}