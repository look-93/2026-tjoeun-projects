import React from 'react';
import { Card, Space, Typography } from 'antd';
import { EnvironmentOutlined } from '@ant-design/icons';

const { Text } = Typography;

function MeetupMap() {
  return (
    <Card title="모임 위치" className="meetup-side-card">
      <div className="meetup-map-placeholder">
        <Space direction="vertical" align="center">
          <EnvironmentOutlined />

          <Text>지도 영역</Text>
        </Space>
      </div>
    </Card>
  );
}

export default MeetupMap;
