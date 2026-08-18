import {all, call, put, takeLatest} from 'redux-saga/effects';
import axios from '../api/axios';

import {qnaCreateRequest, qnaCreateSuccess, qnaCreateFailure,
    qnaListRequest, qnaListSuccess, qnaListFailure,
    qnaDetailRequest, qnaDetailSuccess, qnaDetailFailure,
    qnaReset,
} from '../reducers/qnaReducer';

export default function* qnaSaga(){
    yield all();
}