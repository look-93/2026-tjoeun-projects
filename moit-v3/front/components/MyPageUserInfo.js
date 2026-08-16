import React from 'react';
import { Card, Col, Row, Typography } from 'antd';

const { Title, Text } = Typography;

function MyPageUserInfo({ user }) {
  const infoItems = [
    {
      label: '닉네임',
      value: user?.nickname,
    },
    {
      label: '이메일',
      value: user?.email,
    },
    {
      label: '가입일',
      value: user?.createdAt,
    },
    {
      label: '관심 카테고리',
      value: user?.categories,
    },
  ];

  return (
    <Card className="mypage-user-info">
      <Title level={3}>사용자 정보</Title>

      <Row gutter={[16, 16]}>
        {infoItems.map((item) => (
          <Col key={item.label} xs={24} sm={12}>
            <div className="mypage-info-box">
              <Text type="secondary">{item.label}</Text>
              <div>{item.value}</div>
            </div>
          </Col>
        ))}
      </Row>
    </Card>
  );
}

export default MyPageUserInfo;
