import {combineReducers} from "@reduxjs/toolkit"; // 여러 리듀서를 하나로 합쳐주는 함수
import authReducer from './authReducer';
import qnaReducer  from './qnaReducer';

const rootReducer = combineReducers({
    auth: authReducer, //state.auth
    qna:  qnaReducer,  //state.qna
})

export default rootReducer;