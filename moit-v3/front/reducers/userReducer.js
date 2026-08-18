import { createSlice } from "@reduxjs/toolkit";

//1. 초기화 상태(공용)
const initialState = {
    user: null,     //단건 조회된 사용자 정보
    accessToken: null,
    refreshToken: null,
    loading:false,  //로딩상태
    error: null,    //에러메시지
    success:false,   //성공여부

    // 이메일 인증
    emailVerification: {
        sending: false,
        verifying: false,
        sent: false,
        verified: false,
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
        // 이메일 인증번호 발송
        // =========================
        emailSendRequest: (state)=>{
            state.emailVerification.sending = true;
            state.emailVerification.sent = false;
            state.emailVerification.error = null;
        },
        emailSendSuccess: (state)=>{
            state.emailVerification.sending = false;
            state.emailVerification.sent = true;
            state.emailVerification.error = null;
        },
        emailSendFailure: (state,action)=>{
            state.emailVerification.sending = false;
            state.emailVerification.sent = false;
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
        },
        checkLoginIdSuccess: (state)=>{
            state.loading = false;
            state.error = null;
        },
        checkLoginIdFailure: (state,action)=>{
            state.loading = false;
            state.error = action.payload;
        },

        // =========================
        // 이메일 중복검사
        // =========================
        checkEmailRequest: (state)=>{
            state.loading = true;
            state.error = null;
        },
        checkEmailSuccess: (state)=>{
            state.loading = false;
            state.error = null;
        },
        checkEmailFailure: (state,action)=>{
            state.loading = false;
            state.error = action.payload;
        },

        // =========================
        // 닉네임 중복검사
        // =========================
        checkNicknameRequest: (state)=>{
            state.loading = true;
            state.error = null;
        },
        checkNicknameSuccess: (state)=>{
            state.loading = false;
            state.error = null;
        },
        checkNicknameFailure: (state,action)=>{
            state.loading = false;
            state.error = action.payload;
        },

        // =========================
        // 전화번호 중복검사
        // =========================
        checkMobileRequest: (state)=>{
            state.loading = true;
            state.error = null;
        },
        checkMobileSuccess: (state)=>{
            state.loading = false;
            state.error = null;
        },
        checkMobileFailure: (state,action)=>{
            state.loading = false;
            state.error = action.payload;
        },


        // =========================
        // 로그아웃
        // =========================
        logout: (state)=>{
            state.user = null;
            state.accessToken = null;
            state.refreshToken = null;
            state.loading = false;
            state.error = null;
            state.success = false;
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
    logout,
} = userReducer.actions;

//4. export
export default userReducer.reducer;

