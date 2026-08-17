import { all, call, put, takeLatest } from 'redux-saga/effects';
import api from '../api/axios';
import {
    fetchMeetupsRequest,
    fetchMeetupsSuccess,
    fetchMeetupsFailure,
    resetMeetupState,
} from '../reducers/meetupReducer';

const MEETUP_API_BASE = '/api/meetups';
// // GET - 목록 조회
// export const fetch
// // GET - 단건 조회
// export const fetchPost
// // POST - 생성
// export const create
// // PUT/PATCH - 수정
// export const update
// // DELETE - 삭제
// export const delete

//
export const fetchMeetupsAPI = (params) =>
    api.get(`${MEETUP_API_BASE}`, { params });
export function* fetchMeetups() {
    try {
        const result = yield call(fetchMeetupsAPI, {
            page: 0,
            size: 10,
        });
        yield put(fetchMeetupsSuccess(result.data));
    } catch (err) {
        yield put(
            fetchMeetupsFailure(err.response?.data?.message || err.message),
        );
    }
}
function* watchFetchMeetups() {
    yield takeLatest(fetchMeetupsRequest.type, fetchMeetups);
}
