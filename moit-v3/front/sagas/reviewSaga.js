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
    changeVisibilitySuccess,
    // ★ [추가] 댓글 관련 액션 임포트
    getCommentsRequest, getCommentsSuccess, getCommentsFailure,
    createCommentRequest, createCommentSuccess, createCommentFailure,
    // updateCommentRequest, updateCommentSuccess, updateCommentFailure,
    deleteCommentRequest, deleteCommentSuccess, deleteCommentFailure
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

// 모임별 리뷰 API에 keyword 파라미터 추가
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

// ★ [추가] 댓글 API 통신 함수
export const fetchCommentsApi = (reviewId) => api.get(`${REVIEW_API_BASE}/${reviewId}/comments`);
export const createCommentApi = ({ reviewId, content, parentCommentId }) => 
    api.post(`${REVIEW_API_BASE}/${reviewId}/comments`, { content, parentCommentId });
// export const updateCommentApi = ({ commentId, content }) => 
//     api.put(`${REVIEW_API_BASE}/comments/${commentId}`, { content });
export const deleteCommentApi = (commentId) => 
    api.delete(`${REVIEW_API_BASE}/comments/${commentId}`);


// ==========================================
// 2. Saga 처리 함수 
// ==========================================

export function* createReview(action) {
    try {
        yield call(createReviewApi, action.payload);
        yield put(createReviewSuccess());
    } catch (err) {
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

export function* fetchReviewList(action) {
    try {
        const { meetupId, ...params } = action.payload || {};
        let result;

        if (meetupId) {
            result = yield call(fetchReviewsByMeetupApi, action.payload);
        } else {
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

export function* changeReviewVisibility(action) {
    try {
        const reviewId = action.payload;
        yield call(changeReviewVisibilityApi, reviewId);
        yield put(changeVisibilitySuccess(reviewId));
    } catch (err) {
        const errorMsg = typeof err.response?.data === 'string'
            ? err.response.data
            : err.response?.data?.message || err.message;
        console.warn("⚠️ [공개 상태 변경 실패]:", errorMsg);
    }
}

// ★ [추가] 댓글 Saga 처리 함수들
export function* fetchComments(action) {
    try {
        // 💡 [수정] action.payload가 객체({ reviewId: 15 })이든 단일 값(15)이든 모두 안전하게 추출합니다.
        const reviewId = typeof action.payload === 'object' && action.payload !== null
            ? action.payload.reviewId || action.payload.id
            : action.payload;

        if (!reviewId) {
            console.error("❌ 댓글 조회 실패: reviewId가 존재하지 않습니다.", action.payload);
            return;
        }

        const result = yield call(fetchCommentsApi, reviewId);
        
        // 백엔드 반환값이 배열일 때와 객체({ content: [...] })일 때 모두 안전하게 지원
        const commentsData = Array.isArray(result.data) 
            ? result.data 
            : (result.data?.content || result.data?.comments || []);

        yield put(getCommentsSuccess({
            reviewId: reviewId,
            comments: commentsData
        }));
    } catch (err) {
        const errorMsg = typeof err.response?.data === 'string'
            ? err.response.data
            : err.response?.data?.message || err.message;
        yield put(getCommentsFailure(errorMsg));
    }
}

// export function* fetchComments(action) {
//     try {
//         const reviewId = action.payload;
//         const result = yield call(fetchCommentsApi, reviewId);
        
//         // ★ [수정] reviewId와 댓글 목록을 객체로 묶어서 리듀서로 전달
//         yield put(getCommentsSuccess({
//             reviewId: reviewId,
//             comments: result.data.content || result.data.comments || result.data
//         }));
//     } catch (err) {
//         const errorMsg = typeof err.response?.data === 'string'
//             ? err.response.data
//             : err.response?.data?.message || err.message;
//         yield put(getCommentsFailure(errorMsg));
//     }
// }

export function* createComment(action) {
    try {
        const { reviewId, content, parentCommentId } = action.payload;
        yield call(createCommentApi, { reviewId, content, parentCommentId });
        yield put(createCommentSuccess());
        // 작성 후 댓글 목록 자동 갱신
        yield put(getCommentsRequest(reviewId));
    } catch (err) {
        const errorMsg = typeof err.response?.data === 'string'
            ? err.response.data
            : err.response?.data?.message || err.message;
        yield put(createCommentFailure(errorMsg));
    }
}

// export function* updateComment(action) {
//     try {
//         const { commentId, content, reviewId } = action.payload;
//         yield call(updateCommentApi, { commentId, content });
//         yield put(updateCommentSuccess());
//         if (reviewId) {
//             yield put(getCommentsRequest(reviewId));
//         }
//     } catch (err) {
//         const errorMsg = typeof err.response?.data === 'string'
//             ? err.response.data
//             : err.response?.data?.message || err.message;
//         yield put(updateCommentFailure(errorMsg));
//     }
// }

export function* deleteComment(action) {
    try {
        const { commentId, reviewId } = action.payload;
        yield call(deleteCommentApi, commentId);
        yield put(deleteCommentSuccess(commentId));
        if (reviewId) {
            yield put(getCommentsRequest(reviewId));
        }
    } catch (err) {
        const errorMsg = typeof err.response?.data === 'string'
            ? err.response.data
            : err.response?.data?.message || err.message;
        yield put(deleteCommentFailure(errorMsg));
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
function* watchFetchAdminReviewList() { yield takeLatest(getReviewListRequest, fetchAdminReviewList); }
function* watchAdminDeleteReview() { yield takeLatest(deleteReviewRequest, adminDeleteReview); }
function* watchChangeReviewVisibility() { yield takeLatest('review/changeVisibilityRequest', changeReviewVisibility); }

// ★ [추가] 댓글 Watcher 함수들
function* watchFetchComments() { yield takeLatest(getCommentsRequest, fetchComments); }
function* watchCreateComment() { yield takeLatest(createCommentRequest, createComment); }
function* watchDeleteComment() { yield takeLatest(deleteCommentRequest, deleteComment); }
// function* watchFetchComments() { yield takeLatest(getCommentsRequest, fetchComments); }
// function* watchCreateComment() { yield takeLatest(createCommentRequest, createComment); }
// // function* watchUpdateComment() { yield takeLatest(updateCommentRequest, updateComment); }
// function* watchDeleteComment() { yield takeLatest(deleteCommentRequest, deleteComment); }

export default function* reviewSaga() {
    yield all([
        fork(watchCreateReview),
        fork(watchFetchReviewDetail),
        fork(watchUpdateReview),
        fork(watchDeleteReview),
        fork(watchFetchReviewList),
        fork(watchAnalyzeReviews),
        fork(watchToggleReviewLike),
        fork(watchFetchAdminReviewList),
        fork(watchAdminDeleteReview),
        fork(watchChangeReviewVisibility),
        // ★ [추가] 댓글 포크 등록
        fork(watchFetchComments),
        fork(watchCreateComment),
        // fork(watchUpdateComment),
        fork(watchDeleteComment),
    ]);
}