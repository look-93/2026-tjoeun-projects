import React, { useState } from 'react';
import { Card, Radio, Input, Button, Typography, Divider, Space } from 'antd';
import { StarFilled } from '@ant-design/icons';

const { Title, Text } = Typography;
const { TextArea } = Input;

function ReviewWritePage() {
  const [rating, setRating] = useState(3);
  const [isPublic, setIsPublic] = useState('Y');

  return (
    <div className="review-write-page">
      <Card className="review-write-card">
        {/* 상단 */}
        <div className="review-write-header">
          <Text className="review-write-badge">리뷰 등록</Text>

          <Title level={2} className="review-write-title">
            방문하신 <span>"모임"</span>은 만족스러우셨나요?
          </Title>

          <Text type="secondary">모임에 대한 솔직한 후기를 남겨주세요.</Text>
        </div>

        <Divider />

        {/* 별점 */}
        <div className="review-write-field">
          <Title level={5}>평가 별점</Title>

          <div className="review-star-rating">
            {[1, 2, 3, 4, 5].map((star) => (
              <StarFilled
                key={star}
                className={
                  star <= rating ? 'review-star active' : 'review-star'
                }
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
            rows={8}
            maxLength={500}
            showCount
            placeholder="다른 이용자들에게 도움이 될 수 있도록 솔직한 경험과 소감을 남겨주세요."
          />
        </div>

        {/* 버튼 */}
        <div className="review-write-actions">
          <Space>
            <Button size="large">취소</Button>

            <Button type="primary" size="large">
              리뷰 등록하기
            </Button>
          </Space>
        </div>
      </Card>
    </div>
  );
}

export default ReviewWritePage;
