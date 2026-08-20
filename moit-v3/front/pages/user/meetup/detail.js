import React, { useState, useEffect } from 'react';

import MeetupImageCarousel from '../../../components/MeetupImageCarousel';
import MeetupWeather from '../../../components/MeetupWeather';
import MeetupTabs from '../../../components/MeetupTabs';
import MeetupRecruitInfo from '../../../components/MeetupRecruitInfo';
import MeetupAuthor from '../../../components/MeetupAuthor';
import RecommendedMeetups from '../../../components/RecommendedMeetups';
import MeetupMap from '../../../components/MeetupMap';
import MeetupAd from '../../../components/MeetupAd';
import { useRouter } from 'next/router';
import { getReviewListRequest, toggleReviewLikeRequest } from '../../../reducers/reviewReducer';
import { useDispatch, useSelector } from 'react-redux';

import { Row, Col, Card, Button, Typography, Tag } from 'antd';
import {
  ArrowLeftOutlined,
  ExclamationCircleOutlined,
} from '@ant-design/icons';

const { Title } = Typography;

function MeetupDetailPage() {
  const [activeTab, setActiveTab] = useState('detail');
  const router = useRouter();
  const dispatch = useDispatch();

  // Redux Store에서 reviews 가져오기
  const { reviews: reduxReviews } = useSelector((state) => {
    if (!state) return {};
    return state.review || state.reviewReducer || {};
  });

  // 1. 현재 모임 ID 추출
  const currentMeetupId = router.query.meetupId ? Number(router.query.meetupId) : 1;

  // URL tab 쿼리 파라미터 처리 (탭 변경)
  useEffect(() => {
    if (!router.isReady) return;

    if (router.query.tab) {
      setActiveTab(router.query.tab);
    }
  }, [router.isReady, router.query.tab]);

  // 2. 리뷰 목록 조회 (의존성 배열 수정: router.query 제거 및 currentMeetupId 사용)
  // ★ router.query 전체를 넣으면 좋아요 클릭 시 재렌더링으로 서버 데이터를 재요청하여 상태를 덮어씁니다!
  useEffect(() => {
    if (!router.isReady || !currentMeetupId) return;

    // 최초 로딩 시에도 기본 페이징과 정렬 값을 함께 전달
    dispatch(getReviewListRequest({ 
        meetupId: currentMeetupId,
        page: 0,
        size: 10,
        sort: 'id,desc'
    }));
}, [dispatch, router.isReady, currentMeetupId]);

  // 3. 좋아요 핸들러
  const handleLikeReview = (reviewId) => {
    if (!reviewId) return;

    console.log('좋아요 요청 실행! 리뷰 ID:', reviewId);
    dispatch(toggleReviewLikeRequest(reviewId));
  };
  // 정렬 핸들러 추가
  const handleSortChange = (sortParam) => {
    console.log('정렬 요청 실행:', sortParam);
    dispatch(
      getReviewListRequest({
        meetupId: currentMeetupId,
        sort: sortParam, // 예: 'likesCount,desc' 또는 'id,desc'
      })
    );
  };

  // 리뷰 검색 핸들러 추가
  const handleSearch = (keyword) => {
    console.log('2. MeetupDetailPage에서 handleSearch 실행됨! 검색어:', keyword);
    console.log('현재 모임 ID:', currentMeetupId);
    
    dispatch(
      getReviewListRequest({
        meetupId: currentMeetupId,
        keyword: keyword, 
      })
    );
  };

  // 모임 데이터
  const meetup = {
    //meetupId: currentMeetupId,
    meetupId: 25, // 테스트용 추가함
    title: '주말 한강 러닝 같이 하실 분!',
    status: '모집중',
    participants: 8,
    maxParticipants: 10,
    location: '서울',
    author: '보라',
    content:
      '주말마다 한강에서 같이 러닝하실 분들을 모집합니다. 초보자도 편하게 참여하실 수 있습니다.',
  };

  // 이미지
  const images = [
    '/images/meetup1.jpg',
    '/images/meetup2.jpg',
    '/images/meetup3.jpg',
  ];

  // 추천 모임
  const recommendedMeetups = [
    {
      id: 1,
      title: '한강 자전거 모임',
      location: '서울',
    },
    {
      id: 2,
      title: '주말 등산 모임',
      location: '서울',
    },
    {
      id: 3,
      title: '러닝 초보 모임',
      location: '인천',
    },
  ];

  // ★ 후기 데이터 변환 (isPublic 및 isLiked 포함)
  const rawReviews =
    reduxReviews?.map((review) => ({
      id: review.id,
      nickname: review.memberNickname || review.nickname || '익명',
      rating: review.rating,
      content: review.content,
      date: review.createdAt ? String(review.createdAt).substring(0, 10) : '',
      likesCount: review.likesCount ?? 0,
      isLiked: Boolean(review.isLiked || review.liked),
      images: review.images || [],
      isPublic: review.isPublic ?? "Y", // 👈 핵심: isPublic 필드 매핑 추가!
    })) || [];

  // ★ 핵심 안전 장치: 모임 상세 페이지에서는 오직 공개된('Y') 후기만 보여줍니다!
  const reviews = rawReviews.filter((review) => review.isPublic === "Y");

  // Q&A
  const qnaLists = [
    {
      id: 1,
      nickname: '김철수',
      title: '초보자도 참여 가능한가요?',
      content: '러닝을 처음 시작하는 사람도 참여할 수 있나요?',
      answer: '네! 초보자도 편하게 참여 가능합니다.',
    },
    {
      id: 2,
      nickname: '이영희',
      title: '몇 시에 모이나요?',
      content: '정확한 집합 시간이 궁금합니다.',
      answer: null,
    },
  ];

  // 날씨
  const weather = {
    tmp: 24,
    pop: 10,
    sky: '맑음',
  };

  // 광고
  const ad = {
    title: 'Moit 특별 이벤트',
    image: '/images/ad-banner.png',
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
            temperature={weather.tmp}
            precipitation={weather.pop}
            sky={weather.sky}
          />

          {/* 이미지 */}
          <MeetupImageCarousel images={images} />

          {/* 제목 */}
          <Card className="meetup-title-card">
            <Row justify="space-between" align="middle">
              <Col>
                <Tag color="green">{meetup.status}</Tag>
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
            onLikeReview={handleLikeReview}
            onSortChange={handleSortChange}
            onSearch={handleSearch}
          />
        </Col>

        {/* RIGHT SIDEBAR */}
        <Col xs={24} lg={8}>
          <MeetupRecruitInfo meetup={meetup} />
          <MeetupAuthor meetup={meetup} />
          <RecommendedMeetups recommendedMeetups={recommendedMeetups} />
          <MeetupMap />
          <MeetupAd ad={ad} />
        </Col>
      </Row>
    </div>
  );
}

export default MeetupDetailPage;