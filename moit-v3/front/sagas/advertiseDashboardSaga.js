import {
    all,
    call,
    put,
    takeLatest,
} from 'redux-saga/effects';

import {
    getAdvertiseDashboardSummary,
    getAdvertiseDashboardDaily,
    getAdvertiseDashboardCtr,
    getAdvertiseDashboardGrade,
    getAdvertiseDashboardPosition,
    getAdvertiseDashboardExtensionRate,
    getAdvertiseDashboardPositionCtr,
    getAdvertiseDashboardAiSummary,
} from '../api/advertiseDashboardApi';

import {
    getAdvertiseDashboardRequest,
    getAdvertiseDashboardSuccess,
    getAdvertiseDashboardFailure,
} from '../reducers/advertiseDashboardReducer';


// =========================================================
// 대시보드 전체 조회
// =========================================================

export function* getAdvertiseDashboardSaga() {

    try {
        const [
            statisticsRes,
            summaryRes,
            dailyRes,
            ctrRes,
            gradeRes,
            positionRes,
            extensionRes,
            positionCtrRes,
            aiSummaryRes,
        ] = yield all([
            call( getAdvertiseDashboardSummary ),
            call( getAdvertiseDashboardDaily ),
            call( getAdvertiseDashboardCtr ),
            call( getAdvertiseDashboardGrade ),
            call( getAdvertiseDashboardPosition ),
            call( getAdvertiseDashboardExtensionRate ),
            call( getAdvertiseDashboardPositionCtr ),
            call( getAdvertiseDashboardAiSummary ),
        ]);

        console.log('===== 광고 대시보드 API =====');

        console.log('summary:', summaryRes);
        console.log('daily:', dailyRes);
        console.log('ctr:', ctrRes);
        console.log('grade:', gradeRes);
        console.log('position:', positionRes);
        console.log('extension:', extensionRes);
        console.log('positionCtr:', positionCtrRes);
        console.log('aiSummary:', aiSummaryRes);

        yield put(
            getAdvertiseDashboardSuccess({

                statistics:
                    statisticsRes?.data ?? {},

                summary:
                    summaryRes?.data ?? {},

                dailyData:
                    Array.isArray(dailyRes?.data)
                        ? dailyRes.data
                        : [],

                ctrData:
                    Array.isArray(ctrRes?.data)
                        ? ctrRes.data
                        : [],

                gradeData:
                    Array.isArray(gradeRes?.data)
                        ? gradeRes.data
                        : [],

                positionData:
                    Array.isArray(positionRes?.data)
                        ? positionRes.data
                        : [],

                extensionRate:
                    Number(extensionRes?.data ?? 0),

                positionCtrData:
                    Array.isArray(positionCtrRes?.data)
                        ? positionCtrRes.data
                        : [],

                aiSummary:
                    aiSummaryRes?.data ?? null,
            })
        );

    } catch (error) {

        console.error(
            '===== 광고 대시보드 API 실패 ====='
        );

        console.error(
            'status:',
            error.response?.status
        );

        console.error(
            'url:',
            error.config?.url
        );

        console.error(
            'data:',
            error.response?.data
        );

        console.error(
            'message:',
            error.message
        );

        yield put(
            getAdvertiseDashboardFailure(
                error.response?.data ||
                '광고 대시보드 조회 실패'
            )
        );
    }
}

// =========================================================
// Watcher
// =========================================================

export function* watchAdvertiseDashboard() {

    yield takeLatest(
        getAdvertiseDashboardRequest.type,
        getAdvertiseDashboardSaga
    );
}

// =========================================================
// Dashboard Saga
// =========================================================

export default function* advertiseDashboardSaga() {

    yield all([
        watchAdvertiseDashboard()
    ]);
}