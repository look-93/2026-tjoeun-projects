import {combineReducers} from "@reduxjs/toolkit"; // 여러 리듀서를 하나로 합쳐주는 함수
import authReducer from './authReducer';

const rootReducer = combineReducers({
    auth: authReducer, //state.auth
})

export default rootReducer;