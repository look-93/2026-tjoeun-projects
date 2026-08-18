import React from 'react';
import { Card, Space, Row, Button, Typography } from 'antd';
import { EnvironmentOutlined } from '@ant-design/icons';

const { Text } = Typography;

function MeetupRecruitInfo({ meetup }) {
  return (
    <Card title="모집 정보" className="meetup-side-card">
      <Space direction="vertical" size={16} style={{ width: '100%' }}>
        <Row justify="space-between">
          <Text type="secondary">인원</Text>

          <Text strong>
            {meetup.participants} / {meetup.maxParticipants}
          </Text>
        </Row>

        <Row justify="space-between">
          <Text type="secondary">지역</Text>

          <Text strong>
            <EnvironmentOutlined /> {meetup.location}
          </Text>
        </Row>

        <Button type="primary" size="large" block>
          신청하기
        </Button>
      </Space>
    </Card>
  );
}

export default MeetupRecruitInfo;
