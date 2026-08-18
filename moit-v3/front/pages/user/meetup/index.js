import React, { useState } from 'react';
import { Row, Col } from 'antd';
import { useRouter } from 'next/router';

import MeetupListAd from '../../../components/MeetupListAd';
import MeetupSearchFilter from '../../../components/MeetupSearchFilter';
import MeetupCategory from '../../../components/MeetupCategory';
import MeetupList from '../../../components/MeetupList';
import CommonPagination from '../../../components/CommonPagination';

function MeetupListPage() {
  const router = useRouter();

  const [searchText, setSearchText] = useState('');

  const [sidoId, setSidoId] = useState(0);

  const [orderType, setOrderType] = useState('createAt');

  const [categoryId, setCategoryId] = useState(0);

  const [currentPage, setCurrentPage] = useState(1);

  // 임시 데이터
  const sidoList = [
    {
      sidoId: 1,
      name: '서울',
    },
    {
      sidoId: 2,
      name: '인천',
    },
    {
      sidoId: 3,
      name: '경기',
    },
  ];

  const categories = [
    {
      categoryId: 1,
      categoryName: '운동',
    },
    {
      categoryId: 2,
      categoryName: '스터디',
    },
    {
      categoryId: 3,
      categoryName: '취미',
    },
    {
      categoryId: 4,
      categoryName: '문화',
    },
    {
      categoryId: 5,
      categoryName: '맛집',
    },
  ];

  const meetups = [
    {
      meetupId: 1,
      title: '주말 한강 러닝 같이 하실 분!',
      status: 'RECRUITING',
      categoryName: '운동',
      sigunguName: '마포구',
      totalParticipants: 8,
      maxParticipants: 10,
      formattedMeetupAt: '2026.08.22 10:00',
      imagePath: null,
      hasLike: true,
      likeCnt: 15,
    },

    {
      meetupId: 2,
      title: '주말 등산 같이 가실 분',
      status: 'RECRUITING',
      categoryName: '운동',
      sigunguName: '관악구',
      totalParticipants: 5,
      maxParticipants: 10,
      formattedMeetupAt: '2026.08.23 09:00',
      imagePath: null,
      hasLike: false,
      likeCnt: 7,
    },

    {
      meetupId: 3,
      title: 'React 스터디 모집합니다',
      status: 'CLOSED',
      categoryName: '스터디',
      sigunguName: '강남구',
      totalParticipants: 10,
      maxParticipants: 10,
      formattedMeetupAt: '2026.08.20 19:00',
      imagePath: null,
      hasLike: false,
      likeCnt: 3,
    },
  ];

  const ad = {
    title: 'Moit 특별 이벤트',
    image: '/images/ad-banner.png',
  };

  // 검색
  const handleSearch = () => {
    console.log({
      searchText,
      sidoId,
      orderType,
      categoryId,
    });

    setCurrentPage(1);
  };

  // 카테고리
  const handleCategoryChange = (id) => {
    setCategoryId(id);
    setCurrentPage(1);
  };

  // 상세
  const handleMeetupClick = (meetupId) => {
    router.push(`/user/meetup/detail?meetupId=${meetupId}`);
  };

  // 좋아요
  const handleToggleLike = (meetupId) => {
    console.log('좋아요:', meetupId);
  };

  // 모임등록
  const handleCreateMeetup = () => {
    router.push('/user/meetup/write');
  };

  // 페이지
  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  return (
    <div className="meetup-list-page">
      <Row gutter={[24, 24]}>
        {/* =====================
            메인
        ====================== */}
        <Col xs={24} lg={18}>
          {/* 광고 */}
          <MeetupListAd ad={ad} />

          {/* 검색 */}
          <MeetupSearchFilter
            searchText={searchText}
            setSearchText={setSearchText}
            sidoId={sidoId}
            setSidoId={setSidoId}
            orderType={orderType}
            setOrderType={setOrderType}
            sidoList={sidoList}
            onSearch={handleSearch}
            onCreate={handleCreateMeetup}
          />

          {/* 모임 목록 */}
          <MeetupList
            meetups={meetups}
            onClick={handleMeetupClick}
            onToggleLike={handleToggleLike}
          />

          {/* 페이지 */}
          <CommonPagination
            current={currentPage}
            total={30}
            pageSize={10}
            onChange={handlePageChange}
          />
        </Col>

        {/* =====================
            사이드바
        ====================== */}
        <Col xs={24} lg={6}>
          <MeetupCategory
            categories={categories}
            selectedCategoryId={categoryId}
            onChange={handleCategoryChange}
          />
        </Col>
      </Row>
    </div>
  );
}

export default MeetupListPage;
