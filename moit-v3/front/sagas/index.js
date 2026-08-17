import {all, fork} from 'redux-saga/effects';

import userSaga from './userSaga';
///// 추가되는 saga ////////
import advertiseSaga from './advertiseSaga';

export default function* rootSaga(){
    yield all([
        fork(userSaga),
        ///// 추가되는 saga ////////
        fork(advertiseSaga),
    ])
} 