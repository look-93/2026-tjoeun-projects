import React from 'react';
import { Row, Col, Card, Button, Typography } from 'antd';
import { SearchOutlined } from '@ant-design/icons';

const { Title, Text } = Typography;

function Hero() {
  return (
    <Row>
      <Col span={24}>
        <Card className="main-hero-card">
          <Row align="middle" justify="space-between">
            <Col>
              <Title level={1}>
                당신의 관심사가
                <br />
                새로운 모임이 되는 곳
              </Title>

              <Text type="secondary">대학생 · 일반인을 위한 모임 플랫폼</Text>

              <div className="main-hero-buttons">
                <Button type="primary" size="large" icon={<SearchOutlined />}>
                  모임 찾기
                </Button>

                <Button size="large">행사 보기</Button>
              </div>
            </Col>

            <Col>
              <div className="main-hero-icon">👥</div>
            </Col>
          </Row>
        </Card>
      </Col>
    </Row>
  );
}

export default Hero;
