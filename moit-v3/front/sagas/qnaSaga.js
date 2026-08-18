import {all, call, put, takeLatest} from 'redux-saga/effects';
import api from '../api/axios';

import {qnaCreateRequest, qnaCreateSuccess, qnaCreateFailure,
        qnaListRequest, qnaListSuccess, qnaListFailure,
        qnaMeetupListRequest, qnaMeetupListSuccess, qnaMeetupListFailure,
        qnaDetailRequest, qnaDetailSuccess, qnaDetailFailure,
} from '../reducers/qnaReducer';

const QNA_API_BASE = '/api/questions'

// --- QnA 등록 POST : /api/questions ---
export const qnaCreateAPI = (payload) => api.post(QNA_API_BASE, payload);
export function* qnaCreate(action){
    try {
        const result = yield call(qnaCreateAPI, action.payload);
        // Controller에서 201 Created만 반환하므로
        // result.data는 따로 사용하지 않음
        yield put(qnaCreateSuccess());
    }catch(err){
        yield put( qnaCreateFailure(err.response?.data?.message || err.message));
    }
}

// --- QnA 목록 조회 GET : /api/questions/myQuestion ---
export const qnaListAPI = ({page = 1, type, keyword,}) => api.get(`${QNA_API_BASE}/myQuestion`, {
        params: {page, type, keyword,}});
export function* qnaList(action){
    try {
        const result = yield call(qnaListAPI, action.payload || { page: 1 });
        yield put(qnaListSuccess(result.data));
    }catch(err){
        yield put(qnaListFailure(err.response?.data?.message || err.message));
    }
}

// 특정 모임 Q&A 목록 조회
export const qnaMeetupListAPI = (meetupId) => api.get(`${QNA_API_BASE}/meetup/${meetupId}`);
export function* qnaMeetupList(action) {
    try {
        const result = yield call(qnaMeetupListAPI, action.payload);
        yield put(qnaMeetupListSuccess(result.data));
    } catch (err) {
        yield put(qnaMeetupListFailure(err.response?.data?.message || err.message));
    }
}

// --- QnA 상세 조회 GET : /api/questions/{questionId} ---
export const qnaDetailAPI = (questionId) => api.get(`${QNA_API_BASE}/${questionId}`);
export function* qnaDetail(action){
    try {
        const result = yield call(qnaDetailAPI, action.payload);
        yield put(qnaDetailSuccess(result.data));
    }catch(err) {
        yield put(qnaDetailFailure(err.response?.data?.message || err.message));
    }
}

// --- QnA Saga ---
function* watchQnaCreate() {     yield takeLatest( qnaCreateRequest.type,     qnaCreate ); }
function* watchQnaList()   {     yield takeLatest( qnaListRequest.type,       qnaList ); }
function* watchQnaDetail() {     yield takeLatest( qnaDetailRequest.type,     qnaDetail ); }
function* watchQnaMeetupList() { yield takeLatest( qnaMeetupListRequest.type, qnaMeetupList ); }

export default function* qnaSaga() {
    yield all([
        call(watchQnaCreate),
        call(watchQnaList),
        call(watchQnaDetail),
        call(watchQnaMeetupList),
    ]);
}