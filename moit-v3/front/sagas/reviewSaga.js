import { all, call, put, takeLatest, takeEvery } from 'redux-saga/effects';
import api from '../api/axios';

import {
    createReviewRequest, createReviewSuccess, createReviewFailure,
    getReviewDetailRequest, getReviewDetailSuccess, getReviewDetailFailure,
    updateReviewRequest, updateReviewSuccess, updateReviewFailure,
    deleteReviewRequest, deleteReviewSuccess, deleteReviewFailure,
    getReviewListRequest, getReviewListSuccess, getReviewListFailure,
    toggleReviewLikeRequest, toggleReviewLikeSuccess, toggleReviewLikeFailure,
    analyzeReviewsRequest, analyzeReviewsSuccess, analyzeReviewsFailure,
    changeVisibilitySuccess
} from '../reducers/reviewReducer';

const REVIEW_API_BASE = '/api/reviews';
const ADMIN_REVIEW_API_BASE = '/api/admin/reviews';

// ==========================================
// 1. API 통신 함수 
// ==========================================
export const createReviewApi = (requestDto) => api.post(REVIEW_API_BASE, requestDto);
export const fetchReviewDetailApi = (reviewId) => api.get(`${REVIEW_API_BASE}/${reviewId}`);
export const updateReviewApi = ({ reviewId, requestDto }) => api.put(`${REVIEW_API_BASE}/${reviewId}`, requestDto);
export const deleteReviewApi = (reviewId) => api.delete(`${REVIEW_API_BASE}/${reviewId}`);

export const fetchReviewsByMeetupApi = ({ meetupId, page = 0, size = 10, sort = 'id,desc' }) =>
    api.get(`${REVIEW_API_BASE}/meetup/${meetupId}`, { params: { page, size, sort } });

export const fetchMyReviewsApi = (params = { page: 0, size: 10, sort: 'id,desc' }) =>
    api.get(`${REVIEW_API_BASE}/my`, { params });

export const toggleReviewLikeApi = (reviewId) => api.post(`${REVIEW_API_BASE}/${reviewId}/like`);
export const analyzeReviewsApi = (meetupId) => api.post(`${REVIEW_API_BASE}/meetup/${meetupId}/analysis`);

// 관리자 API
export const fetchAdminReviewListApi = (params = { keyword: '', page: 0, size: 10, sort: 'id,desc' }) =>
    api.get(ADMIN_REVIEW_API_BASE, { params });

export const changeReviewVisibilityApi = (reviewId) => api.patch(`${ADMIN_REVIEW_API_BASE}/${reviewId}/visibility`);
export const adminDeleteReviewApi = (reviewId) => api.delete(`${ADMIN_REVIEW_API_BASE}/${reviewId}`);




// 리뷰 작성
export function* createReview(action) {
    try {
        yield call(createReviewApi, action.payload);
        yield put(createReviewSuccess());
    } catch (err) {
        yield put(createReviewFailure(err.response?.data?.message || err.message));
    }
}

// 리뷰 상세 조회
export function* fetchReviewDetail(action) {
    try {
        const result = yield call(fetchReviewDetailApi, action.payload);
        yield put(getReviewDetailSuccess(result.data));
    } catch (err) {
        yield put(getReviewDetailFailure(err.response?.data?.message || err.message));
    }
}

// 리뷰 수정
export function* updateReview(action) {
    try {
        yield call(updateReviewApi, action.payload);
        yield put(updateReviewSuccess(action.payload));
    } catch (err) {
        yield put(updateReviewFailure(err.response?.data?.message || err.message));
    }
}

// 리뷰 삭제 (사용자 / 관리자 공통)
export function* deleteReview(action) {
    try {
        yield call(deleteReviewApi, action.payload);
        yield put(deleteReviewSuccess(action.payload));
    } catch (err) {
        yield put(deleteReviewFailure(err.response?.data?.message || err.message));
    }
}

// 특정 모임 리뷰 목록 조회
export function* fetchReviewsByMeetup(action) {
    try {
        const result = yield call(fetchReviewsByMeetupApi, action.payload);
        yield put(getReviewListSuccess(result.data));
    } catch (err) {
        yield put(getReviewListFailure(err.response?.data?.message || err.message));
    }
}

// 내가 작성한 리뷰 목록 조회
export function* fetchMyReviews(action) {
    try {
        const result = yield call(fetchMyReviewsApi, action.payload);
        yield put(getReviewListSuccess(result.data));
    } catch (err) {
        yield put(getReviewListFailure(err.response?.data?.message || err.message));
    }
}

// 리뷰 좋아요 토글
export function* toggleReviewLike(action) {
    try {
        yield call(toggleReviewLikeApi, action.payload);
        yield put(toggleReviewLikeSuccess(action.payload));
    } catch (err) {
        yield put(toggleReviewLikeFailure(err.response?.data?.message || err.message));
    }
}

// AI 리뷰 분석
export function* analyzeReviews(action) {
    try {
        const result = yield call(analyzeReviewsApi, action.payload);
        yield put(analyzeReviewsSuccess(result.data));
    } catch (err) {
        yield put(analyzeReviewsFailure(err.response?.data?.message || err.message));
    }
}

// [관리자] 전체 리뷰 목록 조회
export function* fetchAdminReviewList(action) {
    try {
        const result = yield call(fetchAdminReviewListApi, action.payload);
        yield put(getReviewListSuccess(result.data));
    } catch (err) {
        yield put(getReviewListFailure(err.response?.data?.message || err.message));
    }
}

// [관리자] 리뷰 강제 삭제
export function* adminDeleteReview(action) {
    try {
        yield call(adminDeleteReviewApi, action.payload);
        yield put(deleteReviewSuccess(action.payload));
    } catch (err) {
        yield put(deleteReviewFailure(err.response?.data?.message || err.message));
    }
}



function* watchCreateReview() { yield takeLatest(createReviewRequest.type, createReview); }
function* watchFetchReviewDetail() { yield takeLatest(getReviewDetailRequest.type, fetchReviewDetail); }
function* watchUpdateReview() { yield takeLatest(updateReviewRequest.type, updateReview); }
function* watchDeleteReview() { yield takeLatest(deleteReviewRequest.type, deleteReview); }
function* watchFetchReviewList() { yield takeLatest(getReviewListRequest.type, fetchReviewsByMeetup); }
function* watchAnalyzeReviews() { yield takeLatest(analyzeReviewsRequest.type, analyzeReviews); }
function* watchToggleReviewLike() { yield takeEvery(toggleReviewLikeRequest.type, toggleReviewLike); }



export default function* reviewSaga() {
    yield all([
        call(watchCreateReview),
        call(watchFetchReviewDetail),
        call(watchUpdateReview),
        call(watchDeleteReview),
        call(watchFetchReviewList),
        call(watchAnalyzeReviews),
        call(watchToggleReviewLike),
    ]);
}