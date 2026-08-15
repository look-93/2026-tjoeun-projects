import React from 'react';
import { Row, Col, Card, Typography } from 'antd';

const { Title, Text } = Typography;

function CategoryList({ categories }) {
  return (
    <Row gutter={[12, 12]}>
      <Col span={24}>
        <Title level={3}>카테고리</Title>

        <Text type="secondary">관심 있는 모임을 찾아보세요</Text>
      </Col>

      {categories.map((category) => (
        <Col xs={12} sm={8} md={6} lg={3} key={category.id}>
          <Card hoverable className="category-card">
            <div className="category-icon">{category.icon}</div>

            <Text strong>{category.name}</Text>
          </Card>
        </Col>
      ))}
    </Row>
  );
}

export default CategoryList;
