// reducers/reportReducer.js

import { createSlice } from '@reduxjs/toolkit';
// import { resetUserState } from './authReducer';

const initialState= {
    reports: [],          // 전체신고글 목록
    currentReport: null,  // 단건 조회된 상세 신고글
    loading: false,
    error: null,
    success: false,
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

        // --- 전체 신고글 ---
        fetchReportsRequest: (state)=> {
            state.loading = true;
            state.success = false;
            state.error = null;
        },
        fetchReportsSuccess: (state, action)=> {
            state.loading = false;
            state.success = true;
            state.reports = action.payload;   // 전체신고글 목록
        },
        fetchReportsFailure: (state, action)=> {
            state.loading = false;
            state.success = false;
            state.error = action.payload;
        },
        
        // --- 단건 신고글 ---
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
                report.id === action.payload.id ? action.payload : report
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
            // 삭제된 신고글의 id 받아서 목록에서 제외
            state.reports = state.reports.filter( report =>
                report.id !== action.payload
            );
        },
        deleteReportFailure: (state, action)=> {
            state.loading = false;
            state.success = false;
            state.error = action.payload;
        },
        
        
    }
});

export const {
    fetchReportsRequest, fetchReportsSuccess, fetchReportsFailure,
    fetchReportsDetailRequest, fetchReportsDetailSuccess, fetchReportsDetailFailure,
    createReportRequest, createReportSuccess, createReportFailure,
    updateReportRequest, updateReportSuccess, updateReportFailure,
    deleteReportRequest, deleteReportSuccess, deleteReportFailure,
    resetReportState
} = reportReducer.actions;

export default reportReducer.reducer;
