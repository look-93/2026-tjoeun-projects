// reducers/reportReducer.js

import { createSlice } from '@reduxjs/toolkit';
// import { resetUserState } from './authReducer';

const initialState= {
    reports: [],                // 전체신고글 목록
    currentReport: null,        // 단건 조회된 상세 신고글
    checkDoubleReport: null,    // 모임,리뷰 중복신고 더블체크
    loading: false,
    error: null,
    success: false,

    auditLogs: [],
    trustInfo: null,

    totalCount: 0,
    totalPage: 0,
};

const reportReducer = createSlice({
    name: "report",
    initialState,
    reducers: {

        // --- 상태 초기화 ---
        resetReportState: (state)=> {
            state.loading = false;
            state.success = false;
            state.error = null;
        },

        // --- 신고글 작성 ---
        createReportRequest: (state)=> {
            state.loading = true;
            state.success = false;
            state.error = null;
        },
        createReportSuccess: (state, action)=> {
            state.loading = false;
            state.success = true;
            state.reports.unshift(action.payload);
        },
        createReportFailure: (state, action)=> {
            state.loading = false;
            state.success = false;
            state.error = action.payload;
        },
        
        // --- 신고글 수정 ---
        updateReportRequest: (state)=> {
            state.loading = true;
            state.success = false;
            state.error = null;
        },
        updateReportSuccess: (state, action)=> {
            state.loading = false;
            state.success = true;
            state.currentReport = action.payload;
            // 새 신고글을 목록상단추가
            state.reports = state.reports.map( report =>
                report.reportId === action.payload.reportId ? action.payload : report
            );
        },
        updateReportFailure: (state, action)=> {
            state.loading = false;
            state.success = false;
            state.error = action.payload;
        },
        
        // --- 신고글 삭제 ---
        deleteReportRequest: (state)=> {
            state.loading = true;
            state.success = false;
            state.error = null;
        },
        deleteReportSuccess: (state, action)=> {
            state.loading = false;
            state.success = true;
            // 삭제된 신고글의 reportId 받아서 목록에서 제외
            state.reports = state.reports.filter( report =>
                report.reportId !== action.payload
            );
        },
        deleteReportFailure: (state, action)=> {
            state.loading = false;
            state.success = false;
            state.error = action.payload;
        },

        // --- 내 신고내역 조회 (사용자 신고 목록 조회 + 페이징) ---
        fetchReportsRequest: (state)=> {
            state.loading = true;
            state.success = false;
            state.error = null;
        },
        fetchReportsSuccess: (state, action)=> {
            state.loading = false;
            state.success = true;
            state.reports = action.payload.reports;         // 전체신고글 목록 reports, totalCount, totalPage
            state.totalCount = action.payload.totalCount;   // totalCount
            state.totalPage = action.payload.totalPage;     // totalPage
        },
        fetchReportsFailure: (state, action)=> {
            state.loading = false;
            state.success = false;
            state.error = action.payload;
        },
        
        // --- 사용자 신고 상세 조회 ---
        fetchReportsDetailRequest: (state)=> {
            state.loading = true;
            state.success = false;
            state.error = null;
        },
        fetchReportsDetailSuccess: (state, action)=> {
            state.loading = false;
            state.success = true;
            state.currentReport = action.payload;  // 단건 조회된 상세 신고글
        },
        fetchReportsDetailFailure: (state, action)=> {
            state.loading = false;
            state.success = false;
            state.error = action.payload;
        },
        
        // --- 모임, 리뷰 신고 더블 체크 (화면용 중복 체크) ---
        checkDoubleReportRequest: (state) => {
            state.loading = true;
            state.success = false;
            state.error = null;
        },
        checkDoubleReportSuccess: (state, action) => {
            state.loading = false;
            state.success = true;
            state.checkDoubleReport = action.payload;
        },
        checkDoubleReportFailure: (state, action) => {
            state.loading = false;
            state.success = false;
            state.error = action.payload;
        },
        
        ///////////////////////////////////////////////////////////////////
        // --- 관리자 처리 상태 (승인/반려/신뢰도점수/감사로그) 변경 ---
        updateAdminReportRequest: (state)=> {
            state.loading = true;
            state.success = false;
            state.error = null;
        },
        updateAdminReportSuccess: (state, action)=> {
            state.loading = false;
            state.success = true;
            state.currentReport = action.payload;
            // 새 신고글을 목록상단추가
            state.reports = state.reports.map( report =>
                report.reportId === action.payload.reportId ? action.payload : report
            );
        },
        updateAdminReportFailure: (state, action)=> {
            state.loading = false;
            state.success = false;
            state.error = action.payload;
        },
        
        // --- 관리자 신고 삭제 (물리삭제 -> 논리삭제 변경 + 감사 로그 processReason 포함) ---
        deleteAdminReportRequest: (state)=> {
            state.loading = true;
            state.success = false;
            state.error = null;
        },
        deleteAdminReportSuccess: (state, action)=> {
            state.loading = false;
            state.success = true;
            // 삭제된 신고글의 reportId 받아서 목록에서 제외
            state.reports = state.reports.filter( report =>
                report.reportId !== action.payload
            );
        },
        deleteAdminReportFailure: (state, action)=> {
            state.loading = false;
            state.success = false;
            state.error = action.payload;
        },

        // --- 관리자 신고 목록 조회 + 검색 + 페이징 ---
        fetchAdminReportsRequest: (state)=> {
            state.loading = true;
            state.success = false;
            state.error = null;
        },
        fetchAdminReportsSuccess: (state, action)=> {
            state.loading = false;
            state.success = true;
            state.reports = action.payload.reports;         // 전체신고글 목록 reports, totalCount, totalPage
            state.totalCount = action.payload.totalCount;   // totalCount
            state.totalPage = action.payload.totalPage;     // totalPage
        },
        fetchAdminReportsFailure: (state, action)=> {
            state.loading = false;
            state.success = false;
            state.error = action.payload;
        },
        
        // --- 관리자 신고 상세 조회 ---
        fetchAdminReportsDetailRequest: (state)=> {
            state.loading = true;
            state.success = false;
            state.error = null;
        },
        fetchAdminReportsDetailSuccess: (state, action)=> {
            state.loading = false;
            state.success = true;
            state.currentReport = action.payload;  // 단건 조회된 상세 신고글
        },
        fetchAdminReportsDetailFailure: (state, action)=> {
            state.loading = false;
            state.success = false;
            state.error = action.payload;
        },
        
        ///////////////////////////////////////////////////////
        // --- 관리자 처리 로그 조회 ---
        fetchAdminReportAuditLogsRequest: (state)=> {
            state.loading = true;
            state.success = false;
            state.error = null;
        },
        fetchAdminReportAuditLogsSuccess: (state, action)=> {
            state.loading = false;
            state.success = true;
            state.auditLogs = action.payload;   // 관리자 신고 처리 로그 이력
        },
        fetchAdminReportAuditLogsFailure: (state, action)=> {
            state.loading = false;
            state.success = false;
            state.error = action.payload;
        },

        // --- 신고당한 회원 (신뢰도점수/뱃지) 조회 -
        fetchReportTrustScoreRequest: (state)=> {
            state.loading = true;
            state.success = false;
            state.error = null;
        },
        fetchReportTrustScoreSuccess: (state, action)=> {
            state.loading = false;
            state.success = true;
            state.trustInfo = action.payload;   // 신뢰도점수
        },
        fetchReportTrustScoreFailure: (state, action)=> {
            state.loading = false;
            state.success = false;
            state.error = action.payload;
        },
    }
});

export const {
    createReportRequest, createReportSuccess, createReportFailure,
    updateReportRequest, updateReportSuccess, updateReportFailure,
    deleteReportRequest, deleteReportSuccess, deleteReportFailure,
    fetchReportsRequest, fetchReportsSuccess, fetchReportsFailure,
    fetchReportsDetailRequest, fetchReportsDetailSuccess, fetchReportsDetailFailure,
    checkDoubleReportRequest, checkDoubleReportSuccess, checkDoubleReportFailure,
    resetReportState,
    updateAdminReportRequest, updateAdminReportSuccess, updateAdminReportFailure,
    deleteAdminReportRequest, deleteAdminReportSuccess, deleteAdminReportFailure,
    fetchAdminReportsRequest, fetchAdminReportsSuccess, fetchAdminReportsFailure,
    fetchAdminReportsDetailRequest, fetchAdminReportsDetailSuccess, fetchAdminReportsDetailFailure,
    fetchAdminReportAuditLogsRequest, fetchAdminReportAuditLogsSuccess, fetchAdminReportAuditLogsFailure,
    fetchReportTrustScoreRequest, fetchReportTrustScoreSuccess, fetchReportTrustScoreFailure
} = reportReducer.actions;

export default reportReducer.reducer;
