import React from 'react';
import { Card, Row, Col, Typography } from 'antd';

const { Title, Text } = Typography;

function AdminHeader() {
  return (
    <Card
      className="admin-header"
      bordered={false}
    >
      <Row
        align="middle"
        justify="space-between"
      >
        <Col>
          <Title
            level={3}
            className="admin-header-title"
          >
            관리자관리
          </Title>
        </Col>

        <Col>
          <Text className="admin-header-user">
            최고관리자
          </Text>
        </Col>
      </Row>
    </Card>
  );
}

export default AdminHeader;