import { createSlice } from '@reduxjs/toolkit';

const initialState = {

    summary: {},

    // 광고 운영 현황
    statistics: {
        totalAdCnt: 0,
        openCnt: 0,
        pendingCnt: 0,
        closedCnt: 0,
    },

    dailyData: [],

    ctrData: [],

    gradeData: [],

    positionData: [],

    positionCtrData: [],

    extensionRate: 0,

    aiSummary: null,

    loading: false,

    error: null,
};

const advertiseDashboardSlice = createSlice({

    name: 'advertiseDashboard',

    initialState,

    reducers: {

        // =====================================================
        // 대시보드 조회
        // =====================================================

        getAdvertiseDashboardRequest: (state) => {

            state.loading = true;
            state.error = null;
        },

        getAdvertiseDashboardSuccess: (state, action) => {

            state.loading = false;

            state.summary =
                action.payload.summary ?? {};

            // 광고 운영 현황
            state.statistics =
                action.payload.statistics ?? {
                    totalAdCnt: 0,
                    openCnt: 0,
                    pendingCnt: 0,
                    closedCnt: 0,
                };

            state.dailyData =
                action.payload.dailyData ?? [];

            state.ctrData =
                action.payload.ctrData ?? [];

            state.gradeData =
                action.payload.gradeData ?? [];

            state.positionData =
                action.payload.positionData ?? [];

            state.positionCtrData =
                action.payload.positionCtrData ?? [];

            state.extensionRate =
                action.payload.extensionRate ?? 0;

            state.aiSummary =
                action.payload.aiSummary ?? null;
        },

        getAdvertiseDashboardFailure: (state, action) => {

            state.loading = false;

            state.error = action.payload;
        },
    },
});

export const {
    getAdvertiseDashboardRequest,
    getAdvertiseDashboardSuccess,
    getAdvertiseDashboardFailure,
} = advertiseDashboardSlice.actions;

export default advertiseDashboardSlice.reducer;