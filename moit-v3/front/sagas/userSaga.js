import {all, call, put, takeLatest} from 'redux-saga/effects';
import axios from 'axios';
import {
    loginRequest,loginSuccess,loginFailure,
    signupRequest,signupSuccess,signupFailure,
    emailSendRequest,emailSendSuccess,emailSendFailure,
    emailVerifyRequest,emailVerifySuccess,emailVerifyFailure,
    logout,
} from '../reducers/userReducer';

const API_URL = "http://localhost:8080";

// =========================
// 로그인 API
// =========================
function loginApi(loginData){
    return axios.post(`${API_URL}/api/members/login`,loginData);
}

// =========================
// 회원가입 API
// =========================
function signupApi(signupData){
    return axios.post(`${API_URL}/api/members/signup`,signupData);
}

// =========================
// 로그인
// =========================
function* login(action){
    try{
        const response = yield call(loginApi, action.payload);

        console.log("로그인 성공:",response.data);

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
        console.log("회원가입 실패:",err);

        let message = "회원가입에 실패했습니다.";

        if(err.response?.status == 400){
            message = "입력한 회원가입 정보를 확인해주세요.";
        }

        yield put(signupFailure(message));
    }
}


export default function* userSaga(){
    yield all([
        takeLatest(loginRequest.type, login),
        takeLatest(signupRequest.type, signup),
    ]);
}