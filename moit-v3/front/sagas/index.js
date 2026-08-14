import {all, fork} from 'redux-saga/effects';

import authSaga from './authSaga';
///// 추가되는 saga ////////
import advertiseSaga from './advertiseSaga';

export default function* rootSaga(){
    yield all([
        fork(authSaga),
        ///// 추가되는 saga ////////
        fork(advertiseSaga),
    ])
} 