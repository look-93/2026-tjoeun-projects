import React, { useState, useEffect } from "react";

import MeetupImageCarousel from "../../../components/MeetupImageCarousel";
import MeetupWeather from "../../../components/MeetupWeather";
import MeetupTabs from "../../../components/MeetupTabs";
import MeetupRecruitInfo from "../../../components/MeetupRecruitInfo";
import MeetupAuthor from "../../../components/MeetupAuthor";
import RecommendedMeetups from "../../../components/RecommendedMeetups";
import MeetupMap from "../../../components/MeetupMap";
import MeetupAd from "../../../components/MeetupAd";
import { useRouter } from "next/router";
import { useSelector, useDispatch } from "react-redux";
import { fetchMeetupDetailRequest } from "../../../reducers/meetupReducer";
import { fetchWeatherRequest } from "../../../reducers/commonReducer";
import { Row, Col, Card, Button, Typography, Tag } from "antd";
import {
    ArrowLeftOutlined,
    ExclamationCircleOutlined,
} from "@ant-design/icons";

// http://localhost:3000/user/meetup/detail

const { Title } = Typography;

function MeetupDetailPage() {
    const [activeTab, setActiveTab] = useState("detail");
    const router = useRouter();
    const dispatch = useDispatch();
    const { meetup } = useSelector((state) => state.meetup);
    const { weather } = useSelector((state) => state.common);
    const { user } = useSelector((state) => state.user);
    const { meetupId } = router.query;
    const isOwner = 1 === meetup?.memberId; //user?.id === meetup?.memberId;

    // =========================
    // 모임 데이터
    // =========================
    useEffect(() => {
        if (!router.isReady || !meetupId) {
            return;
        }

        dispatch(fetchMeetupDetailRequest(meetupId));
    }, [router.isReady, meetupId, dispatch]);

    // =========================
    // 날씨 데이터
    // =========================
    useEffect(() => {
        if (!meetup || !meetup.meetupAt) {
            return;
        }
        const meetupDate = meetup.meetupAt.substring(0, 10).replaceAll("-", "");

        const meetupTime = Number(meetup.meetupAt.substring(11, 13));
        dispatch(
            fetchWeatherRequest({
                meetupDate: meetupDate,
                meetupTime: meetupTime,
                nx: meetup.nx,
                ny: meetup.ny,
            }),
        );
    }, [meetup, dispatch]);

    // =========================
    // 로딩 처리
    // =========================
    if (!meetup) {
        return <div>모임 정보를 불러오는 중입니다...</div>;
    }

    // =========================
    // 이미지
    // =========================
    const images =
        meetup?.imagePaths?.length > 0
            ? meetup.imagePaths.map(
                  (imagePath) =>
                      `http://localhost:8080/upload/meetup/${imagePath}`,
              )
            : ["http://localhost:8080/upload/no-image.png"];

    // =========================
    // 추천 모임
    // =========================
    const recommendedMeetups = [
        {
            id: 1,
            title: "한강 자전거 모임",
            location: "서울",
        },
        {
            id: 2,
            title: "주말 등산 모임",
            location: "서울",
        },
        {
            id: 3,
            title: "러닝 초보 모임",
            location: "인천",
        },
    ];

    // =========================
    // 후기
    // =========================
    const reviews = [
        {
            id: 1,
            nickname: "김철수",
            rating: 5,
            content: "분위기도 좋고 정말 재밌었습니다!",
            date: "2026.08.10",
            likes: 12,
        },
        {
            id: 2,
            nickname: "이영희",
            rating: 4,
            content: "다음에도 참여하고 싶어요.",
            date: "2026.08.08",
            likes: 7,
        },
    ];

    // =========================
    // Q&A
    // =========================
    const qnaLists = [
        {
            id: 1,
            nickname: "김철수",
            title: "초보자도 참여 가능한가요?",
            content: "러닝을 처음 시작하는 사람도 참여할 수 있나요?",
            answer: "네! 초보자도 편하게 참여 가능합니다.",
        },
        {
            id: 2,
            nickname: "이영희",
            title: "몇 시에 모이나요?",
            content: "정확한 집합 시간이 궁금합니다.",
            answer: null,
        },
    ];

    // =========================
    // 광고
    // =========================
    const ad = {
        title: "Moit 특별 이벤트",
        image: "/images/ad-banner.png",
    };

    return (
        <div className="meetup-detail-page">
            {/* 목록으로 */}
            <Row style={{ marginBottom: 16 }}>
                <Col span={24}>
                    <Button
                        type="text"
                        icon={<ArrowLeftOutlined />}
                        className="meetup-back-button"
                    >
                        목록으로
                    </Button>
                </Col>
            </Row>

            <Row gutter={[24, 24]}>
                {/* =========================
            LEFT
        ========================== */}
                <Col xs={24} lg={16}>
                    {/* 날씨 */}
                    <MeetupWeather
                        temperature={weather?.tmp}
                        precipitation={weather?.pop}
                        sky={weather?.sky}
                    />

                    {/* 이미지 */}
                    <MeetupImageCarousel images={images} />

                    {/* 제목 */}
                    <Card className="meetup-title-card">
                        <Row justify="space-between" align="middle">
                            <Col>
                                {meetup?.meetupStatus === "RECRUITING" ? (
                                    <Tag color="green">모집중</Tag>
                                ) : meetup?.meetupStatus ===
                                  "WEATHER_CANCELED" ? (
                                    <Tag color="red">기상학화로인한취소</Tag>
                                ) : (
                                    <Tag color="red">종료</Tag>
                                )}
                            </Col>

                            <Col>
                                <Button
                                    danger
                                    onClick={() =>
                                        router.push(
                                            `/user/meetup/report/write?type=MEETUP&targetId=${meetup.meetupId}`,
                                        )
                                    }
                                >
                                    신고
                                </Button>
                            </Col>
                        </Row>

                        <Title level={2} style={{ marginTop: 16 }}>
                            {meetup.title}
                        </Title>
                    </Card>

                    {/* 탭 */}
                    <MeetupTabs
                        activeTab={activeTab}
                        setActiveTab={setActiveTab}
                        meetup={meetup}
                        reviews={reviews}
                        qnaLists={qnaLists}
                    />
                </Col>

                {/* =========================
            RIGHT SIDEBAR
        ========================== */}
                <Col xs={24} lg={8}>
                    {/* 모집 정보 */}
                    <MeetupRecruitInfo meetup={meetup} isOwner={isOwner} />

                    {/* 작성자 */}
                    <MeetupAuthor meetup={meetup} />

                    {/* 추천 모임 */}
                    <RecommendedMeetups
                        recommendedMeetups={recommendedMeetups}
                    />

                    {/* 지도 */}
                    <MeetupMap
                        latitude={meetup.latitude}
                        longitude={meetup.longitude}
                        address={meetup.address}
                    />

                    {/* 광고 */}
                    <MeetupAd ad={ad} />
                </Col>
            </Row>
        </div>
    );
}

export default MeetupDetailPage;
