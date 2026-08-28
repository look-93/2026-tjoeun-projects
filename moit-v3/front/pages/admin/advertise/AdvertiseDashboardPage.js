import { useEffect } from 'react';

import {
    useDispatch,
    useSelector,
} from 'react-redux';

import {
    Row,
    Col,
    Card,
    Statistic,
    Typography,
    Spin,
    Empty,
    Button,
    message,
} from 'antd';

import {
    Line,
    Bar,
    Pie,
} from 'react-chartjs-2';

import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    BarElement,
    ArcElement,
    Tooltip,
    Legend,
} from 'chart.js';

import {
    getAdvertiseDashboardRequest,
} from '../../../reducers/advertiseDashboardReducer';

const { Title, Text } = Typography;

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    BarElement,
    ArcElement,
    Tooltip,
    Legend
);

function AdvertiseDashboardPage() {

    const dispatch = useDispatch();

    // =========================================================
    // Redux 상태
    // =========================================================
    const {
        summary,
        dailyData,
        ctrData,
        gradeData,
        positionData,
        positionCtrData,
        extensionRate,
        aiSummary,
        loading,
        error,
    } = useSelector(
        state => state.advertiseDashboard
    );

    // =========================================================
    // 대시보드 조회
    // =========================================================
    useEffect(() => {

        dispatch(
            getAdvertiseDashboardRequest()
        );

    }, [dispatch]);

    // =========================================================
    // 에러 처리
    // =========================================================
    useEffect(() => {

        if (error) {

            console.error(
                '광고 대시보드 조회 실패',
                error
            );

            message.error(
                '광고 대시보드 데이터를 불러오지 못했습니다.'
            );
        }

    }, [error]);

    // =========================================================
    // 최근 7일
    // =========================================================
    const dailyChartData = {

        labels: dailyData.map(
            item => item.statDate
        ),

        datasets: [

            {
                label: '노출수',

                data: dailyData.map(
                    item => item.impressions ?? 0
                ),

                borderWidth: 2,

                tension: 0.3,
            },

            {
                label: '클릭수',

                data: dailyData.map(
                    item => item.clicks ?? 0
                ),

                borderWidth: 2,

                tension: 0.3,
            },
        ],
    };

    const dailyChartOptions = {

        responsive: true,

        maintainAspectRatio: false,

        scales: {

            y: {
                beginAtZero: true,
            },
        },
    };

    // =========================================================
    // CTR TOP 5
    // =========================================================
    const ctrChartData = {

        labels: ctrData.map(
            item => item.title
        ),

        datasets: [

            {
                label: 'CTR (%)',

                data: ctrData.map(
                    item => item.ctr ?? 0
                ),

                borderWidth: 1,
            },
        ],
    };

    const ctrChartOptions = {

        responsive: true,

        maintainAspectRatio: false,

        scales: {

            y: {
                beginAtZero: true,
            },
        },
    };

    // =========================================================
    // 광고 등급
    // =========================================================
    const gradeChartData = {

        labels: gradeData.map(
            item => item.adGrade
        ),

        datasets: [

            {
                data: gradeData.map(
                    item => item.count ?? 0
                ),
            },
        ],
    };

    // =========================================================
    // 위치별 노출
    // =========================================================
    const positionChartData = {

        labels: positionData.map(
            item => item.position
        ),

        datasets: [

            {
                label: '노출수',

                data: positionData.map(
                    item => item.impressions ?? 0
                ),

                borderWidth: 1,
            },
        ],
    };

    const positionChartOptions = {

        responsive: true,

        maintainAspectRatio: false,

        plugins: {

            legend: {
                display: false,
            },
        },

        scales: {

            y: {
                beginAtZero: true,
            },
        },
    };

    // =========================================================
    // 위치별 CTR
    // =========================================================
    const positionCtrChartData = {

        labels: positionCtrData.map(
            item => item.position
        ),

        datasets: [

            {
                label: 'CTR (%)',

                data: positionCtrData.map(
                    item => item.ctr ?? 0
                ),

                borderWidth: 1,
            },
        ],
    };

    const positionCtrChartOptions = {

        responsive: true,

        maintainAspectRatio: false,

        scales: {

            y: {
                beginAtZero: true,
            },
        },
    };

    // =========================================================
    // 로딩
    // =========================================================
    if (loading) {
        return (
            <div
                style={{
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    minHeight: 500,
                }}
            >
                <Spin size="large" />
            </div>
        );
    }

    // =========================================================
    // 화면
    // =========================================================
    return (
        <div>
            {/* =================================================
                헤더
            ================================================= */}
            <div
                style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    marginBottom: 24,
                }}
            >
                <div>

                    <Title
                        level={2}
                        style={{ margin: 0 }}
                    >
                        📊 광고 대시보드
                    </Title>

                    <Text type="secondary">
                        광고 운영 현황 및 성과 분석
                    </Text>

                </div>

                <Button
                    onClick={() => {
                        window.location.href =
                            '/admin/advertise';
                    }}
                >
                    광고 관리
                </Button>
            </div>

            {/* =================================================
                상단 요약
            ================================================= */}
            <Row
                gutter={[16, 16]}
                style={{ marginBottom: 24 }}
            >
                <Col xs={24} sm={12} lg={6}>
                    <Card>
                        <Statistic
                            title="총 광고"
                            value={summary?.totalAd ?? 0}
                            suffix="개"
                        />
                    </Card>
                </Col>

                <Col xs={24} sm={12} lg={6}>
                    <Card>
                        <Statistic
                            title="총 노출"
                            value={summary?.totalImp ?? 0}
                        />
                    </Card>
                </Col>

                <Col xs={24} sm={12} lg={6}>
                    <Card>
                        <Statistic
                            title="총 클릭"
                            value={summary?.totalClick ?? 0}
                        />
                    </Card>
                </Col>

                <Col xs={24} sm={12} lg={6}>
                    <Card>
                        <Statistic
                            title="평균 CTR"
                            value={summary?.avgCtr ?? 0}
                            precision={2}
                            suffix="%"
                        />
                    </Card>
                </Col>
            </Row>

            {/* =================================================
                AI 운영 분석
            ================================================= */}
            <Card
                title="🤖 AI 운영 분석"
                style={{ marginBottom: 24 }}
            >
                {aiSummary?.summary ? (
                    <>
                        <div
                            style={{
                                whiteSpace: 'pre-line',
                                lineHeight: 1.9,
                            }}
                        >
                            {aiSummary.summary}
                        </div>

                        <div
                            style={{
                                marginTop: 16,
                                textAlign: 'right',
                            }}
                        >
                            <Text type="secondary">

                                생성 시각 : {
                                    aiSummary.createdAt || '-'
                                }
                            </Text>
                        </div>
                    </>
                ) : (
                    <Empty
                        description="아직 생성된 AI 분석이 없습니다."
                    />
                )}
            </Card>

            {/* =================================================
                최근 7일
            ================================================= */}
            <Card
                title="최근 7일 광고 통계"
                style={{ marginBottom: 24 }}
            >
                <div
                    style={{
                        height: 400,
                    }}
                >
                    <Line
                        data={dailyChartData}
                        options={dailyChartOptions}
                    />
                </div>
            </Card>

            {/* =================================================
                CTR TOP5 + 등급 + 연장률
            ================================================= */}
            <Row
                gutter={[16, 16]}
                style={{ marginBottom: 24 }}
            >
                <Col xs={24} lg={12}>
                    <Card title="CTR TOP 5">
                        <div
                            style={{
                                height: 350,
                            }}
                        >
                            <Bar
                                data={ctrChartData}
                                options={ctrChartOptions}
                            />
                        </div>
                    </Card>
                </Col>

                <Col xs={24} md={12} lg={6}>
                    <Card title="광고 등급 비율">
                        <div
                            style={{
                                height: 300,
                                display: 'flex',
                                justifyContent: 'center',
                            }}
                        >
                            <Pie
                                data={gradeChartData}
                                options={{
                                    responsive: true,
                                    maintainAspectRatio: false,
                                }}
                            />
                        </div>
                    </Card>
                </Col>

                <Col xs={24} md={12} lg={6}>
                    <Card title="광고 연장률">
                        <div
                            style={{
                                height: 300,
                                display: 'flex',
                                flexDirection: 'column',
                                justifyContent: 'center',
                                alignItems: 'center',
                            }}
                        >
                            <div
                                style={{
                                    fontSize: 48,
                                    fontWeight: 700,
                                }}
                            >
                                {Number(extensionRate).toFixed(2)}%
                            </div>

                            <Text type="secondary">
                                전체 광고 대비
                            </Text>

                            <Text type="secondary">
                                연장 결제 비율
                            </Text>

                        </div>
                    </Card>
                </Col>
            </Row>

            {/* =================================================
                위치별 통계
            ================================================= */}
            <Row
                gutter={[16, 16]}
            >
                <Col xs={24} lg={12}>
                    <Card title="위치별 노출">
                        <div
                            style={{
                                height: 350,
                            }}
                        >
                            <Bar
                                data={positionChartData}
                                options={positionChartOptions}
                            />
                        </div>
                    </Card>
                </Col>

                <Col xs={24} lg={12}>
                    <Card title="광고 위치별 CTR">
                        <div
                            style={{
                                height: 350,
                            }}
                        >
                            <Bar
                                data={positionCtrChartData}
                                options={positionCtrChartOptions}
                            />
                        </div>
                    </Card>
                </Col>
            </Row>
        </div>
    );
}

export default AdvertiseDashboardPage;