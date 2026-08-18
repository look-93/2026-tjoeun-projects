import { createSlice } from "@reduxjs/toolkit";

//1. 초기화 상태 (공용)
const initialState={
    qnaList: [],    // QnA 목록
    qna:   null,    // QnA 상세
    loading: false, // 로딩상태
    error: null,    // 에러메시지
    success: false, // 성공여부
};

//2. 상태변화
const qnaReducer=createSlice({
    name: "qna",
    initialState,
    reducers: {
        // --- QnA 등록 ---
        qnaCreateRequest: (state) => {
            state.loading = true;
            state.error = null;
            state.success = false;
        },
        qnaCreateSuccess: (state) => {
            state.loading = false;
            state.success = true;
        },
        qnaCreateFailure: (state, action) => {
            state.loading = false;
            state.error = action.payload;
            state.success = false;
        },
        // --- QnA 목록 조회 ---
        qnaListRequest: (state) => {
            state.loading = true;
            state.error = null;
        },
        qnaListSuccess: (state, action) => {
            state.loading = false;
            state.qnaList = action.payload;
        },
        qnaListFailure: (state, action) => {
            state.loading = false;
            state.error = action.payload;
        },
        // --- QnA 특정 모임 조회 ---
        qnaMeetupListRequest: (state) => {
            state.loading = true;
            state.error = null;
        },

        qnaMeetupListSuccess: (state, action) => {
            state.loading = false;
            state.qnaList = action.payload;
        },

        qnaMeetupListFailure: (state, action) => {
            state.loading = false;
            state.error = action.payload;
        },
        // --- QnA 상세 조회 ---
        qnaDetailRequest: (state) => {
            state.loading = true;
            state.error = null;
        },
        qnaDetailSuccess: (state, action) => {
            state.loading = false;
            state.qna = action.payload;
        },
        qnaDetailFailure: (state, action) => {
            state.loading = false;
            state.error = action.payload;
        },
        // --- 상태 초기화 ---
        qnaReset: (state) => {
            state.qna = null;
            state.error = null;
            state.success = false;
        },
    },
});

//3. action
export const {qnaCreateRequest, qnaCreateSuccess, qnaCreateFailure,
              qnaListRequest, qnaListSuccess, qnaListFailure,
              qnaMeetupListRequest, qnaMeetupListSuccess, qnaMeetupListFailure,
              qnaDetailRequest, qnaDetailSuccess, qnaDetailFailure,
              qnaReset,
} = qnaReducer.actions;

//4. export
export default qnaReducer.reducer;