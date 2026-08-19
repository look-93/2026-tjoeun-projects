import {all, call, put, takeLatest} from 'redux-saga/effects';
import api from '../api/axios';
import {
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
} from '../reducers/userReducer';



// =========================
// 로그인 API
// =========================
function loginApi(loginData){
    return api.post("/api/members/login",loginData);
}

// =========================
// 내 정보 조회 API
// =========================
function getMyInfoApi() {
    return api.get("/api/members/me");
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

// =========================
// 비밀번호 유출 검사 API
// =========================
function checkPasswordLeakApi(password){
    return api.post("/api/members/check-password", {password: password});
}

// =========================
// 회원 전체 조회
// =========================
function findMembersApi() {
    return api.get("/api/members");
}

// =========================
// 로그아웃
// =========================
function logoutApi() {
    return api.post("/api/members/logout");
}

////////////////////////////////////////////////////

// =========================
// 로그인
// =========================
function* login(action){
    try{
        const response = yield call(loginApi, action.payload);

        console.log("===== 일반 로그인 응답 =====");
        console.log("status:", response.status);
        console.log("response.data:", response.data);
        console.log("accessToken:", response.data?.accessToken);
        console.log("refreshToken:", response.data?.refreshToken);

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
            message = err.response?.data?.message ||
                "아이디 또는 비밀번호가 올바르지 않습니다.";
        }

        // 회원 유형 오류
        if(err.response?.status === 403){
            message =
                err.response?.data?.message ||
                "회원유형이 맞지 않습니다.";
        }

        yield put(loginFailure(message));
    }
}

// =========================
// 내 정보 조회
// =========================
function* getMyInfo() {

    console.log("===== GET MY INFO SAGA START =====");

    try {

        console.log("===== GET MY INFO API CALL =====");

        const response = yield call(getMyInfoApi);

        console.log("===== GET MY INFO API RESPONSE =====");
        console.log("status:", response.status);
        console.log("data:", response.data);

        yield put(getMyInfoSuccess(response.data));

        console.log("===== GET MY INFO SUCCESS DISPATCH =====");

    } catch(err) {

        console.error("===== GET MY INFO SAGA ERROR =====");
        console.error(err);

        const message =
            err.response?.data?.message ||
            "회원정보를 불러오지 못했습니다.";

        yield put(getMyInfoFailure(message));
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
        yield put(signupFailure(err.response?.data?.message || err.message));
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
        yield put(emailSendFailure(err.response?.data?.message || err.message));
    }
}

// =========================
// 이메일 인증번호 확인
// =========================
function* emailVerify(action){
    try{
        const {email,code} = action.payload;

        const response = yield call(emailverifyApi,
                                    email,
                                    code);

        console.log("이메일 인증 성공:",response.data);

        yield put(emailVerifySuccess());
    }catch(err){       
        yield put(emailVerifyFailure(err.response?.data?.message || err.message));
    }
}

// =========================
// 아이디 중복검사
// =========================
function* checkLoginId(action){ 
    try{ 
        const response = yield call(checkLoginIdApi, action.payload); 

        console.log("아이디 존재 여부:", response.data);

        // 백엔드:
        // true  = 이미 존재
        // false = 존재하지 않음
        //
        // 프론트:
        // true = 사용 가능
        // false = 사용 불가
        const available = !response.data;

        console.log("아이디 사용 가능 여부:", available);

        yield put(checkLoginIdSuccess(available));

    }catch(err){ 
        console.error("아이디 중복검사 실패:",err); 
 
        yield put(
            checkLoginIdFailure(
                err.response?.data?.message || err.message
            )
        ); 
    } 
}

// =========================
// 비밀번호 유출 검사
// =========================
function* checkPasswordLeak(action){
    try{
        const response = yield call(checkPasswordLeakApi,action.payload);

        console.log("비밀번호 유출 검사:", response.data);

        yield put( checkPasswordLeakSuccess(response.data));
    }catch(err){
        console.error("비밀번호 유출 검사 실패:", err);
        yield put(checkPasswordLeakFailure(err.response?.data?.message || err.message));
    }
}

// =========================
// 이메일 중복검사 
// =========================
function* checkEmail(action){ 
    try{ 
        const response = yield call(checkEmailApi, action.payload); 

        const available = !response.data;

        console.log("이메일 사용 가능 여부:", available); 
 
        yield put(checkEmailSuccess(available));

    }catch(err){ 
        console.error("이메일 중복검사 실패:",err); 
 
        yield put(
            checkEmailFailure(
                err.response?.data?.message || err.message
            )
        ); 
    } 
}

// =========================
// 닉네임 중복검사 
// =========================
function* checkNickname(action){ 
    try{ 
        const response = yield call(checkNicknameApi, action.payload); 

        const available = !response.data;

        console.log("닉네임 사용 가능 여부:", available); 
 
        yield put(checkNicknameSuccess(available));

    }catch(err){ 
        console.error("닉네임 중복검사 실패:",err); 
 
        yield put(
            checkNicknameFailure(
                err.response?.data?.message || err.message
            )
        ); 
    } 
}

// =========================
// 전화번호 중복검사
// =========================
function* checkMobile(action){ 
    try{ 
        const response = yield call(checkMobileApi, action.payload); 

        const available = !response.data;

        console.log("전화번호 사용 가능 여부:", available); 
 
        yield put(checkMobileSuccess(available));

    }catch(err){ 
        console.error("전화번호 중복검사 실패:",err); 
 
        yield put(
            checkMobileFailure(
                err.response?.data?.message || err.message
            )
        ); 
    } 
}

// =========================
// 회원 전체 조회
// =========================
function* findMembers() {
    try {
        const response = yield call(findMembersApi);

        console.log("전체 회원 조회:", response.data);

        yield put(findMembersSuccess(response.data));
    } catch (err) {
        console.error("전체 회원 조회 실패:", err);
        yield put(findMembersFailure(err.response?.data?.message || err.message));
    }
}

// =========================
// 로그아웃
// =========================
function* logoutSaga() {
    try {
        yield call(logoutApi);
        console.log("로그아웃 성공");
    } catch (err) {
        console.error("로그아웃 API 실패:", err);
    } finally {
        if (typeof window !== "undefined") {
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
        }
        // Redux 로그인 상태 초기화
        yield put(logout());
    }
}

export default function* userSaga(){

    console.log("===== USER SAGA STARTED =====");

    yield all([
        takeLatest(loginRequest.type, login),
        takeLatest(signupRequest.type, signup),
        takeLatest(emailSendRequest.type, emailSend),
        takeLatest(emailVerifyRequest.type, emailVerify),
        takeLatest(checkLoginIdRequest.type, checkLoginId),
        takeLatest(checkEmailRequest.type, checkEmail),
        takeLatest(checkNicknameRequest.type, checkNickname),
        takeLatest(checkMobileRequest.type, checkMobile),
        takeLatest(checkPasswordLeakRequest.type, checkPasswordLeak),
        takeLatest(findMembersRequest.type, findMembers),
        takeLatest(logoutRequest.type, logoutSaga),
        takeLatest(getMyInfoRequest.type, getMyInfo),
    ]);
}