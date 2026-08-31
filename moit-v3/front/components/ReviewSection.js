import React, { useState } from 'react';
import {
  Row,
  Col,
  Button,
  Input,
  Select,
  Card,
  Typography,
  Rate,
  Progress,
  Space,
  Avatar,
  Image,
  Modal,
  Spin,
} from 'antd';
import { 
  SearchOutlined, 
  LikeOutlined, 
  LikeFilled, 
  UserOutlined, 
  RobotOutlined,
  LoadingOutlined 
} from '@ant-design/icons';
import { useRouter } from 'next/router';
import { useDispatch, useSelector } from 'react-redux';
import { analyzeReviewsRequest,resetReviewState } from '../reducers/reviewReducer'; 
import ReviewComments from './ReviewComment'; 

const { Title, Text, Paragraph } = Typography;

const BACKEND_URL = 'http://localhost:8080'; // 본인 백엔드 주소

const getImageUrl = (imgItem) => {
  if (imgItem === null || imgItem === undefined) return null;

  // 1. 만약 백엔드 엔티티 구조상 imgItem 안에 image 객체가 포함되어 있다면 추출
  const target = imgItem.image || imgItem;

  if (typeof target === 'number') {
    return `${BACKEND_URL}/api/images/${target}`;
  }

  if (typeof target === 'string') {
    if (target.startsWith('http')) return target;
    if (!isNaN(target)) return `${BACKEND_URL}/api/images/${target}`;
    return `${BACKEND_URL}${target.startsWith('/') ? '' : '/'}${target}`;
  }

  // 2. 객체 안에서 파일 경로를 나타내는 다양한 필드명 체크 (filePath 추가!)
  const url = target.filePath || target.imageUrl || target.url || target.path || target.imagePath;
  if (url) {
    if (url.startsWith('http')) return url;
    const cleanPath = url.startsWith('/') ? url : `/${url}`;
    // upload 경로가 이미 포함되어 있는지 확인
    if (cleanPath.startsWith('/upload')) {
      return `${BACKEND_URL}${cleanPath}`;
    }
    return `${BACKEND_URL}/upload/review${cleanPath}`;
  }

  // 3. 경로가 없고 아이디만 있는 경우
  const id = target.imageId || target.id || target.reviewImageId;
  if (id) {
    return `${BACKEND_URL}/api/images/${id}`;
  }

  return null;
};

function ReviewSection({ 
  reviews = [], 
  meetupId, 
  isHost = false, 
  onWriteReview, 
  onLikeReview,
  onSortChange,
  onSearch,
  onReport  // 신고 추가 ...
}) {
  const router = useRouter();
  const dispatch = useDispatch();

  // 검색어 및 정렬 상태 관리
  const [searchText, setSearchText] = useState('');
  const [sortValue, setSortValue] = useState('latest');

  // 모달 오픈 상태 관리
  const [isModalOpen, setIsModalOpen] = useState(false);

  // Redux에서 AI 분석 관련 상태 가져오기
  const { analysisResult, loading: aiLoading } = useSelector((state) => {
    const reviewState = state.review || state.reviewReducer || {};
    return {
      analysisResult: reviewState.analysisResult,
      loading: reviewState.loading,
    };
  });

  // 대상 모임 ID 추출
  const targetMeetupId = meetupId || router.query.meetupId || router.query.id;

  // 비공개 후기('N')는 목록에서 제외
  const publicReviews = reviews.filter((review) => {
    const pub = review.isPublic;
    if (pub === 'N' || pub === 'n' || pub === false) {
      return false; 
    }
    return true;
  });

  // ==========================================
  // 평점 및 개수 동적 계산 로직
  // ==========================================
  const totalReviewsCount = publicReviews.length;

  const averageRating = totalReviewsCount > 0
    ? (publicReviews.reduce((acc, cur) => acc + (Number(cur.rating) || 0), 0) / totalReviewsCount).toFixed(1)
    : '0.0';

  const ratingCounts = { 5: 0, 4: 0, 3: 0, 2: 0, 1: 0 };
  publicReviews.forEach((review) => {
    const rate = Math.round(Number(review.rating) || 0);
    if (rate >= 1 && rate <= 5) {
      ratingCounts[rate] += 1;
    }
  });

  const getRatingPercent = (score) => {
    if (totalReviewsCount === 0) return 0;
    return Math.round((ratingCounts[score] / totalReviewsCount) * 100);
  };
  // ==========================================

  // AI 후기 인사이트 버튼 클릭 핸들러
  const handleOpenAiModal = () => {
    if (!targetMeetupId) {
      alert('모임 정보를 찾을 수 없습니다.');
      return;
    }

    setIsModalOpen(true);
    // Redux 사가 액션 디스패치
    dispatch(analyzeReviewsRequest(targetMeetupId));
  };

  // 후기 작성 이동
  const handleWriteClick = () => {
  dispatch(resetReviewState()); // ★ 작성 페이지로 넘어가기 전 Redux 상태(success 등)를 깨끗하게 리셋!

  if (onWriteReview) {
    onWriteReview();
    return;
  }

  if (targetMeetupId) {
    router.push(`/user/meetup/review/write?meetupId=${targetMeetupId}`);
  } else {
    alert('모임 정보(meetupId)를 찾을 수 없습니다.');
  }
};

  // 후기 상세보기 이동
  const handleDetailClick = (reviewId) => {
    if (!reviewId) return;

    if (targetMeetupId) {
      router.push(`/user/meetup/review/detailreview?reviewId=${reviewId}&meetupId=${targetMeetupId}`);
    } else {
      router.push(`/user/meetup/review/detailreview?reviewId=${reviewId}`);
    }
  };

  // 좋아요 클릭 핸들러
  const handleLikeClick = (reviewId) => {
    if (onLikeReview) {
      onLikeReview(reviewId);
    } else {
      console.warn("onLikeReview 함수가 부모 컴포넌트로부터 전달되지 않았습니다.");
    }
  };

  // 정렬 변경 핸들러
  const handleInternalSortChange = (value) => {
    setSortValue(value);
    const sortParam = value === 'likes' ? 'likesCount,desc' : 'id,desc';

    if (onSortChange) {
      onSortChange(sortParam);
    }
  };

  // 검색 버튼 클릭 및 엔터키 입력 시 동작
  const handleSearchSubmit = (value) => {
    if (onSearch) {
      onSearch(value.trim());
    } else {
      console.warn("❌ onSearch 함수가 부모로부터 전달되지 않았습니다!");
    }
  };

  return (
    <div id="review-section">
      <Row justify="space-between" align="middle" style={{ marginBottom: 24 }}>
        <Col>
          <Space size={12} align="center">
            <Title level={4} style={{ margin: 0 }}>모임 후기</Title>
            
            {/* ★ [추가] 개설자(isHost)일 때만 보이는 AI 인사이트 버튼 */}
            {isHost && (
              <Button
                type="dashed"
                icon={<RobotOutlined style={{ color: '#722ed1' }} />}
                style={{ borderColor: '#722ed1', color: '#722ed1', fontWeight: 500 }}
                onClick={handleOpenAiModal}
              >
                AI 후기 인사이트
              </Button>
            )}
          </Space>
        </Col>

        <Col>
          <Button type="primary" onClick={handleWriteClick}>
            후기 작성하기
          </Button>
        </Col>
      </Row>

      {/* 검색 및 정렬 */}
      <Row gutter={12} style={{ marginBottom: 24 }}>
        <Col flex="auto">
          <Input.Search 
            placeholder="후기 검색어를 입력하세요" 
            allowClear
            enterButton="검색"
            size="middle"
            value={searchText}
            onChange={(e) => setSearchText(e.target.value)}
            onSearch={handleSearchSubmit}
          />
        </Col>

        <Col>
          <Select
            value={sortValue}
            onChange={handleInternalSortChange}
            style={{ width: 110 }}
            options={[
              { value: 'latest', label: '최신순' },
              { value: 'likes', label: '좋아요순' },
            ]}
          />
        </Col>
      </Row>

      {/* 평점 통계 카드 */}
      <Card className="review-rating-card">
        <Row gutter={40} align="middle">
          <Col xs={24} sm={8}>
            <div className="review-score" style={{ textAlign: 'center' }}>
              <Title level={1} style={{ margin: 0 }}>{averageRating}</Title>
              <Rate disabled allowHalf value={Number(averageRating)} style={{ margin: '8px 0' }} />
              <div><Text type="secondary">총 후기 {totalReviewsCount}개</Text></div>
            </div>
          </Col>

          <Col xs={24} sm={16}>
            {[5, 4, 3, 2, 1].map((score) => (
              <Row key={score} align="middle" gutter={8} style={{ marginBottom: 4 }}>
                <Col flex="40px">
                  <Text>{score}점</Text>
                </Col>

                <Col flex="auto">
                  <Progress
                    percent={getRatingPercent(score)}
                    showInfo={false}
                  />
                </Col>

                <Col flex="30px" style={{ textAlign: 'right' }}>
                  <Text type="secondary" style={{ fontSize: '12px' }}>{ratingCounts[score]}</Text>
                </Col>
              </Row>
            ))}
          </Col>
        </Row>
      </Card>

      {/* 후기 목록 */}
      <Space
        direction="vertical"
        size={16}
        style={{
          width: '100%',
          marginTop: 20,
        }}
      >
        {publicReviews.map((review) => {
          const imageList = 
            review.images || 
            review.reviewImages || 
            review.imageUrls || 
            review.reviewImageList || 
            [];
          
          const isLiked = Boolean(review.isLiked || review.liked);

          return (
            <Card key={review.id} className="review-card">
              <Row justify="space-between" align="middle">
                <Col>
                  <Space>
                    <Avatar icon={<UserOutlined />} />

                    <div>
                      <Text strong>{review.memberNickname || review.nickname || '작성자'}</Text>

                      <div>
                        <Text type="secondary">
                          {review.createdAt ? String(review.createdAt).substring(0, 10) : ''}
                        </Text>
                      </div>
                    </div>
                  </Space>
                </Col>

                <Col>
                  <Space>
                    <Button
                      type="default"
                      size="small"
                      onClick={() => handleDetailClick(review.id)}
                    >
                      상세보기
                    </Button>

                    <Button
                      type="text"
                      danger
                      onClick={() =>
                        onReport(
                          "REVIEW",
                          review.id,
                          review.memberId
                        )
                      }
                    >
                      신고
                    </Button>
                  </Space>
                </Col>
              </Row>

              <Rate disabled value={review.rating} style={{ marginTop: 12 }} />

              <Paragraph style={{ marginTop: 12 }}>{review.content}</Paragraph>

              {Array.isArray(imageList) && imageList.length > 0 && (
                <div style={{ marginTop: 12, marginBottom: 12 }}>
                  <Image.PreviewGroup>
                    <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
                      {imageList.map((imgItem, idx) => {
                        const imgSrc = getImageUrl(imgItem);
                        if (!imgSrc) return null;

                        return (
                          <Image
                            key={imgItem?.reviewImageId || imgItem?.imageId || imgItem?.id || idx}
                            src={imgSrc}
                            alt={`review-attached-${idx}`}
                            style={{
                              width: '90px',
                              height: '90px',
                              objectFit: 'cover',
                              borderRadius: '8px',
                            }}
                          />
                        );
                      })}
                    </div>
                  </Image.PreviewGroup>
                </div>
              )}

              <Button
                type="text"
                icon={isLiked ? <LikeFilled style={{ color: '#1890ff' }} /> : <LikeOutlined />}
                style={{ color: isLiked ? '#1890ff' : 'inherit' }}
                onClick={() => handleLikeClick(review.id)}
              >
                {review.likesCount ?? review.likes ?? 0}
              </Button>

              <ReviewComments reviewId={review.id} />
            </Card>
          );
        })}
      </Space>

      {/* ★ [추가] AI 후기 분석 결과 모달 */}
      <Modal
        title={
          <Space>
            <RobotOutlined style={{ color: '#722ed1' }} />
            <span>AI 모임 후기 인사이트</span>
          </Space>
        }
        open={isModalOpen}
        onOk={() => setIsModalOpen(false)}
        onCancel={() => setIsModalOpen(false)}
        footer={[
          <Button key="close" type="primary" onClick={() => setIsModalOpen(false)}>
            확인
          </Button>,
        ]}
        width={600}
      >
        <div style={{ minHeight: '150px', padding: '10px 0' }}>
          {aiLoading ? (
            <div style={{ textAlign: 'center', padding: '40px 0' }}>
              <Spin indicator={<LoadingOutlined style={{ fontSize: 36, color: '#722ed1' }} spin />} />
              <div style={{ marginTop: 16 }}>
                <Text type="secondary">AI가 모임 후기를 분석하고 있습니다...</Text>
              </div>
            </div>
          ) : (
            <div>
              <Paragraph style={{ whiteSpace: 'pre-wrap', lineHeight: '1.6' }}>
                {analysisResult || '분석된 후기 내용이 없거나 결과가 비어있습니다.'}
              </Paragraph>
            </div>
          )}
        </div>
      </Modal>
    </div>
  );
}

export default ReviewSection;