import { createSlice } from '@reduxjs/toolkit';

const initialState = {
    meetups: [], // 전체 모임
    meetup: null, // 단건 모임
    loading: false,
    error: null,
    success: false,
};

const meetupReducer = createSlice({
    name: 'meetup',
    initialState,
    reducers: {
        // 전체 모임 조회 요청
        fetchMeetupRequest: (state) => {
            state.loading = true;
            state.error = null;
            state.success = false;
        },
        // 전체 모임 조회 성공
        fetchMeetupsSuccess: (state, action) => {
            state.loading = false;
            state.meetups = action.payload;
            state.success = true;
        },

        // 전체 모임 조회 실패
        fetchMeetupsFailure: (state, action) => {
            state.loading = false;
            state.error = action.payload;
            state.success = false;
        },

        //상태조기화
        resetMeetupState: (state) => {
            state.loading = false;
            state.error = null;
            state.success = false;
        },
    },
});

export const {
    fetchMeetupsRequest,
    fetchMeetupsSuccess,
    fetchMeetupsFailure,
    resetMeetupState,
} = meetupReducer.actions;

export default meetupReducer.reducer;
