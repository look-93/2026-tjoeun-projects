import { combineReducers } from '@reduxjs/toolkit'; // 여러 리듀서를 하나로 합쳐주는 함수
import userReducer from './userReducer';
import meetupReducer from './meetupReducer';

const rootReducer = combineReducers({
    user: userReducer, //state.user
    meetup: meetupReducer,
});

export default rootReducer;
