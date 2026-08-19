import { createSlice } from "@reduxjs/toolkit";

//1. 초기화 상태(공용)
const initialState = {
    user: null,     //단건 조회된 사용자 정보
    members: [],    // 전체 조회
    accessToken: null,
    refreshToken: null,
    loading:false,  //로딩상태
    error: null,    //에러메시지
    success:false,   //성공여부
    membersLoading: false,
    membersError: null,

    // 중복확인
    duplicateCheck: {
        loginId: null,
        email: null,
        nickname: null,
        mobile: null,
    },

    // 이메일 인증
    emailVerification: {
        sending: false,
        verifying: false,
        sent: false,
        verified: false,
        error: null,
    },

    // 비밀번호 유출 검사
    passwordLeak: {
        checking: false,
        checked: false,
        leaked: false,
        count: 0,
        error: null,
    },
};

//2. 상태변화
const userReducer = createSlice({
    name: "user",
    initialState,
    reducers: {

        // =========================
        // 로그인
        // =========================
        loginRequest: (state)=>{
            state.loading = true;
            state.success = false;
            state.error = null;            
        },
        loginSuccess: (state,action)=>{
            state.loading = false;
            state.success = true;
            state.error = null;

            state.accessToken = action.payload.accessToken;
            state.refreshToken = action.payload.refreshToken;

            state.user = {
                memberId : action.payload.memberId,
                loginId : action.payload.loginId,
                memberTypeId: action.payload.memberTypeId,
            };
        },
        loginFailure: (state,action)=>{
            state.loading = false;
            state.success = false;
            state.error = action.payload;
        },
        // =========================
        // 내 정보 조회
        // =========================
        getMyInfoRequest: (state)=>{
            state.loading = true;
            state.error = null;
        },
        getMyInfoSuccess: (state,action)=>{
            state.loading = false;
            state.error = null;
            state.user = action.payload;
        },
        getMyInfoFailure: (state,action)=>{
            state.loading = false;
            state.error = action.payload;
        },

        // =========================
        // 회원가입
        // =========================
        signupRequest: (state)=>{
            state.loading = true;
            state.success = false;
            state.error = null;
        },
        signupSuccess: (state)=>{
            state.loading = false;
            state.success = true;
            state.error = null;
        },
        signupFailure: (state,action)=>{
            state.loading = false;
            state.success = false;
            state.error = action.payload;
        },

        // =========================
        // 비밀번호 유출 검사
        // =========================

        checkPasswordLeakRequest: (state) => {
            state.passwordLeak.checking = true;
            state.passwordLeak.checked = false;
            state.passwordLeak.leaked = false;
            state.passwordLeak.count = 0;
            state.passwordLeak.error = null;
        },
        checkPasswordLeakSuccess: (state, action) => {
            state.passwordLeak.checking = false;
            state.passwordLeak.checked = true;
            state.passwordLeak.leaked = action.payload.leaked;
            state.passwordLeak.count = action.payload.count;
            state.passwordLeak.error = null;
        },
        checkPasswordLeakFailure: (state, action) => {
            state.passwordLeak.checking = false;
            state.passwordLeak.checked = false;
            state.passwordLeak.error = action.payload;
        },

        // =========================
        // 이메일 인증번호 발송
        // =========================
        emailSendRequest: (state)=>{
            state.emailVerification.sending = true;
            state.emailVerification.sent = false;
            state.emailVerification.verified = false;
            state.emailVerification.error = null;
        },
        emailSendSuccess: (state)=>{
            state.emailVerification.sending = false;
            state.emailVerification.sent = true;
            state.emailVerification.verified = false;
            state.emailVerification.error = null;
        },
        emailSendFailure: (state,action)=>{
            state.emailVerification.sending = false;
            state.emailVerification.sent = false;
            state.emailVerification.verified = false;
            state.emailVerification.error = action.payload;
        },

        // =========================
        // 이메일 인증번호 확인
        // =========================
        emailVerifyRequest: (state)=>{
            state.emailVerification.verifying = true;
            state.emailVerification.verified = false;
            state.emailVerification.error = null;
        },
        emailVerifySuccess: (state)=>{
            state.emailVerification.verifying = false;
            state.emailVerification.verified = true;
            state.emailVerification.error = null;
        },
        emailVerifyFailure: (state,action)=>{
            state.emailVerification.verifying = false;
            state.emailVerification.verified = false;
            state.emailVerification.error = action.payload;
        },

        // =========================
        // 아이디 중복검사
        // =========================
        checkLoginIdRequest: (state)=>{
            state.loading = true;
            state.error = null;
            state.duplicateCheck.loginId = null;
        },
        checkLoginIdSuccess: (state,action)=>{
            state.loading = false;
            state.error = null;
            state.duplicateCheck.loginId = action.payload;
        },
        checkLoginIdFailure: (state,action)=>{
            state.loading = false;
            state.error = action.payload;
            state.duplicateCheck.loginId = null;
        },

        // =========================
        // 이메일 중복검사
        // =========================
        checkEmailRequest: (state)=>{
            state.loading = true;
            state.error = null;
            state.duplicateCheck.email = null;
        },
        checkEmailSuccess: (state,action)=>{
            state.loading = false;
            state.error = null;
            state.duplicateCheck.email = action.payload;
        },
        checkEmailFailure: (state,action)=>{
            state.loading = false;
            state.error = action.payload;
            state.duplicateCheck.email = null;
        },

        // =========================
        // 닉네임 중복검사
        // =========================
        checkNicknameRequest: (state)=>{
            state.loading = true;
            state.error = null;
            state.duplicateCheck.nickname = null;
        },
        checkNicknameSuccess: (state,action)=>{
            state.loading = false;
            state.error = null;
            state.duplicateCheck.nickname = action.payload;
        },
        checkNicknameFailure: (state,action)=>{
            state.loading = false;
            state.error = action.payload;
            state.duplicateCheck.nickname = false;
            state.duplicateCheck.nickname = null;
        },

        // =========================
        // 전화번호 중복검사
        // =========================
        checkMobileRequest: (state)=>{
            state.loading = true;
            state.error = null;
            state.duplicateCheck.mobile = null;
        },
        checkMobileSuccess: (state,action)=>{
            state.loading = false;
            state.error = null;
            state.duplicateCheck.mobile = action.payload;
        },
        checkMobileFailure: (state,action)=>{
            state.loading = false;
            state.error = action.payload;
            state.duplicateCheck.mobile = null;
        },
        // =========================
        // 전체 회원 조회
        // =========================
        findMembersRequest: (state) => {
            state.membersLoading = true;
            state.membersError = null;
        },
        findMembersSuccess: (state, action) => {
            state.membersLoading = false;
            state.members = action.payload;
            state.membersError = null;
        },
        findMembersFailure: (state, action) => {
            state.membersLoading = false;
            state.membersError = action.payload;
        },


        // =================================================
        // 중복확인 상태 초기화
        // =================================================
        resetDuplicateCheck: (state, action) => {
            const field = action.payload;

            if (field in state.duplicateCheck) { state.duplicateCheck[field] = null;}
        },

        // =================================================
        // 이메일 인증 상태 초기화
        // =================================================
        resetEmailVerification: (state) => {
            state.emailVerification = {
                sending: false,
                verifying: false,
                sent: false,
                verified: false,
                error: null,
            };
        },

        // =================================================
        // 비밀번호 유출검사 상태 초기화
        // =================================================
        resetPasswordLeak: (state) => {
            state.passwordLeak = {
                checking: false,
                checked: false,
                leaked: false,
                count: 0,
                error: null,
            };
        },

        // =========================
        // 로그아웃
        // =========================
        logoutRequest: (state) => {
            state.loading = true;
        },
        logout: (state)=>{
            state.user = null;
            state.accessToken = null;
            state.refreshToken = null;
            state.loading = false;
            state.error = null;
            state.success = false;

            // 중복확인 상태 초기화
            state.duplicateCheck = {
                loginId: false,
                email: false,
                nickname: false,
                mobile: false,
            };
        },
    }
});

//3. action
export const {
    loginRequest,loginSuccess,loginFailure,
    signupRequest,signupSuccess,signupFailure,
    emailSendRequest,emailSendSuccess,emailSendFailure,
    emailVerifyRequest,emailVerifySuccess,emailVerifyFailure,
    checkLoginIdRequest,checkLoginIdSuccess,checkLoginIdFailure,
    checkEmailRequest,checkEmailSuccess,checkEmailFailure,
    checkNicknameRequest,checkNicknameSuccess,checkNicknameFailure,
    checkMobileRequest,checkMobileSuccess,checkMobileFailure,
    logout, logoutRequest, resetDuplicateCheck,resetEmailVerification,
    checkPasswordLeakRequest,checkPasswordLeakSuccess,checkPasswordLeakFailure,
    resetPasswordLeak,findMembersRequest,findMembersSuccess,findMembersFailure,
    getMyInfoRequest, getMyInfoSuccess, getMyInfoFailure,
} = userReducer.actions;

//4. export
export default userReducer.reducer;

