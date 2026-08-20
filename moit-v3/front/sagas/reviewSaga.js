import { all, call, fork, put, takeLatest } from 'redux-saga/effects';
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

export const updateReviewApi = ({ reviewId, requestDto, ...rest }) => {
    const body = requestDto || rest;
    return api.put(`${REVIEW_API_BASE}/${reviewId}`, body);
};

export const deleteReviewApi = (reviewId) => api.delete(`${REVIEW_API_BASE}/${reviewId}`);

// ★ 모임별 리뷰 API에 keyword 파라미터 추가
export const fetchReviewsByMeetupApi = ({ meetupId, keyword = '', page = 0, size = 10, sort = 'id,desc' }) =>
    api.get(`${REVIEW_API_BASE}/meetup/${meetupId}`, { params: { keyword, page, size, sort } });

export const fetchMyReviewsApi = (params = { page: 0, size: 10, sort: 'id,desc' }) =>
    api.get(`${REVIEW_API_BASE}/my`, { params });

export const toggleReviewLikeApi = (reviewId) => api.post(`${REVIEW_API_BASE}/${reviewId}/like`);
export const analyzeReviewsApi = (meetupId) => api.post(`${REVIEW_API_BASE}/meetup/${meetupId}/analysis`);

export const fetchAdminReviewListApi = (params = { keyword: '', page: 0, size: 10, sort: 'id,desc' }) =>
    api.get(ADMIN_REVIEW_API_BASE, { params });

export const changeReviewVisibilityApi = (reviewId) => api.patch(`${ADMIN_REVIEW_API_BASE}/${reviewId}/visibility`);
export const adminDeleteReviewApi = (reviewId) => api.delete(`${ADMIN_REVIEW_API_BASE}/${reviewId}`);


// ==========================================
// 2. Saga 처리 함수 
// ==========================================

export function* createReview(action) {
    try {
        yield call(createReviewApi, action.payload);
        yield put(createReviewSuccess());
    } catch (err) {
        // ★ 백엔드가 보낸 응답이 문자열 형태일 경우 처리하도록 수정
        const errorMsg = typeof err.response?.data === 'string'
            ? err.response.data
            : err.response?.data?.message || err.message;

        yield put(createReviewFailure(errorMsg));
    }
}

export function* fetchReviewDetail(action) {
    try {
        const result = yield call(fetchReviewDetailApi, action.payload);
        yield put(getReviewDetailSuccess(result.data));
    } catch (err) {
        const errorMsg = typeof err.response?.data === 'string'
            ? err.response.data
            : err.response?.data?.message || err.message;

        yield put(getReviewDetailFailure(errorMsg));
    }
}

export function* updateReview(action) {
    try {
        const payload = action.payload || {};
        const reviewId = payload.reviewId || payload.id;

        if (!reviewId) {
            throw new Error("수정할 리뷰 ID(reviewId)가 존재하지 않습니다.");
        }

        const requestDto = payload.requestDto || {
            meetupId: payload.meetupId,
            content: payload.content,
            rating: payload.rating,
            isPublic: payload.isPublic,
            imageIds: payload.imageIds,
        };

        yield call(updateReviewApi, { reviewId, requestDto });
        yield put(updateReviewSuccess(action.payload));
    } catch (err) {
        // ★ 백엔드가 보낸 응답이 문자열 형태일 경우 처리하도록 수정
        const errorMsg = typeof err.response?.data === 'string'
            ? err.response.data
            : err.response?.data?.message || err.message;

        yield put(updateReviewFailure(errorMsg));
    }
}

export function* deleteReview(action) {
    try {
        yield call(deleteReviewApi, action.payload);
        yield put(deleteReviewSuccess(action.payload));
    } catch (err) {
        const errorMsg = typeof err.response?.data === 'string'
            ? err.response.data
            : err.response?.data?.message || err.message;

        yield put(deleteReviewFailure(errorMsg));
    }
}

// ★ [통합] meetupId 유무에 따라 모임 상세 API 혹은 마이페이지 API로 자동 분기
export function* fetchReviewList(action) {
    try {
        const { meetupId, ...params } = action.payload || {};
        let result;

        if (meetupId) {
            // 모임 상세 페이지용 (keyword, 정렬, 페이징 포함)
            result = yield call(fetchReviewsByMeetupApi, action.payload);
        } else {
            // 마이페이지용 (keyword, 정렬, 페이징 포함)
            result = yield call(fetchMyReviewsApi, params);
        }
        
        yield put(getReviewListSuccess({
            reviews: result.data.content || result.data.reviews || result.data,
            totalCount: result.data.totalElements || 0,
            totalPage: result.data.totalPages || 0,
        }));
    } catch (err) {
        const errorMsg = typeof err.response?.data === 'string'
            ? err.response.data
            : err.response?.data?.message || err.message;

        yield put(getReviewListFailure(errorMsg));
    }
}

// 리뷰 좋아요 토글 (중복 좋아요 400 에러 핸들링 추가)
export function* toggleReviewLike(action) {
    try {
        const reviewId = typeof action.payload === 'object' && action.payload !== null
            ? action.payload.reviewId || action.payload.id 
            : action.payload;

        if (!reviewId) {
            console.error("❌ reviewId가 존재하지 않습니다!");
            return;
        }

        yield call(toggleReviewLikeApi, reviewId);
        yield put(toggleReviewLikeSuccess(reviewId));
    } catch (err) {
        const errorMsg = typeof err.response?.data === 'string'
            ? err.response.data
            : err.response?.data?.message || err.message;
            
        console.warn("⚠️ [좋아요 제한]:", errorMsg);
        yield put(toggleReviewLikeFailure(errorMsg));
    }
}

export function* analyzeReviews(action) {
    try {
        const result = yield call(analyzeReviewsApi, action.payload);
        yield put(analyzeReviewsSuccess(result.data));
    } catch (err) {
        const errorMsg = typeof err.response?.data === 'string'
            ? err.response.data
            : err.response?.data?.message || err.message;

        yield put(analyzeReviewsFailure(errorMsg));
    }
}

export function* fetchAdminReviewList(action) {
    try {
        const result = yield call(fetchAdminReviewListApi, action.payload);
        yield put(getReviewListSuccess(result.data));
    } catch (err) {
        const errorMsg = typeof err.response?.data === 'string'
            ? err.response.data
            : err.response?.data?.message || err.message;

        yield put(getReviewListFailure(errorMsg));
    }
}

export function* adminDeleteReview(action) {
    try {
        yield call(adminDeleteReviewApi, action.payload);
        yield put(deleteReviewSuccess(action.payload));
    } catch (err) {
        const errorMsg = typeof err.response?.data === 'string'
            ? err.response.data
            : err.response?.data?.message || err.message;

        yield put(deleteReviewFailure(errorMsg));
    }
}


// ==========================================
// 3. Watcher 함수
// ==========================================
function* watchCreateReview() { yield takeLatest(createReviewRequest, createReview); }
function* watchFetchReviewDetail() { yield takeLatest(getReviewDetailRequest, fetchReviewDetail); }
function* watchUpdateReview() { yield takeLatest(updateReviewRequest, updateReview); }
function* watchDeleteReview() { yield takeLatest(deleteReviewRequest, deleteReview); }
function* watchFetchReviewList() { yield takeLatest(getReviewListRequest, fetchReviewList); }
function* watchAnalyzeReviews() { yield takeLatest(analyzeReviewsRequest, analyzeReviews); }
function* watchToggleReviewLike() { yield takeLatest(toggleReviewLikeRequest, toggleReviewLike); }

export default function* reviewSaga() {
    yield all([
        fork(watchCreateReview),
        fork(watchFetchReviewDetail),
        fork(watchUpdateReview),
        fork(watchDeleteReview),
        fork(watchFetchReviewList),
        fork(watchAnalyzeReviews),
        fork(watchToggleReviewLike),
    ]);
}