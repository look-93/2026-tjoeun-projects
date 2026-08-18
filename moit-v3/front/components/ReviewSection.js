import React from 'react';
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
} from 'antd';
import { SearchOutlined, LikeOutlined, UserOutlined } from '@ant-design/icons';
import { useRouter } from 'next/router';

const { Title, Text, Paragraph } = Typography;

function ReviewSection({ reviews = [] }) {
  const router = useRouter();

  return (
    <div>
      <Row justify="space-between" align="middle" style={{ marginBottom: 24 }}>
        <Col>
          <Title level={4}>모임 후기</Title>
        </Col>

        <Col>
          {/* meetupId=${meetup.meetupId} 아이디넘겨야하면추가 */}
          <Button
            type="primary"
            onClick={() => router.push(`/user/meetup/review/write`)}
          >
            후기 작성하기
          </Button>
        </Col>
      </Row>

      {/* 검색 */}
      <Row gutter={12} style={{ marginBottom: 24 }}>
        <Col flex="auto">
          <Input placeholder="후기 검색" prefix={<SearchOutlined />} />
        </Col>

        <Col>
          <Select
            defaultValue="latest"
            options={[
              { value: 'latest', label: '최신순' },
              { value: 'likes', label: '좋아요순' },
            ]}
          />
        </Col>
      </Row>

      {/* 평점 */}
      <Card className="review-rating-card">
        <Row gutter={40}>
          <Col xs={24} sm={8}>
            <div className="review-score">
              <Title level={1}>4.8</Title>

              <Rate disabled defaultValue={5} />

              <Text type="secondary">총 후기 {reviews.length}개</Text>
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
        {reviews.map((review) => (
          <Card key={review.id} className="review-card">
            <Row justify="space-between">
              <Col>
                <Space>
                  <Avatar icon={<UserOutlined />} />

                  <div>
                    <Text strong>{review.nickname}</Text>

                    <div>
                      <Text type="secondary">{review.date}</Text>
                    </div>
                  </div>
                </Space>
              </Col>

              <Col>
                {/* targetId=${meetup.meetupId} 아이디넘겨야하면추가 */}
                <Button
                  type="text"
                  danger
                  onClick={() =>
                    router.push(`/user/meetup/report/write?type=REVIEW`)
                  }
                >
                  신고
                </Button>
              </Col>
            </Row>

            <Rate disabled value={review.rating} style={{ marginTop: 12 }} />

            <Paragraph style={{ marginTop: 12 }}>{review.content}</Paragraph>

            <Button type="text" icon={<LikeOutlined />}>
              {review.likes}
            </Button>
          </Card>
        ))}
      </Space>
    </div>
  );
}

export default ReviewSection;
