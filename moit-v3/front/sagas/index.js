import {all, fork} from 'redux-saga/effects';

import userSaga from './userSaga';
import qnaSaga  from './qnaSaga';

export default function* rootSaga(){
    yield all([
        fork(userSaga),
        fork(qnaSaga),
    ])
} 