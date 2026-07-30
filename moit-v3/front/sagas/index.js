import {all, fork} from 'redux-saga/effects';

import authSaga from './authSaga';
///// 추가되는 saga ////////

export default function* rootSaga(){
    yield all([
        fork(authSaga),
        ///// 추가되는 saga ////////
    ])
} 