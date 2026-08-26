import React, { useState, useEffect } from 'react';
import { Card, Radio, Input, Button, Typography, Divider, Space, Upload, Modal } from 'antd';
import { StarFilled, PlusOutlined } from '@ant-design/icons';
import { useDispatch, useSelector } from 'react-redux';
import { useRouter } from 'next/router';
import axios from 'axios';
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
        const initialFiles = reviewDetail.images.map((img, idx) => {
          // 💡 파일명만 올 경우 앞에 백엔드 정적 리소스 주소를 붙여줍니다.
          // (백엔드 업로드 경로 구조에 맞게 폴더명 수정이 필요할 수 있습니다. 예: /upload/review/ 등)
          const rawUrl = img.imageUrl || '';
          const fullImageUrl = rawUrl.startsWith('http') 
            ? rawUrl 
            : `http://localhost:8080/upload/review/${rawUrl}`; // 👈 백엔드 이미지 저장 경로에 맞게 확인해주세요!

          return {
            uid: `-${idx}`,
            name: rawUrl.substring(rawUrl.lastIndexOf('_') + 1) || `image-${idx}.png`,
            status: 'done',
            id: img.reviewImageId || img.imageId || img.id,
            url: fullImageUrl,
            thumbUrl: fullImageUrl, // 썸네일 미리보기 경로 완성
          };
        });
        setFileList(initialFiles);
      }
    }
  }, [isEditMode, reviewDetail]);

  // 3. 성공/실패 처리
  useEffect(() => {
    if (success) {
      alert(isEditMode ? '리뷰가 성공적으로 수정되었습니다.' : '리뷰가 성공적으로 등록되었습니다.');
      dispatch(resetReviewState());

      // 1. queryMeetupId가 우선, 없으면 reviewDetail 내부의 meetupId 확인 (그것도 없으면 기본 6)
      const targetMeetupId = queryMeetupId || (reviewDetail && reviewDetail.meetupId) || 6;

      // 2. 탭 이동과 함께 해시(#review-section)를 붙여서 스크롤 이동 유도
      router.push(`/user/meetup/detail?meetupId=${targetMeetupId}&tab=review#review-section`);
    }

    if (error) {
      console.log('🚨 최종 수신된 에러 값:', error);

      // 서버가 보낸 실제 응답 메시지를 안전하게 추출
      let errorMsg = '알 수 없는 오류가 발생했습니다.';

      if (typeof error === 'object') {
        const serverData = error.response?.data || error;
        
        if (typeof serverData === 'string') {
          errorMsg = serverData;
        } else {
          errorMsg = serverData.error || serverData.message || JSON.stringify(serverData);
        }
      } else {
        errorMsg = String(error);
      }

      alert(errorMsg);
      dispatch(resetReviewState());
    }
  }, [success, error, dispatch, router, queryMeetupId, isEditMode, fromMypage, reviewDetail]);

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

  const handleSubmit = async () => {
    if (!content.trim()) {
      alert('리뷰 내용을 입력해주세요.');
      return;
    }

    const rawMeetupId = router.query.meetupId || router.query.id || router.query.meetup_id;

    if (!isEditMode && !rawMeetupId) {
      alert('모임 정보(meetupId)를 찾을 수 없습니다.');
      return;
    }

    try {
      const newFiles = fileList.filter((file) => file.originFileObj && !file.id);
      let uploadedImageIds = fileList
        .filter((file) => file.id)
        .map((file) => file.id);

      if (newFiles.length > 0) {
        const formData = new FormData();
        newFiles.forEach((file) => {
          formData.append('images', file.originFileObj);
        });

        // 💡 1. 로컬 스토리지에서 액세스 토큰을 안전하게 꺼냅니다.
        const token = localStorage.getItem('accessToken') || localStorage.getItem('token');

        // 💡 2. 헤더에 Authorization을 포함하여 백엔드로 전송합니다.
        const imageResponse = await axios.post('http://localhost:8080/api/reviews/images', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
            'Authorization': token ? `Bearer ${token}` : '', 
          },
          withCredentials: true,
        });

        const newImageIds = imageResponse.data;
        uploadedImageIds = [...uploadedImageIds, ...newImageIds];
      }

      const requestDto = {
        meetupId: rawMeetupId ? Number(rawMeetupId) : undefined,
        rating,
        isPublic,
        content: content.trim(),
        imageIds: uploadedImageIds, 
      };

      if (isEditMode) {
        dispatch(
          updateReviewRequest({
            reviewId: Number(queryReviewId),
            ...requestDto,
          })
        );
      } else {
        dispatch(createReviewRequest(requestDto));
      }
    } catch (e) {
      console.error('이미지 업로드 또는 리뷰 전송 실패:', e);
      alert('이미지 업로드 중 오류가 발생했습니다.');
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