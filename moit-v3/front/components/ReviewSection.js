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
} from 'antd';
import { SearchOutlined, LikeOutlined, LikeFilled, UserOutlined } from '@ant-design/icons';
import { useRouter } from 'next/router';

const { Title, Text, Paragraph } = Typography;

const BACKEND_URL = 'http://localhost:8080'; // 본인 백엔드 주소

const getImageUrl = (imgItem) => {
  if (imgItem === null || imgItem === undefined) return null;

  if (typeof imgItem === 'number') {
    return `${BACKEND_URL}/api/images/${imgItem}`;
  }

  if (typeof imgItem === 'string') {
    if (imgItem.startsWith('http')) return imgItem;
    if (!isNaN(imgItem)) return `${BACKEND_URL}/api/images/${imgItem}`;
    return `${BACKEND_URL}${imgItem.startsWith('/') ? '' : '/'}${imgItem}`;
  }

  const url = imgItem.imageUrl || imgItem.url || imgItem.path;
  if (url) {
    if (url.startsWith('http')) return url;
    return `${BACKEND_URL}${url.startsWith('/') ? '' : '/'}${url}`;
  }

  const id = imgItem.imageId || imgItem.id || imgItem.reviewImageId;
  if (id) {
    return `${BACKEND_URL}/api/images/${id}`;
  }

  return null;
};

function ReviewSection({ 
  reviews = [], 
  meetupId, 
  onWriteReview, 
  onLikeReview,
  onSortChange,
  onSearch 
}) {
  const router = useRouter();

  // 검색어 상태 관리
  const [searchText, setSearchText] = useState('');
  // 정렬 상태 관리 (기본값: 최신순)
  const [sortValue, setSortValue] = useState('latest');

  // ★ [추가] 비공개 후기('N')는 목록에서 제외하고 공개 후기만 필터링
  const publicReviews = reviews.filter((review) => {
    const pub = review.isPublic;
    if (pub === 'N' || pub === 'n' || pub === false) {
      return false; 
    }
    return true;
  });

  // 후기 작성 이동
  const handleWriteClick = () => {
    if (onWriteReview) {
      onWriteReview();
      return;
    }

    const targetId = meetupId || router.query.meetupId || router.query.id;

    if (targetId) {
      router.push(`/user/meetup/review/write?meetupId=${targetId}`);
    } else {
      alert('모임 정보(meetupId)를 찾을 수 없습니다.');
    }
  };

  // 후기 상세보기 이동
  const handleDetailClick = (reviewId) => {
    if (!reviewId) return;

    const targetMeetupId = meetupId || router.query.meetupId || router.query.id;

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
    console.log("1. ReviewSection에서 검색 버튼 클릭됨! 검색어:", value);
    if (onSearch) {
      onSearch(value.trim());
    } else {
      console.warn("❌ onSearch 함수가 부모로부터 전달되지 않았습니다!");
    }
  };

  return (
    <div>
      <Row justify="space-between" align="middle" style={{ marginBottom: 24 }}>
        <Col>
          <Title level={4}>모임 후기</Title>
        </Col>

        <Col>
          <Button type="primary" onClick={handleWriteClick}>
            후기 작성하기
          </Button>
        </Col>
      </Row>

      {/* 검색 및 정렬 (Input.Search를 사용하여 '검색' 버튼 추가) */}
      <Row gutter={12} style={{ marginBottom: 24 }}>
        <Col flex="auto">
          <Input.Search 
            placeholder="후기 검색어를 입력하세요" 
            allowClear
            enterButton="검색" // 👈 명확한 '검색' 버튼 생성
            size="middle"
            value={searchText}
            onChange={(e) => setSearchText(e.target.value)}
            onSearch={handleSearchSubmit} // 👈 버튼 클릭이나 엔터 시 실행
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
        <Row gutter={40}>
          <Col xs={24} sm={8}>
            <div className="review-score">
              <Title level={1}>4.8</Title>
              <Rate disabled defaultValue={5} />
              <Text type="secondary">총 후기 {publicReviews.length}개</Text>
            </div>
          </Col>

          <Col xs={24} sm={16}>
            {[5, 4, 3, 2, 1].map((score) => (
              <Row key={score} align="middle" gutter={8}>
                <Col flex="40px">
                  <Text>{score}점</Text>
                </Col>

                <Col flex="auto">
                  <Progress
                    percent={score === 5 ? 70 : score === 4 ? 20 : 5}
                    showInfo={false}
                  />
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
                        router.push(
                          `/user/meetup/report/write?type=REVIEW&targetId=${review.id}`,
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
            </Card>
          );
        })}
      </Space>
    </div>
  );
}

export default ReviewSection;