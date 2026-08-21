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
import {
    getReviewListRequest,
    toggleReviewLikeRequest,
} from "../../../reducers/reviewReducer";
import {
    qnaMeetupListRequest,
} from '../../../reducers/qnaReducer';
import { useDispatch, useSelector } from "react-redux";

import { fetchMeetupDetailRequest } from "../../../reducers/meetupReducer";
import { fetchWeatherRequest } from "../../../reducers/commonReducer";

import { Row, Col, Card, Button, Typography, Tag } from "antd";
import {
    ArrowLeftOutlined,
    ExclamationCircleOutlined,
} from "@ant-design/icons";

const { Title } = Typography;

function MeetupDetailPage() {
    const [activeTab, setActiveTab] = useState("detail");
    const router = useRouter();
    const dispatch = useDispatch();

    // meetup
    const { meetup } = useSelector((state) => state.meetup);
    const { weather } = useSelector((state) => state.common);
    const { user } = useSelector((state) => state.user);
    const { meetupId } = router.query;
    const isOwner = user?.memberId === meetup?.memberId; //user?.id === meetup?.memberId;
    // qna
    const { meetupQnaList, loading: qnaLoading } = useSelector((state) => state.qna);

    useEffect(() => {
      if (!meetup?.meetupId) return;
      dispatch(qnaMeetupListRequest(meetup.meetupId));
    }, [meetup?.meetupId, dispatch]);
    //console.log(isOwner);
    //console.log(user);
    // Redux Store에서 reviews 가져오기
    const { reviews: reduxReviews } = useSelector((state) => {
        if (!state) return {};
        return state.review || state.reviewReducer || {};
    });

    // 1. 현재 모임 ID 추출
    const currentMeetupId = router.query.meetupId
        ? Number(router.query.meetupId)
        : 1;

    // URL tab 쿼리 파라미터 처리 (탭 변경)
    useEffect(() => {
        if (!router.isReady) return;

        if (router.query.tab) {
            setActiveTab(router.query.tab);
        }
    }, [router.isReady, router.query.tab]);

    // 2. 리뷰 목록 조회 (의존성 배열 수정: router.query 제거 및 currentMeetupId 사용)
    useEffect(() => {
        if (!router.isReady || !currentMeetupId) return;

        // 최초 로딩 시에도 기본 페이징과 정렬 값을 함께 전달
        dispatch(
            getReviewListRequest({
                meetupId: currentMeetupId,
                page: 0,
                size: 10,
                sort: "id,desc",
            }),
        );
    }, [dispatch, router.isReady, currentMeetupId]);

    // 3. 좋아요 핸들러
    const handleLikeReview = (reviewId) => {
        if (!reviewId) return;

        console.log("좋아요 요청 실행! 리뷰 ID:", reviewId);
        dispatch(toggleReviewLikeRequest(reviewId));
    };

    // 정렬 핸들러 추가
    const handleSortChange = (sortParam) => {
        console.log("정렬 요청 실행:", sortParam);
        dispatch(
            getReviewListRequest({
                meetupId: currentMeetupId,
                sort: sortParam, // 예: 'likesCount,desc' 또는 'id,desc'
            }),
        );
    };

    // 리뷰 검색 핸들러 추가
    const handleSearch = (keyword) => {
        console.log(
            "2. MeetupDetailPage에서 handleSearch 실행됨! 검색어:",
            keyword,
        );
        console.log("현재 모임 ID:", currentMeetupId);

        dispatch(
            getReviewListRequest({
                meetupId: currentMeetupId,
                keyword: keyword,
            }),
        );
    };

    // 모임 데이터
    useEffect(() => {
        if (!router.isReady || !meetupId) {
            return;
        }

        dispatch(fetchMeetupDetailRequest(meetupId));
    }, [router.isReady, meetupId, dispatch]);

    // 이미지
    const images =
        meetup?.imagePaths?.length > 0
            ? meetup.imagePaths.map(
                  (imagePath) =>
                      `http://localhost:8080/upload/meetup/${imagePath}`,
              )
            : ["http://localhost:8080/upload/no-image.png"];

    // 추천 모임
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

    // ★ 후기 데이터 변환 (isPublic 및 isLiked 포함)
    const rawReviews =
        reduxReviews?.map((review) => ({
            id: review.id,
            nickname: review.memberNickname || review.nickname || "익명",
            rating: review.rating,
            content: review.content,
            date: review.createdAt
                ? String(review.createdAt).substring(0, 10)
                : "",
            likesCount: review.likesCount ?? 0,
            isLiked: Boolean(review.isLiked || review.liked),
            images: review.images || [],
            isPublic: review.isPublic ?? "Y", // 👈 핵심: isPublic 필드 매핑 추가!
        })) || [];

    // ★ 핵심 안전 장치: 모임 상세 페이지에서는 오직 공개된('Y') 후기만 보여줍니다!
    const reviews = rawReviews.filter((review) => review.isPublic === "Y");

    // Q&A
    const qnaLists = Array.isArray(meetupQnaList)
    ? meetupQnaList
    : [];

    // 날씨
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

    //로딩처리
    if (!meetup) {
        return <div>모임 정보를 불러오는 중입니다...</div>;
    }

    // 광고
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
                        onClick={() => router.back()}
                    >
                        목록으로
                    </Button>
                </Col>
            </Row>

            <Row gutter={[24, 24]}>
                {/* LEFT */}
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
                                            // `/user/meetup/report/write?targetType=MEETUP&targetId=${meetup.meetupId}`,
                                            `/user/meetup/report/write?targetType=MEETUP&targetId=2`,
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
                        meetupId={currentMeetupId}
                        isHost={isOwner}
                        onLikeReview={handleLikeReview}
                        onSortChange={handleSortChange}
                        onSearch={handleSearch}
                    />
                </Col>

                {/* RIGHT SIDEBAR */}
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
