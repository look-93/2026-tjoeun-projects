import React, { useState } from 'react';

import MeetupImageCarousel from '../../../components/MeetupImageCarousel';
import MeetupWeather from '../../../components/MeetupWeather';
import MeetupTabs from '../../../components/MeetupTabs';
import MeetupRecruitInfo from '../../../components/MeetupRecruitInfo';
import MeetupAuthor from '../../../components/MeetupAuthor';
import RecommendedMeetups from '../../../components/RecommendedMeetups';
import MeetupMap from '../../../components/MeetupMap';
import MeetupAd from '../../../components/MeetupAd';
import { useRouter } from 'next/router';

import { Row, Col, Card, Button, Typography, Tag } from 'antd';
import {
  ArrowLeftOutlined,
  ExclamationCircleOutlined,
} from '@ant-design/icons';

// http://localhost:3000/user/meetup/detail

const { Title } = Typography;

function MeetupDetailPage() {
  const [activeTab, setActiveTab] = useState('detail');
  const router = useRouter();
  // =========================
  // 모임 데이터
  // =========================
  const meetup = {
    title: '주말 한강 러닝 같이 하실 분!',
    status: '모집중',
    participants: 8,
    maxParticipants: 10,
    location: '서울',
    author: '보라',
    content:
      '주말마다 한강에서 같이 러닝하실 분들을 모집합니다. 초보자도 편하게 참여하실 수 있습니다.',
  };

  // =========================
  // 이미지
  // =========================
  const images = [
    '/images/meetup1.jpg',
    '/images/meetup2.jpg',
    '/images/meetup3.jpg',
  ];

  // =========================
  // 추천 모임
  // =========================
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

  // =========================
  // 후기
  // =========================
  const reviews = [
    {
      id: 1,
      nickname: '김철수',
      rating: 5,
      content: '분위기도 좋고 정말 재밌었습니다!',
      date: '2026.08.10',
      likes: 12,
    },
    {
      id: 2,
      nickname: '이영희',
      rating: 4,
      content: '다음에도 참여하고 싶어요.',
      date: '2026.08.08',
      likes: 7,
    },
  ];

  // =========================
  // Q&A
  // =========================
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

  // =========================
  // 날씨
  // =========================
  const weather = {
    tmp: 24,
    pop: 10,
    sky: '맑음',
  };

  // =========================
  // 광고
  // =========================
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
          />
        </Col>

        {/* =========================
            RIGHT SIDEBAR
        ========================== */}
        <Col xs={24} lg={8}>
          {/* 모집 정보 */}
          <MeetupRecruitInfo meetup={meetup} />

          {/* 작성자 */}
          <MeetupAuthor meetup={meetup} />

          {/* 추천 모임 */}
          <RecommendedMeetups recommendedMeetups={recommendedMeetups} />

          {/* 지도 */}
          <MeetupMap />

          {/* 광고 */}
          <MeetupAd ad={ad} />
        </Col>
      </Row>
    </div>
  );
}

export default MeetupDetailPage;
