import {combineReducers} from "@reduxjs/toolkit"; // 여러 리듀서를 하나로 합쳐주는 함수
import authReducer from './authReducer';
import advertiseReducer from './advertiseReducer';

const rootReducer = combineReducers({
    auth: authReducer, //state.auth

    // 광고 상태
    advertise: advertiseReducer,
})

export default rootReducer;