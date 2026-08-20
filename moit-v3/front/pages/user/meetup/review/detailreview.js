import React, { useEffect } from 'react';
import { Card, Button, Typography, Space, Rate, Avatar, Image } from 'antd';
import {
  UserOutlined,
  ArrowLeftOutlined,
  EditOutlined,
  DeleteOutlined,
  HeartOutlined,
  HeartFilled,
} from '@ant-design/icons';
import { useRouter } from 'next/router';
import { useDispatch, useSelector } from 'react-redux';

// ★ 리듀서 파일에서 exported 액션 생성자들을 직접 가져옵니다.
import {
  getReviewDetailRequest,
  deleteReviewRequest,
  toggleReviewLikeRequest,
} from '../../../../reducers/reviewReducer'; // 프로젝트 파일 위치에 맞춰 경로 확인

const { Text, Paragraph } = Typography;

function DetailReviewPage() {
  const router = useRouter();
  const dispatch = useDispatch();

  // URL Query에서 reviewId와 meetupId 추출
  const { reviewId, meetupId } = router.query;

  // ★ 리듀서의 initialState 구조에 맞춰 상태 추출
  // initialState = { reviewDetail, loading, error, success ... }
  const { reviewDetail, loading } = useSelector((state) => state.review || {});

  // 1. 리뷰 상세 데이터 요청 (리듀서: getReviewDetailRequest)
  useEffect(() => {
    if (!router.isReady || !reviewId) return;

    // 리듀서의 getReviewDetailRequest 액션 디스패치
    dispatch(getReviewDetailRequest(Number(reviewId)));
  }, [dispatch, router.isReady, reviewId]);

  // 2. 리뷰 삭제 핸들러 (리듀서: deleteReviewRequest)
  const handleDelete = () => {
    if (confirm('정말 후기를 삭제하시겠습니까?')) {
      //  삭제 핸들러 내 액션 호출
      dispatch(deleteReviewRequest(Number(reviewId)));
      alert('삭제되었습니다.');

      // 삭제 후 목록이나 이전 화면으로 이동
      if (meetupId) {
        router.push(`/user/meetup/detail?meetupId=${meetupId}&tab=review`);
      } else {
        router.back();
      }
    }
  };

  // 3. 좋아요 토글 핸들러 (리듀서: toggleReviewLikeRequest)
  const handleLike = () => {
    if (!reviewId) return;

    // 리듀서의 toggleReviewLikeRequest 액션 실행
    // 리듀서 내부 toggleReviewLikeSuccess가 action.payload로 reviewId를 받아 가공함
    dispatch(toggleReviewLikeRequest(Number(reviewId)));
  };

  // 백엔드 이미지 배열 처리
  const images = reviewDetail?.images || [];

  return (
    <div style={{ padding: '16px', maxWidth: '680px', margin: '0 auto' }}>
      {/* 상단 네비게이션 */}
      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 12 }}>
        <Button
          type="text"
          icon={<ArrowLeftOutlined />}
          onClick={() => router.back()}
          style={{ paddingLeft: 0 }}
        >
          목록으로
        </Button>

        
      </div>

      {/* 리듀서의 loading 상태 연결 */}
      <Card loading={loading} bodyStyle={{ padding: '20px' }}>
        {/* 작성자 정보 및 수정/삭제 */}
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Space size={12}>
            <Avatar
              size={40}
              icon={<UserOutlined />}
              src={reviewDetail?.userImage || reviewDetail?.userProfile}
            />
            <div>
              <Text strong style={{ fontSize: '15px', display: 'block' }}>
                {reviewDetail?.nickname || reviewDetail?.userName || '작성자'}
              </Text>
              <Text type="secondary" style={{ fontSize: '12px' }}>
                {reviewDetail?.createdAt
                  ? String(reviewDetail.createdAt).substring(0, 10)
                  : ''}
              </Text>
            </div>
          </Space>

          <Space size={4}>
            <Button
              type="text"
              size="small"
              icon={<EditOutlined />}
              onClick={() =>
                router.push(
                  `/user/meetup/review/write?reviewId=${reviewId}&meetupId=${meetupId}&edit=true`,
                )
              }
            >
              수정
            </Button>
            <Button
              type="text"
              danger
              size="small"
              icon={<DeleteOutlined />}
              onClick={handleDelete}
            >
              삭제
            </Button>
          </Space>
        </div>

        {/* 평점 */}
        <div
          style={{
            margin: '16px 0 12px',
            padding: '12px 16px',
            backgroundColor: '#fafafa',
            borderRadius: '8px',
            display: 'flex',
            alignItems: 'center',
            gap: '8px',
          }}
        >
          <Rate
            disabled
            value={reviewDetail?.rating || 0}
            style={{ fontSize: 16 }}
          />
          <Text strong style={{ fontSize: 14 }}>
            {reviewDetail?.rating ? `${reviewDetail.rating}.0` : '0.0'}
          </Text>
        </div>

        {/* 리뷰 본문 */}
        <Paragraph
          style={{
            fontSize: '15px',
            lineHeight: '1.6',
            color: '#262626',
            marginBottom: 16,
            whiteSpace: 'pre-wrap',
          }}
        >
          {reviewDetail?.content || '등록된 후기 내용이 없습니다.'}
        </Paragraph>

        {/* 리뷰 이미지 목록 */}
        {images.length > 0 && (
          <div style={{ marginBottom: 16 }}>
            <Image.PreviewGroup>
              <div
                style={{
                  display: 'grid',
                  gridTemplateColumns:
                    images.length === 1
                      ? '1fr'
                      : 'repeat(auto-fill, minmax(120px, 1fr))',
                  gap: '8px',
                }}
              >
                {images.map((imgUrl, index) => (
                  <Image
                    key={index}
                    src={typeof imgUrl === 'string' ? imgUrl : imgUrl.url || imgUrl.src}
                    alt={`review-img-${index}`}
                    style={{
                      width: '100%',
                      height: '120px',
                      objectFit: 'cover',
                      borderRadius: '6px',
                    }}
                  />
                ))}
              </div>
            </Image.PreviewGroup>
          </div>
        )}

        
      </Card>
    </div>
  );
}

export default DetailReviewPage;