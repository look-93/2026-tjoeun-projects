import React from 'react';
import { Card, Space, Row, Col, Typography } from 'antd';

const { Text } = Typography;

function RecommendedMeetups({ recommendedMeetups }) {
  return (
    <Card title="추천 모임" className="meetup-side-card">
      <Space direction="vertical" style={{ width: '100%' }} size={12}>
        {recommendedMeetups.map((item) => (
          <Card
            key={item.id}
            size="small"
            hoverable
            className="recommended-meetup-card"
          >
            <Row gutter={12}>
              <Col span={8}>
                <div className="recommended-image">🖼️</div>
              </Col>

              <Col span={16}>
                <Text strong>{item.title}</Text>

                <div>
                  <Text type="secondary">{item.location}</Text>
                </div>
              </Col>
            </Row>
          </Card>
        ))}
      </Space>
    </Card>
  );
}

export default RecommendedMeetups;
