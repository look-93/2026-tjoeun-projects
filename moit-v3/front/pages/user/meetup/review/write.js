import React, { useState, useEffect } from 'react';
import { Card, Radio, Input, Button, Typography, Divider, Space, Upload, Modal } from 'antd';
import { StarFilled, PlusOutlined } from '@ant-design/icons';
import { useDispatch, useSelector } from 'react-redux';
import { useRouter } from 'next/router';
import { 
  createReviewRequest, 
  updateReviewRequest, 
  getReviewDetailRequest, 
  resetReviewState 
} from '../../../../reducers/reviewReducer';

const { Title, Text } = Typography;
const { TextArea } = Input;

const getBase64 = (file) =>
  new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = (error) => reject(error);
  });

function ReviewWritePage() {
  const dispatch = useDispatch();
  const router = useRouter();

  const { loading, success, error, reviewDetail } = useSelector((state) => {
    if (!state) return {};
    return state.review || state.reviewReducer || {};
  });

  const [rating, setRating] = useState(3);
  const [isPublic, setIsPublic] = useState('Y');
  const [content, setContent] = useState('');

  // 이미지 상태
  const [fileList, setFileList] = useState([]);
  const [previewOpen, setPreviewOpen] = useState(false);
  const [previewImage, setPreviewImage] = useState('');
  const [previewTitle, setPreviewTitle] = useState('');

  // URL 파라미터에서 reviewId 및 meetupId 추출 (안전장치 추가)
  const queryReviewId = router.isReady ? router.query.reviewId : null;
  const queryMeetupId = router.isReady ? (router.query.meetupId || router.query.id || router.query.meetup_id) : null;
  
  // 마이페이지에서 왔는지 여부 파악
  const fromMypage = router.isReady ? router.query.from === 'mypage' : false;

  // 수정 모드 여부 판단
  const isEditMode = Boolean(queryReviewId);

  // 1. 수정 모드일 때 기존 리뷰 상세 정보 조회
  useEffect(() => {
    if (isEditMode && queryReviewId) {
      dispatch(getReviewDetailRequest(queryReviewId));
    }
  }, [isEditMode, queryReviewId, dispatch]);

  // 2. 조회된 기존 리뷰 데이터 폼에 채워넣기
  useEffect(() => {
    if (isEditMode && reviewDetail) {
      setContent(reviewDetail.content || '');
      setRating(reviewDetail.rating || 3);
      setIsPublic(reviewDetail.isPublic || 'Y');
      
      // 기존 이미지 매핑 (있는 경우)
      if (reviewDetail.images && Array.isArray(reviewDetail.images)) {
        const initialFiles = reviewDetail.images.map((img, idx) => ({
          uid: `-${idx}`,
          name: `image-${idx}.png`,
          status: 'done',
          id: img.imageId || img.id,
          url: img.imageUrl,
        }));
        setFileList(initialFiles);
      }
    }
  }, [isEditMode, reviewDetail]);

  // 3. 성공/실패 처리
  useEffect(() => {
    if (success) {
      alert(isEditMode ? '리뷰가 성공적으로 수정되었습니다.' : '리뷰가 성공적으로 등록되었습니다.');
      dispatch(resetReviewState());

      // ★ 마이페이지 리뷰 목록 페이지로 정확히 이동
      if (fromMypage) {
        router.push('/user/mypage/review');
      } else if (queryMeetupId) {
        router.push(`/user/meetup/detail?meetupId=${queryMeetupId}&tab=review`);
      } else {
        router.back();
      }
    }

   if (error) {
      console.log('🚨 최종 수신된 에러 값:', error);

      // error가 문자열이든 객체든 간에 문자열로 변환하여 체크합니다.
      const errorStr = typeof error === 'object' ? JSON.stringify(error) : String(error);

      // 400 에러이거나 Request failed가 포함되어 있다면 백엔드 욕설 필터링 차단으로 간주!
      if (errorStr.includes('400') || errorStr.includes('Request failed') || errorStr.includes('부적절') || errorStr.includes('비속어')) {
        alert('부적절한 표현(욕설, 비방 등)이 포함되어 있어 리뷰를 등록할 수 없습니다.');
      } else {
        alert(`리뷰 ${isEditMode ? '수정' : '등록'} 실패: ${errorStr}`);
      }
      
      dispatch(resetReviewState());
    }
  }, [success, error, dispatch, router, queryMeetupId, isEditMode, fromMypage]);

  const handleImageChange = ({ fileList: newFileList }) => {
    setFileList(newFileList);
  };

  const handlePreview = async (file) => {
    if (!file.url && !file.preview) {
      file.preview = await getBase64(file.originFileObj);
    }
    setPreviewImage(file.url || file.preview);
    setPreviewOpen(true);
    setPreviewTitle(file.name || file.url?.substring(file.url.lastIndexOf('/') + 1));
  };

  const handleSubmit = () => {
    if (!content.trim()) {
      alert('리뷰 내용을 입력해주세요.');
      return;
    }

    // 안전하게 meetupId 추출
    const rawMeetupId = router.query.meetupId || router.query.id || router.query.meetup_id;

    if (!isEditMode && !rawMeetupId) {
      console.error('현재 router 쿼리 상태:', router.query);
      alert('모임 정보(meetupId)를 찾을 수 없습니다. 페이지를 다시 로드해 주세요.');
      return;
    }

    const imageIds = fileList
      .map((file) => file.id || file.response?.id)
      .filter((id) => id !== undefined);

    const requestDto = {
      meetupId: rawMeetupId ? Number(rawMeetupId) : undefined,
      rating,
      isPublic,
      content: content.trim(),
      imageIds,
    };

    // 전송 직전 데이터 확인용 로그
    console.log('🚀 서버로 전송할 최종 requestDto:', requestDto);

    if (isEditMode) {
      console.log('수정 요청 실행 (reviewId):', queryReviewId, requestDto);
      dispatch(
        updateReviewRequest({
          reviewId: Number(queryReviewId),
          ...requestDto,
        })
      );
    } else {
      console.log('등록 요청 실행:', requestDto);
      dispatch(createReviewRequest(requestDto));
    }
  };

  const uploadButton = (
    <div>
      <PlusOutlined />
      <div style={{ marginTop: 8 }}>사진 추가</div>
    </div>
  );

  return (
    <div className="review-write-page">
      <Card className="review-write-card">
        <div className="review-write-header">
          <Text className="review-write-badge">
            {isEditMode ? '리뷰 수정' : '리뷰 등록'}
          </Text>
          <Title level={2} className="review-write-title">
            {isEditMode ? '작성하신 리뷰를 수정하시겠어요?' : '방문하신 "모임"은 만족스러우셨나요?'}
          </Title>
          <Text type="secondary">
            {isEditMode ? '변경할 솔직한 후기를 남겨주세요.' : '모임에 대한 솔직한 후기를 남겨주세요.'}
          </Text>
        </div>

        <Divider />

        {/* 별점 */}
        <div className="review-write-field">
          <Title level={5}>평가 별점</Title>
          <div className="review-star-rating">
            {[1, 2, 3, 4, 5].map((star) => (
              <StarFilled
                key={star}
                className={star <= rating ? 'review-star active' : 'review-star'}
                onClick={() => setRating(star)}
              />
            ))}
          </div>
          <Text type="secondary">{rating}점을 선택하셨습니다.</Text>
        </div>

        {/* 공개 설정 */}
        <div className="review-write-field">
          <Title level={5}>공개 설정</Title>
          <div className="review-public-box">
            <div>
              <Text strong>리뷰 공개 여부</Text>
              <div>
                <Text type="secondary">
                  다른 이용자들에게 후기를 공개할지 선택합니다.
                </Text>
              </div>
            </div>
            <Radio.Group
              value={isPublic}
              onChange={(e) => setIsPublic(e.target.value)}
            >
              <Radio value="Y">공개</Radio>
              <Radio value="N">비공개</Radio>
            </Radio.Group>
          </div>
        </div>

        {/* 상세 내용 */}
        <div className="review-write-field">
          <Title level={5}>상세 내용</Title>
          <TextArea
            rows={6}
            maxLength={500}
            showCount
            value={content}
            onChange={(e) => setContent(e.target.value)}
            placeholder="다른 이용자들에게 도움이 될 수 있도록 솔직한 경험과 소감을 남겨주세요."
          />
        </div>

        {/* 사진 첨부 */}
        <div className="review-write-field" style={{ marginTop: 24 }}>
          <Title level={5}>
            사진 첨부 <Text type="secondary" style={{ fontSize: 13, fontWeight: 'normal' }}>(선택, 최대 5장)</Text>
          </Title>

          <Upload
            listType="picture-card"
            fileList={fileList}
            onPreview={handlePreview}
            onChange={handleImageChange}
            beforeUpload={() => false}
            multiple
            accept="image/*"
          >
            {fileList.length >= 5 ? null : uploadButton}
          </Upload>

          <Modal
            open={previewOpen}
            title={previewTitle}
            footer={null}
            onCancel={() => setPreviewOpen(false)}
          >
            <img alt="preview" style={{ width: '100%' }} src={previewImage} />
          </Modal>
        </div>

        {/* 버튼 */}
        <div className="review-write-actions" style={{ marginTop: 32 }}>
          <Space>
            <Button size="large" onClick={() => router.back()}>
              취소
            </Button>
            <Button
              type="primary"
              size="large"
              loading={loading}
              onClick={handleSubmit}
            >
              {isEditMode ? '리뷰 수정하기' : '리뷰 등록하기'}
            </Button>
          </Space>
        </div>
      </Card>
    </div>
  );
}

export default ReviewWritePage;