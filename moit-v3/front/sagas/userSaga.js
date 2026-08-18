import {all, call, put, takeLatest} from 'redux-saga/effects';
import api from 'axios';
import {
    loginRequest,loginSuccess,loginFailure,
    signupRequest,signupSuccess,signupFailure,
    emailSendRequest,emailSendSuccess,emailSendFailure,
    emailVerifyRequest,emailVerifySuccess,emailVerifyFailure,
    checkLoginIdRequest,checkLoginIdSuccess,checkLoginIdFailure,
    checkEmailRequest,checkEmailSuccess,checkEmailFailure,
    checkNicknameRequest,checkNicknameSuccess,checkNicknameFailure,
    checkMobileRequest,checkMobileSuccess,checkMobileFailure,
    logout,
} from '../reducers/userReducer';

// =========================
// 로그인 API
// =========================
function loginApi(loginData){
    return api.post("/api/members/login",loginData);
}

// =========================
// 회원가입 API
// =========================
function signupApi(signupData){
    return api.post("/api/members/signup",signupData);
}

// =========================
// 이메일 인증번호 발송 API
// =========================
function emailSendApi(email){
    return api.post("/api/members/email/send",{email: email});
}

// =========================
// 이메일 인증번호 확인 API
// =========================
function emailverifyApi(email,code){
    return api.post("/api/members/email/verify",{email: email, code: code});
}

// =========================
// 아이디 중복검사 API
// =========================
function checkLoginIdApi(loginId){
    return api.get("/api/members/check-loginId",{params:{loginId: loginId}});
}

// =========================
// 이메일 중복검사 API
// =========================
function checkEmailApi(email){
    return api.get("/api/members/check-email",{params: {email: email}});
}

// =========================
// 닉네임 중복검사 API
// =========================
function checkNicknameApi(nickname){
    return api.get("/api/members/check-nickname",{params: {nickname: nickname}});
}

// =========================
// 전화번호 중복검사 API
// =========================
function checkMobileApi(mobile){
    return api.get("/api/members/check-mobile",{params: {mobile: mobile}});
}


////////////////////////////////////////////////////

// =========================
// 로그인
// =========================
function* login(action){
    try{
        const response = yield call(loginApi, action.payload);

        console.log("로그인 성공:",response.data);

        // Access Token 저장
        if (typeof window !== "undefined") {

            localStorage.setItem("accessToken",response.data.accessToken);
            localStorage.setItem("refreshToken",response.data.refreshToken);
        }

        yield put(loginSuccess(response.data));

    }catch(err){
        console.error("로그인 실패:",err);

        let message = "로그인에 실패했습니다.";

        if(err.response?.status == 401){
            message = "아이디 또는 비밀번호가 올바르지 않습니다.";
        }


        yield put(loginFailure(message));
    }
}

// =========================
// 회원가입
// =========================
function* signup(action){
    try{
        const response = yield call(signupApi,action.payload);

        console.log("회원가입 성공:", response.data);

        yield put(signupSuccess(response.data));
    }catch(err){
        console.error("회원가입 실패:",err);

        let message = "회원가입에 실패했습니다.";

        if(err.response?.status == 400){
            message = err.response.data?.message ||
                      err.response.data || "입력한 회원가입 정보를 확인해주세요.";
        }

        if (err.response?.status === 400 &&
            typeof err.response.data === "string" &&
            err.response.data.includes("이메일 인증")) {

            message = "이메일 인증을 완료해주세요.";
        }

        yield put(signupFailure(message));
    }
}

// =========================
// 이메일 인증번호 발송
// =========================
function* emailSend(action){
    try{
        const response = yield call(emailSendApi, action.payload);

        console.log("이메일 인증번호 발송성공:",response.data);

        yield put(emailSendSuccess());
    }catch(err){
        console.error("이메일 인증번호 발송실패:",err);

        let message = "인증번호 발송 실패했습니다.";

        if(err.response?.status === 400){
            message = err.response.data?.message ||
                      err.response.data || "올바른 이메일 주소를 입력해주세요.";
        }

        yield put(emailSendFailure(message));
    }
}

// =========================
// 이메일 인증번호 확인
// =========================
function* emailVerify(action){
    try{
        const {email,code} = action.payload;

        const response = yield call(emailverifyApi,action.payload.email,
            action.payload.code);

        console.log("이메일 인증 성공:",response.data);

        yield put(emailVerifySuccess());
    }catch(err){
        console.error("이메일 인증실패:",err);

        let message = "이메일 인증에 실패했습니다.";

        if(err.response?.status === 400){
            message = err.response.data?.message ||
                      err.response.data || "인증번호가 일치하지 않거나 만료되었습니다.";
        }

        yield put(emailVerifyFailure(message));
    }
}

// =========================
// 아이디 중복검사
// =========================
function* checkLoginId(action){
    try{
        const response = yield call(checkLoginIdApi, action.payload);

        console.log("아이디 중복검사:", response.data);

        yield put(checkLoginIdSuccess(response.data));
    }catch(err){
        console.error("아이디 중복검사 실패:",err);

        yield put(checkLoginIdFailure(err.response?.data || "아이디 중복검사에 실패했습니다."));
    }
}

// =========================
// 이메일 중복검사 
// =========================
function* checkEmail(action){
    try{
        const response = yield call(checkEmailApi, action.payload);

        console.log("이메일 중복검사:", response.data);

        yield put(checkEmailSuccess(response.data));
    }catch(err){
        console.error("이메일 중복검사 실패:",err);

        yield put(checkEmailFailure(err.response.data?.message ||
                                    err.response.data || "이메일 중복검사에 실패했습니다."));
    }
}

// =========================
// 닉네임 중복검사 
// =========================
function* checkNickname(action){
    try{
        const response = yield call(checkNicknameApi, action.payload);

        console.log("닉네임 중복검사:", response.data);

        yield put(checkNicknameSuccess(response.data));
    }catch(err){
        console.error("닉네임 중복검사 실패:",err);

        yield put(checkNicknameFailure(err.response.data?.message ||
                                       err.response.data || "닉네임 중복검사에 실패했습니다."));
    }
}

// =========================
// 전화번호 중복검사
// =========================
function* checkMobile(action){
    try{
        const response = yield call(checkMobileApi, action.payload);

        console.log("전화번호 중복검사:", response.data);

        yield put(checkMobileSuccess(response.data));
    }catch(err){
        console.error("전화번호 중복검사 실패:",err);

        yield put(checkMobileFailure(err.response.data?.message ||
                                     err.response.data || "전화번호 중복검사에 실패했습니다."));
    }
}


export default function* userSaga(){
    yield all([
        takeLatest(loginRequest.type, login),
        takeLatest(signupRequest.type, signup),
        takeLatest(emailSendRequest.type, emailSend),
        takeLatest(emailVerifyRequest.type, emailVerify),
        takeLatest(checkLoginIdRequest.type, checkLoginId),
        takeLatest(checkEmailRequest.type, checkEmail),
        takeLatest(checkNicknameRequest.type, checkNickname),
        takeLatest(checkMobileRequest.type, checkMobile),
    ]);
}