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
    getMyPageRequest,getMyPageSuccess,getMyPageFailure,
    findIdRequest,findIdSuccess,findIdFailure,resetFindId,findIdEmailSendRequest,
    findPasswordRequest,findPasswordSuccess,findPasswordFailure,resetFindPassword,
    findPasswordEmailSendRequest, changePasswordRequest,changePasswordSuccess,
    changePasswordFailure,resetChangePassword, updateMyInfoRequest,
    updateMyInfoSuccess,updateMyInfoFailure, resetUpdateMyInfo,
    uploadProfileImageRequest,uploadProfileImageSuccess,uploadProfileImageFailure,
    resetProfileImage,
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
// 마이페이지 조회 API
// =========================
function getMyPageApi() {
    return api.get("/api/members/mypage");
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
// 회원 전체 조회 API
// =========================
function findMembersApi() {
    return api.get("/api/members");
}

// =========================
// 로그아웃 API
// =========================
function logoutApi() {
    return api.post("/api/members/logout");
}

// =========================
// 아이디 찾기 API
// =========================
function findIdApi(email) {
    return api.post("/api/members/find-id", {email: email,});
}
// =========================
// 아이디 찾기용 이메일 가입 여부 확인 API
// =========================
function findIdEmailCheckApi(email) {
    return api.get("/api/members/check-email", {params: {email: email }});
}

// =========================
// 비밀번호 변경 API
// =========================
function resetPasswordApi(data) {
    return api.post("/api/members/reset-password", data);
}

// =========================
// 로그인 회원 비밀번호 변경 API
// =========================
function changePasswordApi(data) {
    return api.put("/api/members/me/password", data);
}

// =========================
// 회원정보 수정 API
// =========================
function updateMyInfoApi(data) {
    return api.put("/api/members/me", data);
}

// =========================
// 프로필 이미지 업로드 API
// =========================
function uploadProfileImageApi(formData) {
    return api.put("/api/members/me/profile-image",formData);
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
// 마이페이지
// =========================
function* getMyPageSaga() {
    try {
        const response = yield call(getMyPageApi);

        yield put(getMyPageSuccess(response.data));
    } catch (error) {

        yield put( getMyPageFailure( error.response?.data?.message || "마이페이지 정보를 불러오지 못했습니다."));
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

// =========================
// 아이디 찾기
// =========================
function* findId(action) {
    try {
        const response = yield call(findIdApi,action.payload);

        console.log("===== 아이디 찾기 성공 =====");
        console.log("response:", response.data);

        yield put(findIdSuccess(response.data.loginId));

    } catch (err) {
        console.error("===== 아이디 찾기 실패 =====");
        console.error(err);

        yield put(findIdFailure(err.response?.data?.message ||"아이디를 찾을 수 없습니다."));
    }
}
// =========================
// 아이디 찾기용 이메일 가입 여부 확인
// =========================
function* findIdEmailSend(action) {
    try {
        const email = action.payload;

        // 가입 이메일 확인
        const checkResponse = yield call(checkEmailApi,email);

        console.log("===== 아이디 찾기 이메일 확인 =====");
        console.log("email:",email);
        console.log("가입 여부:",checkResponse.data);

        // 가입된 이메일이 아니면 종료
        if (!checkResponse.data) {
            yield put( emailSendFailure("해당 이메일로 가입된 회원이 없습니다."));
            return;
        }

        // 가입된 이메일이면 인증번호 발송
        yield call(emailSendApi,email);

        yield put( emailSendSuccess());
    } catch (err) {
        console.error("아이디 찾기 이메일 발송 실패:", err);

        yield put(
            emailSendFailure(err.response?.data?.message ||"인증번호 발송에 실패했습니다."));
    }
}

// =========================
// 비밀번호 변경
// =========================
function* resetPassword(action) {
    try {
        const response = yield call(resetPasswordApi, action.payload);

        console.log("===== 비밀번호 변경 성공 =====");
        console.log("response:", response.data);

        yield put(findPasswordSuccess());

    } catch (err) {
        console.error("===== 비밀번호 변경 실패 =====");
        console.error(err);

        yield put( findPasswordFailure(err.response?.data?.message ||"비밀번호 변경에 실패했습니다."));
    }
}
// =========================
// 비밀번호 찾기용 이메일 발송
// =========================
function* findPasswordEmailSend(action) {

    try {

        const email = action.payload;

        // =========================
        // 가입 이메일 확인
        // =========================
        const checkResponse = yield call(checkEmailApi,email);

        console.log("===== 비밀번호 찾기 이메일 확인 =====");
        console.log("email:", email);
        console.log("가입 여부:", checkResponse.data);

        // =========================
        // 가입되지 않은 이메일
        // =========================
        if (!checkResponse.data) {

            yield put(emailSendFailure("해당 이메일로 가입된 회원이 없습니다."));
            return;
        }

        // =========================
        // 가입된 이메일이면 인증번호 발송
        // =========================
        yield call(emailSendApi, email);

        console.log("비밀번호 찾기 인증번호 발송 성공");

        yield put(emailSendSuccess());

    } catch (err) {

        console.error("비밀번호 찾기 이메일 발송 실패:",err);

        yield put( emailSendFailure(err.response?.data?.message ||"인증번호 발송에 실패했습니다."));
    }
}

// =========================
// 로그인 회원 비밀번호 변경
// =========================
function* changePassword(action) {

    try {

        console.log("===== 회원 비밀번호 변경 START =====");
        console.log("request:", action.payload);

        const response = yield call(changePasswordApi,action.payload);

        console.log("===== 회원 비밀번호 변경 SUCCESS =====");
        console.log("response:", response.data);

        yield put(changePasswordSuccess());

    } catch (err) {

        console.error("===== 회원 비밀번호 변경 FAILURE =====");
        console.error(err);

        yield put(changePasswordFailure(err.response?.data?.message ||"비밀번호 변경에 실패했습니다."));
    }
}
// =========================
// 회원정보 수정
// =========================
function* updateMyInfo(action) {

    try {

        console.log("===== 회원정보 수정 START =====");
        console.log("request:", action.payload);

        const response = yield call(updateMyInfoApi,action.payload);

        console.log("===== 회원정보 수정 SUCCESS =====");
        console.log("response:", response.data);

        yield put(updateMyInfoSuccess(response.data));

    } catch (err) {

        console.error("===== 회원정보 수정 FAILURE =====");
        console.error(err);

        yield put(updateMyInfoFailure(err.response?.data?.message ||"회원정보 수정에 실패했습니다."));
    }
}

// =========================
// 프로필 이미지 업로드
// =========================
function* uploadProfileImage(action) {

    try {
        console.log("===== 프로필 이미지 업로드 START =====");
        console.log("file:", action.payload);

        const formData = new FormData();

        formData.append("profileImage", action.payload);

        const response = yield call(uploadProfileImageApi,formData);

        console.log("===== 프로필 이미지 업로드 SUCCESS =====");
        console.log("response:", response.data);

        yield put(uploadProfileImageSuccess(response.data) );

    } catch (err) {
        console.error("===== 프로필 이미지 업로드 FAILURE =====");
        console.error(err);

        yield put(uploadProfileImageFailure(err.response?.data?.message ||"프로필 이미지 업로드에 실패했습니다."));
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
        takeLatest(getMyPageRequest.type,getMyPageSaga),
        takeLatest(findIdRequest.type, findId),
        takeLatest(findIdEmailSendRequest.type,findIdEmailSend),
        takeLatest(findPasswordRequest.type,resetPassword),
        takeLatest(findPasswordEmailSendRequest.type,findPasswordEmailSend),
        takeLatest(changePasswordRequest.type, changePassword),
        takeLatest(updateMyInfoRequest.type,updateMyInfo),
        takeLatest(uploadProfileImageRequest.type,uploadProfileImage),
    ]);
}