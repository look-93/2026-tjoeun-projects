import {combineReducers} from "@reduxjs/toolkit"; // 여러 리듀서를 하나로 합쳐주는 함수
import authReducer from './authReducer';
import reportReducer from './reportReducer';

const rootReducer = combineReducers({
    auth: authReducer, //state.auth
    report: reportReducer,  // state.report
})

export default rootReducer;